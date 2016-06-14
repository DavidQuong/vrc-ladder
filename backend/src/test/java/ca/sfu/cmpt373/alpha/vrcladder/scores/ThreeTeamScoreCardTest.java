package ca.sfu.cmpt373.alpha.vrcladder.scores;

import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.MatchGroup;
import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
import ca.sfu.cmpt373.alpha.vrcladder.util.MockMatchGroupGenerator;
import ca.sfu.cmpt373.alpha.vrcladder.util.MockTeamGenerator;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThreeTeamScoreCardTest {

    @Test
    public void testGeneralCases() {
        MatchGroup matchGroup = MockMatchGroupGenerator.generateThreeTeamMatchGroup();

        //map input (round winners) to expected output (ranked team orders)
        Map<List<Integer>, List<Integer>> testData = new HashMap<>();
        testData.put(Arrays.asList(1, 1, 3), Arrays.asList(1, 3, 2));
        testData.put(Arrays.asList(1, 3, 2), Arrays.asList(1, 2, 3));
        testData.put(Arrays.asList(2, 2, 3), Arrays.asList(2, 3, 1));
        testData.put(Arrays.asList(2, 3, 3), Arrays.asList(3, 2, 1));
        testData.put(Arrays.asList(2, 2, 1), Arrays.asList(2, 1, 3));

        for (List<Integer> input : testData.keySet()) {
            ThreeTeamScoreCard scoreCard = new ThreeTeamScoreCard(matchGroup);
            for (Integer roundWinner : input) {
                scoreCard.recordRoundWinner(matchGroup.getTeams().get(roundWinner - 1));
            }
            List<Team> expectedRankings = new ArrayList<>();
            for (Integer expectedTeamRank : testData.get(input)) {
                expectedRankings.add(matchGroup.getTeams().get(expectedTeamRank - 1));
            }
            Assert.assertEquals(scoreCard.getRankedResults(), expectedRankings);
        }
    }

    @Test (expected = IllegalStateException.class)
    public void testRecordForeignTeamWinner() {
        //tests trying to set the winner of a round as a team that does not belong in the match group
        MatchGroup matchGroup = MockMatchGroupGenerator.generateThreeTeamMatchGroup();
        ThreeTeamScoreCard scoreCard = new ThreeTeamScoreCard(matchGroup);
        scoreCard.recordRoundWinner(MockTeamGenerator.generateTeam());
    }

    @Test (expected = IllegalStateException.class)
    public void testRecordNotPlayingTeamWinner() {
        //tests setting a team as a round winner that shouldn't be playing in the round
        MatchGroup matchGroup = MockMatchGroupGenerator.generateThreeTeamMatchGroup();
        ThreeTeamScoreCard scoreCard = new ThreeTeamScoreCard(matchGroup);
        scoreCard.recordRoundWinner(matchGroup.getTeam3());
    }

    @Test
    public void testGetWinsLosses() {
        MatchGroup matchGroup = MockMatchGroupGenerator.generateThreeTeamMatchGroup();
        Map<List<Integer>, Pair<List<Integer>, List<Integer>>> testData = new HashMap<>();
        //map input to expected output (a pair of wins/losses for teams 1, 2, 3)
        testData.put(Arrays.asList(1, 1, 3), new ImmutablePair<>(Arrays.asList(2, 0, 1), Arrays.asList(0, 2, 1)));
        testData.put(Arrays.asList(1, 3, 2), new ImmutablePair<>(Arrays.asList(1, 1, 1), Arrays.asList(1, 1, 1)));
        testData.put(Arrays.asList(2, 2, 3), new ImmutablePair<>(Arrays.asList(0, 2, 1), Arrays.asList(2, 0, 1)));
        testData.put(Arrays.asList(2, 3, 3), new ImmutablePair<>(Arrays.asList(0, 1, 2), Arrays.asList(2, 1, 0)));
        testData.put(Arrays.asList(2, 2, 1), new ImmutablePair<>(Arrays.asList(1, 2, 0), Arrays.asList(1, 0, 2)));

        for (List<Integer> input : testData.keySet()) {
            ThreeTeamScoreCard scoreCard = new ThreeTeamScoreCard(matchGroup);
            for (Integer roundWinner : input) {
                scoreCard.recordRoundWinner(matchGroup.getTeams().get(roundWinner - 1));
            }
            Pair<List<Integer>, List<Integer>> expectedResults = testData.get(input);
            for (int i = 0; i < expectedResults.getLeft().size(); i++) {
                WinLossPair winLossResultsPair = new WinLossPair(
                        expectedResults.getLeft().get(i),
                        expectedResults.getRight().get(i));
                Assert.assertEquals(winLossResultsPair, scoreCard.getTeamWinsAndLosses(matchGroup.getTeams().get(i)));
            }
        }
    }

    @Test (expected = IllegalStateException.class)
    public void testGetPrematureResults() {
        MatchGroup matchGroup = MockMatchGroupGenerator.generateThreeTeamMatchGroup();
        ScoreCard scoreCard = new ThreeTeamScoreCard(matchGroup);
        scoreCard.getRankedResults();
    }


}
