package ca.sfu.cmpt373.alpha.vrcladder.scores;

import ca.sfu.cmpt373.alpha.vrcladder.game.score.ThreeTeamScoreCard;
import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.MatchGroup;
import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
import ca.sfu.cmpt373.alpha.vrcladder.util.MockMatchGroupGenerator;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ThreeTeamScoreCardTest {
//        a. 1 VS 2
//        b. Winner (a) VS 3
//        c. Loser (a) VS 3

    //TODO: add more test cases!
    @Test
    public void test() {
        MatchGroup matchGroup = MockMatchGroupGenerator.generateThreeTeamMatchGroup();
        ThreeTeamScoreCard scoreCard = new ThreeTeamScoreCard(matchGroup);
        scoreCard.recordRoundWinner(matchGroup.getTeam1());
        scoreCard.recordRoundWinner(matchGroup.getTeam1());
        scoreCard.recordRoundWinner(matchGroup.getTeam3());

        List<Team> expectedResults = new ArrayList<>();
        expectedResults.add(matchGroup.getTeam1());
        expectedResults.add(matchGroup.getTeam3());
        expectedResults.add(matchGroup.getTeam2());

        Assert.assertEquals(expectedResults, scoreCard.getRankedResults());
    }
}
