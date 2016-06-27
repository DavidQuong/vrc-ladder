package ca.sfu.cmpt373.alpha.vrcladder.scores;

import ca.sfu.cmpt373.alpha.vrcladder.BaseTest;
import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.MatchGroup;
import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.MatchGroupManager;
import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.PlayTime;
import ca.sfu.cmpt373.alpha.vrcladder.util.MockMatchGroupGenerator;
import ca.sfu.cmpt373.alpha.vrcladder.util.MockTeamGenerator;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * A class to test the database integration of @class {@link ScoreCard}
 * Normally, there would be a DatabaseManager class associated with data in the database,
 * but since ScoreCards should only ever be created when a MatchGroup is created,
 * ScoreCards are created through the MatchGroupManager
 */
public class ScoreCardDatabaseTest extends BaseTest {

    private MatchGroupManager matchGroupManager;
    private MatchGroup threeTeamMatchGroupFixture;
    private MatchGroup fourTeamMatchGroupFixture;

    @Before
    public void setUp() {
        matchGroupManager = new MatchGroupManager(sessionManager);

        threeTeamMatchGroupFixture = MockMatchGroupGenerator.generateThreeTeamMatchGroup();

        Session session = sessionManager.getSession();
        Transaction transaction = session.beginTransaction();
        List<Team> threeTeamMatchGroupTeams = threeTeamMatchGroupFixture.getTeams();
        saveTeams(threeTeamMatchGroupTeams, session);
        session.save(threeTeamMatchGroupFixture);
        session.save(threeTeamMatchGroupFixture.getScoreCard());

        fourTeamMatchGroupFixture = MockMatchGroupGenerator.generateFourTeamMatchGroup();

        List<Team> fourTeamMatchGroupTeams = fourTeamMatchGroupFixture.getTeams();
        saveTeams(fourTeamMatchGroupTeams, session);
        session.save(fourTeamMatchGroupFixture);
        session.save(fourTeamMatchGroupFixture.getScoreCard());

        transaction.commit();
        session.close();
    }

    private void saveTeams(List<Team> teams, Session session) {
        for (Team team : teams) {
            team.getAttendanceCard().setPreferredPlayTime(PlayTime.TIME_SLOT_A);
            session.save(team.getFirstPlayer());
            session.save(team.getSecondPlayer());
            session.save(team);
        }
    }

    @Test
    public void testCreateThreeTeams() {
        int numTeams = 3;
        testCreate(numTeams);
    }

    @Test
    public void testCreateFourTeams() {
        int numTeams = 4;
        testCreate(numTeams);
    }

    private void testCreate(int numTeams) {
        List<Team> teams = new ArrayList<>();
        Session session = sessionManager.getSession();
        Transaction transaction = session.beginTransaction();
        for (int i = 0; i < numTeams; i++) {
            Team team = MockTeamGenerator.generateTeam();
            session.save(team.getFirstPlayer());
            session.save(team.getSecondPlayer());
            session.save(team);
            teams.add(team);
        }
        transaction.commit();
        session.close();

        MatchGroupManager matchGroupManager = new MatchGroupManager(sessionManager);
        ScoreCard newScoreCard = matchGroupManager.create(teams).getScoreCard();
        session = sessionManager.getSession();
        ScoreCard retrievedScoreCard = session.get(ScoreCard.class, newScoreCard.getId());
        session.close();

        Assert.assertEquals(newScoreCard, retrievedScoreCard);
    }

    @Test
    public void testUpdateThreeTeams() {
        testUpdate(threeTeamMatchGroupFixture);
    }

    @Test public void testUpdateFourTeams() {
        testUpdate(fourTeamMatchGroupFixture);
    }

    private void testUpdate(MatchGroup matchGroup) {
        List<Team> rankedTeams = new ArrayList<>();
        for (int i = matchGroup.getTeamCount() - 1; i >= 0; i--) {
            rankedTeams.add(matchGroup.getTeams().get(i));
        }
        ScoreCard scoreCard = matchGroup.getScoreCard();
        scoreCard.setRankedTeams(rankedTeams);

        matchGroupManager.updateScoreCard(matchGroup);

        Session session = sessionManager.getSession();
        ScoreCard retrievedScoreCard = session.get(ScoreCard.class, matchGroup.getScoreCard().getId());
        session.close();

        Assert.assertEquals(matchGroup.getScoreCard().getRankedTeams(), retrievedScoreCard.getRankedTeams());
    }

    @Test
    public void testDeleteThreeTeams() {
        testDelete(threeTeamMatchGroupFixture);
    }

    @Test
    public void testDeleteThreeTeamsById() {
        testDeleteById(threeTeamMatchGroupFixture);
    }

    @Test
    public void testDeleteFourTeams() {
        testDelete(fourTeamMatchGroupFixture);
    }

    @Test
    public void testDeleteFourTeamsById() {
        testDeleteById(fourTeamMatchGroupFixture);
    }

    private void testDelete(MatchGroup matchGroup) {
        ScoreCard scoreCard = matchGroup.getScoreCard();
        matchGroupManager.delete(matchGroup);
        Assert.assertNotNull(scoreCard);
        assertDeleted(scoreCard);
    }

    private void testDeleteById(MatchGroup matchGroup) {
        ScoreCard scoreCard = matchGroup.getScoreCard();
        matchGroupManager.deleteById(matchGroup.getId());
        Assert.assertNotNull(scoreCard);
        assertDeleted(scoreCard);
    }

    private void assertDeleted(ScoreCard scoreCard) {
        Session session = sessionManager.getSession();
        ScoreCard retrievedScoreCard = session.get(ScoreCard.class, scoreCard.getId());
        session.close();

        Assert.assertNull(retrievedScoreCard);
    }

    @Test
    public void testGetThreeTeams() {
        testGet(threeTeamMatchGroupFixture);
    }

    @Test
    public void testGetFourTeams() {
        testGet(fourTeamMatchGroupFixture);
    }

    private void testGet(MatchGroup matchGroup) {
        ScoreCard retrievedScoreCard = matchGroupManager.getById(matchGroup.getId()).getScoreCard();
        Assert.assertEquals(retrievedScoreCard, matchGroup.getScoreCard());
    }
}
