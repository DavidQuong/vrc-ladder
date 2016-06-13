package ca.sfu.cmpt373.alpha.vrcladder.scores;

import ca.sfu.cmpt373.alpha.vrcladder.BaseTest;
import ca.sfu.cmpt373.alpha.vrcladder.game.score.ScoreCard;
import ca.sfu.cmpt373.alpha.vrcladder.game.score.ScoreCardManager;
import ca.sfu.cmpt373.alpha.vrcladder.game.score.ThreeTeamScoreCard;
import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.MatchGroup;
import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
import ca.sfu.cmpt373.alpha.vrcladder.util.MockMatchGroupGenerator;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class ScoreCardManagerThreeTeamTest extends BaseTest {
    private ScoreCardManager scoreCardManager;
    private MatchGroup matchGroupFixture;
    private ScoreCard scoreCardFixture;

    @Before
    public void setUp() {
        scoreCardManager = new ScoreCardManager(sessionManager);
        matchGroupFixture = MockMatchGroupGenerator.generateThreeTeamMatchGroup();
        scoreCardFixture = new ThreeTeamScoreCard(matchGroupFixture);

        Session session = sessionManager.getSession();
        Transaction transaction = session.beginTransaction();
        List<Team> mockMatchGroupTeams = matchGroupFixture.getTeams();
        for (Team team : mockMatchGroupTeams) {
            session.save(team.getFirstPlayer());
            session.save(team.getSecondPlayer());
            session.save(team);
        }
        session.save(matchGroupFixture);
        session.save(scoreCardFixture);

        transaction.commit();
        session.close();
    }

    @Test
    public void testCreate() {
        ScoreCard newScoreCard = scoreCardManager.create(matchGroupFixture);
        Session session = sessionManager.getSession();
        ScoreCard retrievedScoreCard = session.get(ScoreCard.class, newScoreCard.getId());
        session.close();

        Assert.assertEquals(newScoreCard, retrievedScoreCard);
    }

    @Test
    public void testUpdateScoreCard() {
        scoreCardFixture.recordRoundWinner(matchGroupFixture.getTeam1());
        scoreCardFixture.recordRoundWinner(matchGroupFixture.getTeam1());
        scoreCardFixture.recordRoundWinner(matchGroupFixture.getTeam3());

        scoreCardManager.update(scoreCardFixture);
        Session session = sessionManager.getSession();
        ScoreCard retrievedScoreCard = session.get(ScoreCard.class, scoreCardFixture.getId());
        session.close();

        Assert.assertEquals(scoreCardFixture.getRankedResults(), retrievedScoreCard.getRankedResults());
    }

    @Test
    public void testDeleteScoreCard() {
        ScoreCard deletedScoreCard = scoreCardManager.deleteById(scoreCardFixture.getId());
        Assert.assertNotNull(deletedScoreCard);

        Session session = sessionManager.getSession();
        ScoreCard retrievedScoreCard = session.get(ScoreCard.class, scoreCardFixture.getId());
        session.close();

        Assert.assertNull(retrievedScoreCard);
    }

    @Test
    public void testGetScoreCard() {
        ScoreCard retrievedScoreCard = scoreCardManager.getById(scoreCardFixture.getId());
        Assert.assertEquals(retrievedScoreCard, scoreCardFixture);
    }

}
