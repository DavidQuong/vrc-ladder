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

public class ScoreCardManagerTest extends BaseTest {
    private ScoreCardManager scoreCardManager;
    private MatchGroup matchGroupFixture;

    @Before
    public void setUp() {
        scoreCardManager = new ScoreCardManager(sessionManager);
        matchGroupFixture = MockMatchGroupGenerator.generateThreeTeamMatchGroup();

        Session session = sessionManager.getSession();
        Transaction transaction = session.beginTransaction();
        List<Team> mockMatchGroupTeams = matchGroupFixture.getTeams();
        for (Team team : mockMatchGroupTeams) {
            session.save(team.getFirstPlayer());
            session.save(team.getSecondPlayer());
            session.save(team);
        }
        session.save(matchGroupFixture);
        transaction.commit();
        session.close();
    }

    @Test
    public void testCreate() {
        ScoreCard newScoreCard = scoreCardManager.create(matchGroupFixture);
        Session session = sessionManager.getSession();
        ScoreCard retrievedScoreCard = session.get(ThreeTeamScoreCard.class, newScoreCard.getId());
        session.close();

        Assert.assertEquals(newScoreCard, retrievedScoreCard);
    }

    @Test
    public void updateScoreCard() {
        //TODO: fix this test case
        ScoreCard newScoreCard = scoreCardManager.create(matchGroupFixture);

        newScoreCard.recordRoundWinner(matchGroupFixture.getTeam1());
        newScoreCard.recordRoundWinner(matchGroupFixture.getTeam1());
        newScoreCard.recordRoundWinner(matchGroupFixture.getTeam3());

        scoreCardManager.update(newScoreCard);
        Session session = sessionManager.getSession();
        ScoreCard retrievedScoreCard = session.get(ThreeTeamScoreCard.class, newScoreCard.getId());
        session.close();

        Assert.assertEquals(newScoreCard.getRankedResults(), retrievedScoreCard.getRankedResults());
    }

}
