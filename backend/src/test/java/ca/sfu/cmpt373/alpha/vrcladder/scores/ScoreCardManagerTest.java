package ca.sfu.cmpt373.alpha.vrcladder.scores;

import ca.sfu.cmpt373.alpha.vrcladder.BaseTest;
import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.MatchGroup;
import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
import ca.sfu.cmpt373.alpha.vrcladder.util.MockMatchGroupGenerator;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

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
        threeTeamScoreCardFixture = new ThreeTeamScoreCard(threeTeamMatchGroupFixture);

        Session session = sessionManager.getSession();
        Transaction transaction = session.beginTransaction();
        List<Team> mockMatchGroupTeams = threeTeamMatchGroupFixture.getTeams();
        for (Team team : mockMatchGroupTeams) {
            session.save(team.getFirstPlayer());
            session.save(team.getSecondPlayer());
            session.save(team);
        }
        session.save(threeTeamMatchGroupFixture);
        session.save(threeTeamScoreCardFixture);

        fourTeamMatchGroupFixture = MockMatchGroupGenerator.generateFourTeamMatchGroup();
        fourTeamScoreCardFixture = new FourTeamScoreCard(fourTeamMatchGroupFixture);

        List<Team> mockFourMatchGroupTeams = fourTeamMatchGroupFixture.getTeams();
        for (Team team : mockFourMatchGroupTeams) {
            session.save(team.getFirstPlayer());
            session.save(team.getSecondPlayer());
            session.save(team);
        }
        session.save(fourTeamMatchGroupFixture);
        session.save(fourTeamScoreCardFixture);

        transaction.commit();
        session.close();
    }

    @Test
    public void testCreateThreeTeams() {
        ScoreCard newScoreCard = scoreCardManager.create(threeTeamMatchGroupFixture);
        testCreate(newScoreCard);
    }

    @Test
    public void testCreateFourTeams() {
        ScoreCard newScoreCard = scoreCardManager.create(fourTeamMatchGroupFixture);
        testCreate(newScoreCard);
    }

    private void testCreate(ScoreCard newScoreCard) {
        Session session = sessionManager.getSession();
        ScoreCard retrievedScoreCard = session.get(ScoreCard.class, newScoreCard.getId());
        session.close();

        Assert.assertEquals(newScoreCard, retrievedScoreCard);
    }

    @Test
    public void testUpdateThreeTeams() {
        threeTeamScoreCardFixture.recordRoundWinner(threeTeamMatchGroupFixture.getTeam1());
        threeTeamScoreCardFixture.recordRoundWinner(threeTeamMatchGroupFixture.getTeam1());
        threeTeamScoreCardFixture.recordRoundWinner(threeTeamMatchGroupFixture.getTeam3());
        testUpdate(threeTeamScoreCardFixture);
    }

    @Test public void testUpdateFourTeams() {
        fourTeamScoreCardFixture.recordRoundWinner(fourTeamMatchGroupFixture.getTeam1());
        fourTeamScoreCardFixture.recordRoundWinner(fourTeamMatchGroupFixture.getTeam2());
        fourTeamScoreCardFixture.recordRoundWinner(fourTeamMatchGroupFixture.getTeam1());
        fourTeamScoreCardFixture.recordRoundWinner(fourTeamMatchGroupFixture.getTeam3());
        testUpdate(fourTeamScoreCardFixture);
    }

    private void testUpdate(ScoreCard scoreCard) {
        scoreCardManager.update(scoreCard);
        Session session = sessionManager.getSession();
        ScoreCard retrievedScoreCard = session.get(ScoreCard.class, scoreCard.getId());
        session.close();

        Assert.assertEquals(scoreCard.getRankedResults(), retrievedScoreCard.getRankedResults());
    }

    @Test
    public void testDeleteThreeTeams() {
        testDelete(threeTeamScoreCardFixture);
    }

    @Test
    public void testDeleteFourTeams() {
        testDelete(fourTeamScoreCardFixture);
    }

    private void testDelete(ScoreCard scoreCard) {
        ScoreCard deletedScoreCard = scoreCardManager.deleteById(scoreCard.getId());
        Assert.assertNotNull(deletedScoreCard);

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
