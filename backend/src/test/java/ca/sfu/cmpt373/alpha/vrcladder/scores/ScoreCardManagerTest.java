package ca.sfu.cmpt373.alpha.vrcladder.scores;

import ca.sfu.cmpt373.alpha.vrcladder.BaseTest;
import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.MatchGroup;
import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.MatchGroupManager;
import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
import ca.sfu.cmpt373.alpha.vrcladder.util.MockMatchGroupGenerator;
import ca.sfu.cmpt373.alpha.vrcladder.util.MockTeamGenerator;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ScoreCardManagerTest extends BaseTest {
    private ScoreCardManager scoreCardManager;
    private MatchGroup threeTeamMatchGroupFixture;
    private ScoreCard threeTeamScoreCardFixture;
    private MatchGroup fourTeamMatchGroupFixture;
    private ScoreCard fourTeamScoreCardFixture;

    @Before
    public void setUp() {
        scoreCardManager = new ScoreCardManager(sessionManager);

        threeTeamMatchGroupFixture = MockMatchGroupGenerator.generateThreeTeamMatchGroup();

        Session session = sessionManager.getSession();
        Transaction transaction = session.beginTransaction();
        List<Team> mockMatchGroupTeams = threeTeamMatchGroupFixture.getTeams();
        for (Team team : mockMatchGroupTeams) {
            session.save(team.getFirstPlayer());
            session.save(team.getSecondPlayer());
            session.save(team);
        }
        session.save(threeTeamMatchGroupFixture);
        threeTeamScoreCardFixture = threeTeamMatchGroupFixture.getScoreCard();
        session.save(threeTeamScoreCardFixture);

        fourTeamMatchGroupFixture = MockMatchGroupGenerator.generateFourTeamMatchGroup();

        List<Team> mockFourMatchGroupTeams = fourTeamMatchGroupFixture.getTeams();
        for (Team team : mockFourMatchGroupTeams) {
            session.save(team.getFirstPlayer());
            session.save(team.getSecondPlayer());
            session.save(team);
        }
        session.save(fourTeamMatchGroupFixture);
        fourTeamScoreCardFixture = fourTeamMatchGroupFixture.getScoreCard();
        session.save(fourTeamScoreCardFixture);

        transaction.commit();
        session.close();
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
        ScoreCard newScoreCard = matchGroupManager.createMatchGroup(teams).getScoreCard();
        session = sessionManager.getSession();
        ScoreCard retrievedScoreCard = session.get(ScoreCard.class, newScoreCard.getId());
        session.close();

        Assert.assertEquals(newScoreCard, retrievedScoreCard);
    }

    @Test
    public void testUpdateThreeTeams() {
        List<Team> rankedTeams = new ArrayList<>();
        int thirdTeamIndex = 2;
        for (int i = thirdTeamIndex; i >= 0; i--) {
            rankedTeams.add(threeTeamMatchGroupFixture.getTeams().get(i));
        }
        threeTeamScoreCardFixture.setRankedTeams(rankedTeams);
        testUpdate(threeTeamScoreCardFixture);
    }

    @Test public void testUpdateFourTeams() {
        List<Team> rankedTeams = new ArrayList<>();
        int fourthTeamIndex = 3;
        for (int i = fourthTeamIndex; i >= 0; i--) {
            rankedTeams.add(fourTeamMatchGroupFixture.getTeams().get(i));
        }
        fourTeamScoreCardFixture.setRankedTeams(rankedTeams);
        testUpdate(fourTeamScoreCardFixture);
    }

    private void testUpdate(ScoreCard scoreCard) {
        scoreCardManager.update(scoreCard);
        Session session = sessionManager.getSession();
        ScoreCard retrievedScoreCard = session.get(ScoreCard.class, scoreCard.getId());
        session.close();

        Assert.assertEquals(scoreCard.getRankedTeams(), retrievedScoreCard.getRankedTeams());
    }

    @Test
    public void testDeleteThreeTeams() {
        testDelete(threeTeamScoreCardFixture);
    }

    @Test
    public void testDeleteThreeTeamsById() {
        testDeleteById(threeTeamScoreCardFixture);
    }

    @Test
    public void testDeleteFourTeams() {
        testDelete(fourTeamScoreCardFixture);
    }

    @Test
    public void testDeleteFourTeamsById() {
        testDeleteById(fourTeamScoreCardFixture);
    }

    private void testDelete(ScoreCard scoreCard) {
        ScoreCard deletedScoreCard = scoreCardManager.delete(scoreCard);
        Assert.assertNotNull(deletedScoreCard);

        assertDeleted(scoreCard);
    }

    private void testDeleteById(ScoreCard scoreCard) {
        ScoreCard deletedScoreCard = scoreCardManager.deleteById(scoreCard.getId());
        Assert.assertNotNull(deletedScoreCard);

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
        testGet(threeTeamScoreCardFixture);
    }

    @Test
    public void testGetFourTeams() {
        testGet(fourTeamScoreCardFixture);
    }

    private void testGet(ScoreCard scoreCard) {
        ScoreCard retrievedScoreCard = scoreCardManager.getById(scoreCard.getId());
        Assert.assertEquals(retrievedScoreCard, scoreCard);
    }
}
