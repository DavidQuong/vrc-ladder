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
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

public class MatchGroupManagerTest extends BaseTest {

    private MatchGroupManager matchGroupManager;
    private MatchGroup matchGroupFixture;

    @Before
    public void setUp() {
        matchGroupManager = new MatchGroupManager(sessionManager);
        matchGroupFixture = MockMatchGroupGenerator.generateFourTeamMatchGroup();

        Session session = sessionManager.getSession();
        Transaction transaction = session.beginTransaction();
        for (Team team: matchGroupFixture.getTeams()) {
            session.save(team.getFirstPlayer());
            session.save(team.getSecondPlayer());
            session.save(team);
        }
        session.save(matchGroupFixture);
        transaction.commit();
        session.close();
    }

    @After
    public void tearDown() {
        MockUserGenerator.resetUserCount();
        MockTeamGenerator.resetTeamCount();
    }

    @Test
    public void testGetMatchGroup() {
        MatchGroup existingMatchGroup = matchGroupManager.getById(matchGroupFixture.getId());

        Assert.assertEquals(matchGroupFixture.getTeam1(), existingMatchGroup.getTeam1());
        Assert.assertEquals(matchGroupFixture.getTeam2(), existingMatchGroup.getTeam2());
        Assert.assertEquals(matchGroupFixture.getTeam3(), existingMatchGroup.getTeam3());
    }

    @Test
    public void testCreateMatchGroup() {
        Team team1 = MockTeamGenerator.generateTeam();
        Team team2 = MockTeamGenerator.generateTeam();
        Team team3 = MockTeamGenerator.generateTeam();
        List<Team> teams = Arrays.asList(team1, team2, team3);

        // Store users and teams in database.
        Session session = sessionManager.getSession();
        Transaction transaction = session.beginTransaction();
        for (Team team : teams) {
            session.save(team.getFirstPlayer());
            session.save(team.getSecondPlayer());
            session.save(team);
        }
        transaction.commit();
        session.close();

        MatchGroup newMatchGroup = matchGroupManager.create(teams);
        Assert.assertEquals(team1, newMatchGroup.getTeam1());
        Assert.assertEquals(team2, newMatchGroup.getTeam2());
        Assert.assertEquals(team3, newMatchGroup.getTeam3());
    }

    @Test
    public void testDeleteMatchGroup() {
        MatchGroup originalMatchGroup = matchGroupManager.deleteById(matchGroupFixture.getId());

        Session session = sessionManager.getSession();
        MatchGroup retrievedMatchGroup = session.get(MatchGroup.class, originalMatchGroup.getId());
        ScoreCard retrievedScoreCard = session.get(ScoreCard.class, originalMatchGroup.getScoreCard().getId());
        session.close();

        Assert.assertNotNull(originalMatchGroup);
        Assert.assertNotNull(originalMatchGroup.getScoreCard());
        // Ensure that the match group is deleted.
        Assert.assertNull(retrievedMatchGroup);
        Assert.assertNull(retrievedScoreCard);

        // Ensure that the individual teams are not deleted.
        Assert.assertNotNull(originalMatchGroup.getTeam1());
        Assert.assertNotNull(originalMatchGroup.getTeam2());
        Assert.assertNotNull(originalMatchGroup.getTeam3());
    }

	@Test
	public void testAddTeamToMatchGroup() {
		Team team1 = MockTeamGenerator.generateTeam();
		Team team2 = MockTeamGenerator.generateTeam();
		Team team3 = MockTeamGenerator.generateTeam();
		Team team4 = MockTeamGenerator.generateTeam();
		List<Team> teams1 = Arrays.asList(team1, team2, team3);
		List<Team> teams2 = Arrays.asList(team1, team2, team3, team4);

		// Store users and teams in database.
		Session session = sessionManager.getSession();
		Transaction transaction = session.beginTransaction();
		for (Team team : teams2) { //Uses teams2 because it contains all 4 teams, and so is able to add them all to the database
			session.save(team.getFirstPlayer());
			session.save(team.getSecondPlayer());
			session.save(team);
		}
		transaction.commit();
		session.close();

		MatchGroup matchGroup1 = matchGroupManager.createMatchGroup(teams1);
		MatchGroup matchGroup2 = new MatchGroup(teams2);
		matchGroup1 = matchGroupManager.addTeamToMatchGroup(matchGroup1.getId(), team4);
		Assert.assertEquals(matchGroup1.getTeams(), matchGroup2.getTeams());
	}

	@Test
	public void testRemoveTeamFromMatchGroup() {
		Team team1 = MockTeamGenerator.generateTeam();
		Team team2 = MockTeamGenerator.generateTeam();
		Team team3 = MockTeamGenerator.generateTeam();
		Team team4 = MockTeamGenerator.generateTeam();
		List<Team> teams1 = Arrays.asList(team1, team2, team3, team4);
		List<Team> teams2 = Arrays.asList(team1, team2, team3);

		// Store users and teams in database.
		Session session = sessionManager.getSession();
		Transaction transaction = session.beginTransaction();
		for (Team team : teams1) {
			session.save(team.getFirstPlayer());
			session.save(team.getSecondPlayer());
			session.save(team);
		}
		transaction.commit();
		session.close();

		MatchGroup matchGroup1 = matchGroupManager.createMatchGroup(teams1);
		MatchGroup matchGroup2 = new MatchGroup(teams2);
		matchGroup1 = matchGroupManager.removeTeamFromMatchGroup(matchGroup1.getId(), team4);
		Assert.assertEquals(matchGroup1.getTeams(), matchGroup2.getTeams());
	}

	@Test
	public void testTradeTeamsInMatchGroups() {
		Team team1 = MockTeamGenerator.generateTeam();
		Team team2 = MockTeamGenerator.generateTeam();
		Team team3 = MockTeamGenerator.generateTeam();
		Team team4 = MockTeamGenerator.generateTeam();
		Team team5 = MockTeamGenerator.generateTeam();
		Team team6 = MockTeamGenerator.generateTeam();
		Team team7 = MockTeamGenerator.generateTeam();
		List<Team> teams1 = Arrays.asList(team1, team2, team3, team4);
		List<Team> teams2 = Arrays.asList(team5, team6, team7);
		List<Team> teams3 = Arrays.asList(team1, team2, team3, team6);
		List<Team> teams4 = Arrays.asList(team5, team7, team4);

		// Store users and teams in database.
		Session session = sessionManager.getSession();
		Transaction transaction = session.beginTransaction();
		for (Team team : teams1) {
			session.save(team.getFirstPlayer());
			session.save(team.getSecondPlayer());
			session.save(team);
		}

		for (Team team : teams2) {
			session.save(team.getFirstPlayer());
			session.save(team.getSecondPlayer());
			session.save(team);
		}
		transaction.commit();
		session.close();

		MatchGroup matchGroup1 = matchGroupManager.createMatchGroup(teams1);
		MatchGroup matchGroup2 = matchGroupManager.createMatchGroup(teams2);
		List<MatchGroup> expectedResults = new ArrayList<>();
		expectedResults.add(new MatchGroup(teams3));
		expectedResults.add(new MatchGroup(teams4));
		List<MatchGroup> results = matchGroupManager.tradeTeamsInMatchGroups(matchGroup1.getId(), team4, matchGroup2.getId(), team6);
		Assert.assertEquals(results.get(0).getTeams(), expectedResults.get(0).getTeams());
		Assert.assertEquals(results.get(1).getTeams(), expectedResults.get(1).getTeams());
	}
}
