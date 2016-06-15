package ca.sfu.cmpt373.alpha.vrcladder.matchmaking;

import ca.sfu.cmpt373.alpha.vrcladder.BaseTest;
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

// TODO - Refactor to use Mock<Class>Generator classes
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
        MatchGroup existingMatchGroup = matchGroupManager.getMatchGroup(matchGroupFixture.getId());

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

        MatchGroup newMatchGroup = matchGroupManager.createMatchGroup(teams);
        Assert.assertEquals(team1, newMatchGroup.getTeam1());
        Assert.assertEquals(team2, newMatchGroup.getTeam2());
        Assert.assertEquals(team3, newMatchGroup.getTeam3());
    }

    @Test
    public void testDeleteMatchGroup() {
        MatchGroup originalMatchGroup = matchGroupManager.deleteMatchGroup(matchGroupFixture.getId());

        Session session = sessionManager.getSession();
        MatchGroup matchGroup = session.get(MatchGroup.class, originalMatchGroup.getId());
        session.close();

        Assert.assertNotNull(originalMatchGroup);
        // Ensure that the match group is deleted.
        Assert.assertNull(matchGroup);

        // Ensure that the individual teams are not deleted.
        Assert.assertNotNull(originalMatchGroup.getTeam1());
        Assert.assertNotNull(originalMatchGroup.getTeam2());
        Assert.assertNotNull(originalMatchGroup.getTeam3());
    }

}
