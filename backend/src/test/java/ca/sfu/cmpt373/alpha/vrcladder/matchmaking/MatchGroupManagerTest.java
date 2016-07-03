package ca.sfu.cmpt373.alpha.vrcladder.matchmaking;

import ca.sfu.cmpt373.alpha.vrcladder.BaseTest;
import ca.sfu.cmpt373.alpha.vrcladder.scores.ScoreCard;
import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
import ca.sfu.cmpt373.alpha.vrcladder.util.MockMatchGroupGenerator;
import ca.sfu.cmpt373.alpha.vrcladder.util.MockTeamGenerator;
import ca.sfu.cmpt373.alpha.vrcladder.util.MockUserGenerator;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class MatchGroupManagerTest extends BaseTest {

    private MatchGroupManager matchGroupManager;
    private MatchGroup threeTeamMatchGroupFixture;
    private List<Team> threeTeamsFixture;
    private MatchGroup fourTeamMatchGroupFixture;
    private List<Team> fourTeamsFixture;

    private static final int FOURTH_TEAM_INDEX = 3;

    @Before
    public void setUp() {
        matchGroupManager = new MatchGroupManager(sessionManager);
        fourTeamMatchGroupFixture = MockMatchGroupGenerator.generateFourTeamMatchGroup();
        threeTeamMatchGroupFixture = MockMatchGroupGenerator.generateThreeTeamMatchGroup();
        int threeTeamsCount = 3;
        threeTeamsFixture = MockTeamGenerator.generateTeams(threeTeamsCount);
        int fourTeamsCount = 4;
        fourTeamsFixture = MockTeamGenerator.generateTeams(fourTeamsCount);

        // Store Users, Teams, and MatchGroups in database.
        Session session = sessionManager.getSession();
        Transaction transaction = session.beginTransaction();

        saveTeams(threeTeamMatchGroupFixture.getTeams(), session);
        session.save(threeTeamMatchGroupFixture);

        saveTeams(fourTeamMatchGroupFixture.getTeams(), session);
        session.save(fourTeamMatchGroupFixture);

        saveTeams(threeTeamsFixture, session);
        saveTeams(fourTeamsFixture, session);

        transaction.commit();
        session.close();
    }

    private void saveTeams(List<Team> teams, Session session) {
        for (Team team : teams) {
            session.save(team.getFirstPlayer());
            session.save(team.getSecondPlayer());
            session.save(team);
        }
    }

    @After
    public void tearDown() {
        MockUserGenerator.resetUserCount();
        MockTeamGenerator.resetTeamCount();
    }

    @Test
    public void testGetMatchGroup() {
        MatchGroup existingMatchGroup = matchGroupManager.getById(fourTeamMatchGroupFixture.getId());

        Assert.assertEquals(fourTeamMatchGroupFixture.getTeam1(), existingMatchGroup.getTeam1());
        Assert.assertEquals(fourTeamMatchGroupFixture.getTeam2(), existingMatchGroup.getTeam2());
        Assert.assertEquals(fourTeamMatchGroupFixture.getTeam3(), existingMatchGroup.getTeam3());
    }

    @Test
    public void testCreateMatchGroup() {
        Team team1 = MockTeamGenerator.generateTeam();
        Team team2 = MockTeamGenerator.generateTeam();
        Team team3 = MockTeamGenerator.generateTeam();
        List<Team> teams = Arrays.asList(team1, team2, team3);

        // Store Users and Teams in database.
        Session session = sessionManager.getSession();
        Transaction transaction = session.beginTransaction();
        for (Team team : teams) {
            session.save(team.getFirstPlayer());
            session.save(team.getSecondPlayer());
            session.save(team);
        }
        transaction.commit();

        MatchGroup newMatchGroup = matchGroupManager.create(teams);

		MatchGroup retrievedNewMatchGroup = session.get(MatchGroup.class, newMatchGroup.getId());
        session.close();

        Assert.assertEquals(team1, retrievedNewMatchGroup.getTeam1());
        Assert.assertEquals(team2, retrievedNewMatchGroup.getTeam2());
        Assert.assertEquals(team3, retrievedNewMatchGroup.getTeam3());
    }

    @Test
    public void testDeleteMatchGroup() {
        MatchGroup originalMatchGroup = matchGroupManager.deleteById(fourTeamMatchGroupFixture.getId());

        Session session = sessionManager.getSession();
        MatchGroup retrievedMatchGroup = session.get(MatchGroup.class, originalMatchGroup.getId());
        ScoreCard retrievedScoreCard = session.get(ScoreCard.class, originalMatchGroup.getScoreCard().getId());
        Team retrievedTeam1 = session.get(Team.class, originalMatchGroup.getTeam1().getId());
        Team retrievedTeam2 = session.get(Team.class, originalMatchGroup.getTeam2().getId());
        Team retrievedTeam3 = session.get(Team.class, originalMatchGroup.getTeam3().getId());
        session.close();

        Assert.assertNotNull(originalMatchGroup);
        Assert.assertNotNull(originalMatchGroup.getScoreCard());
        // Ensure that the match group is deleted.
        Assert.assertNull(retrievedMatchGroup);
        Assert.assertNull(retrievedScoreCard);

        // Ensure that the individual teams are not deleted.
        Assert.assertNotNull(retrievedTeam1);
        Assert.assertNotNull(retrievedTeam2);
        Assert.assertNotNull(retrievedTeam3);
    }

	@Test
	public void testAddTeamToMatchGroup() {
        matchGroupManager.setTeamsInMatchGroup(threeTeamMatchGroupFixture.getId(), fourTeamsFixture);

        Session session = sessionManager.getSession();
        MatchGroup retrievedMatchGroup = session.get(MatchGroup.class, threeTeamMatchGroupFixture.getId());
		session.close();

		Assert.assertEquals(retrievedMatchGroup.getTeams(), fourTeamsFixture);
	}

	@Test
    public void testRemoveTeamFromMatchGroup() {
        for (int i = 0; i < fourTeamMatchGroupFixture.getTeams().size(); i++) {
            tearDown();
            setUpBase();
            setUp();
            testRemoveTeamFromMatchGroup(fourTeamMatchGroupFixture.getTeams().get(i));
        }
    }

	private void testRemoveTeamFromMatchGroup(Team teamToRemove) {
        List<Team> expectedResults = new ArrayList<>(fourTeamMatchGroupFixture.getTeams());
        expectedResults.remove(teamToRemove);

		matchGroupManager.setTeamsInMatchGroup(fourTeamMatchGroupFixture.getId(), expectedResults);

        Session session = sessionManager.getSession();
        MatchGroup retrievedMatchGroup = session.get(MatchGroup.class, fourTeamMatchGroupFixture.getId());
		session.close();

        List<Team> retrievedTeams = retrievedMatchGroup.getTeams();
		for (int i = 0; i < expectedResults.size(); i++) {
            Assert.assertEquals(expectedResults.get(i), retrievedTeams.get(i));
        }
	}

	@Test
	public void testTradeTeamsInMatchGroups() {
        //note that teams appear in ranked order within MatchGroups,
        //so they may not appear in the order they were added in
        List<Team> expectedFourTeamMatchGroupTeams = Arrays.asList(
                fourTeamMatchGroupFixture.getTeam1(),
                fourTeamMatchGroupFixture.getTeam2(),
                fourTeamMatchGroupFixture.getTeam3(),
                threeTeamMatchGroupFixture.getTeam3());
        List<Team> expectedThreeTeamMatchGroupTeams = Arrays.asList(
                fourTeamMatchGroupFixture.getTeams().get(FOURTH_TEAM_INDEX),
                threeTeamMatchGroupFixture.getTeam1(),
                threeTeamMatchGroupFixture.getTeam2());

        matchGroupManager.tradeTeamsInMatchGroups(
                threeTeamMatchGroupFixture.getId(),
                threeTeamMatchGroupFixture.getTeam3(),
                fourTeamMatchGroupFixture.getId(),
                fourTeamMatchGroupFixture.getTeams().get(FOURTH_TEAM_INDEX));

        Session session = sessionManager.getSession();
        MatchGroup retrievedThreeTeamMatchGroup = session.get(MatchGroup.class, threeTeamMatchGroupFixture.getId());
        MatchGroup retrievedFourTeamMatchGroup = session.get(MatchGroup.class, fourTeamMatchGroupFixture.getId());
		session.close();

        Assert.assertEquals(retrievedThreeTeamMatchGroup.getTeams(), expectedThreeTeamMatchGroupTeams);
		Assert.assertEquals(retrievedFourTeamMatchGroup.getTeams(), expectedFourTeamMatchGroupTeams);
	}
}
