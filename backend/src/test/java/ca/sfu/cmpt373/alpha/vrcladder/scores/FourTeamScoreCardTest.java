package ca.sfu.cmpt373.alpha.vrcladder.scores;

import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.MatchGroup;
import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
import ca.sfu.cmpt373.alpha.vrcladder.util.MockMatchGroupGenerator;
import ca.sfu.cmpt373.alpha.vrcladder.util.MockTeamGenerator;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FourTeamScoreCardTest {
    //TODO: add test for getTeamWinsAndLosses()

    @Test
    public void testGeneralCases() {
        MatchGroup matchGroup = MockMatchGroupGenerator.generateFourTeamMatchGroup();

        // map input (round winners) to expected output (ranked team orders)
        Map<List<Integer>, List<Integer>> testData = new HashMap<>();
        testData.put(Arrays.asList(1, 2, 1, 3), Arrays.asList(1, 2, 3, 4));
        testData.put(Arrays.asList(1, 2, 1, 4), Arrays.asList(1, 2, 4, 3));
        testData.put(Arrays.asList(4, 3, 3, 1), Arrays.asList(3, 4, 1, 2));
        testData.put(Arrays.asList(4, 3, 3, 2), Arrays.asList(3, 4, 2, 1));
        testData.put(Arrays.asList(4, 3, 4, 2), Arrays.asList(4, 3, 2, 1));

        for (List<Integer> input : testData.keySet()) {
            FourTeamScoreCard scoreCard = new FourTeamScoreCard(matchGroup);
            for (Integer roundWinner : input) {
                scoreCard.recordRoundWinner(matchGroup.getTeams().get(roundWinner - 1));
            }
            List<Team> expectedRankings = new ArrayList<>();
            for (Integer expectedTeamRank : testData.get(input)) {
                expectedRankings.add(matchGroup.getTeams().get(expectedTeamRank - 1));
            }
            Assert.assertEquals("Error on test case input:" + input + " output: " + testData.get(input),
                    scoreCard.getRankedResults(),
                    expectedRankings);
        }
    }

    @Test (expected = IllegalStateException.class)
    public void testRecordForeignWinner() {
        Team team = MockTeamGenerator.generateTeam();
        ScoreCard scoreCard = new FourTeamScoreCard(MockMatchGroupGenerator.generateFourTeamMatchGroup());
        scoreCard.recordRoundWinner(team);
    }

    @Test (expected = IllegalStateException.class)
    public void testRecordNotPlayingTeamWinner() {
        //tests setting a team as a round winner that shouldn't be playing in the round
        MatchGroup matchGroup = MockMatchGroupGenerator.generateFourTeamMatchGroup();
        FourTeamScoreCard scoreCard = new FourTeamScoreCard(matchGroup);
        scoreCard.recordRoundWinner(matchGroup.getTeam3());
    }

    @Test (expected = IllegalStateException.class)
    public void testGetPrematureResults() {
        MatchGroup matchGroup = MockMatchGroupGenerator.generateFourTeamMatchGroup();
        ScoreCard scoreCard = new FourTeamScoreCard(matchGroup);
        scoreCard.getRankedResults();
    }
}
