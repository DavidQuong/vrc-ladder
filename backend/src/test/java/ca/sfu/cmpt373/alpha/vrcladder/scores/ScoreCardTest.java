package ca.sfu.cmpt373.alpha.vrcladder.scores;

import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.MatchGroup;
import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
import ca.sfu.cmpt373.alpha.vrcladder.util.MockMatchGroupGenerator;
import ca.sfu.cmpt373.alpha.vrcladder.util.MockTeamGenerator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ScoreCardTest {
    private MatchGroup threeTeamMockMatchGroup;
    private MatchGroup fourTeamMockMatchGroup;

    @Before
    public void setUp() {
        threeTeamMockMatchGroup = MockMatchGroupGenerator.generateThreeTeamMatchGroup();
        fourTeamMockMatchGroup = MockMatchGroupGenerator.generateFourTeamMatchGroup();
    }

    @Test
    public void testSetValidResultsFourTeams() {
        ScoreCard scoreCard = new ScoreCard(fourTeamMockMatchGroup);
        List<Team> rankedResults = new ArrayList<>();
        for (int i = 3; i >= 0; i--) {
            rankedResults.add(fourTeamMockMatchGroup.getTeams().get(i));
        }
        scoreCard.setRankedTeams(rankedResults);
        Assert.assertEquals(rankedResults, scoreCard.getRankedTeams());
    }

    @Test
    public void testSetValidResultsThreeTeams() {
        ScoreCard scoreCard = new ScoreCard(threeTeamMockMatchGroup);
        List<Team> rankedResults = new ArrayList<>();
        for (int i = 2; i >= 0; i--) {
            rankedResults.add(threeTeamMockMatchGroup.getTeams().get(i));
        }
        scoreCard.setRankedTeams(rankedResults);
        Assert.assertEquals(rankedResults, scoreCard.getRankedTeams());
    }

    @Test (expected = IllegalStateException.class)
    public void testSetIllegalResults() {
        ScoreCard scoreCard = new ScoreCard(fourTeamMockMatchGroup);
        List<Team> rankedResults = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            rankedResults.add(MockTeamGenerator.generateTeam());
        }
        scoreCard.setRankedTeams(rankedResults);
    }

    @Test (expected = IllegalStateException.class)
    public void testSetTooManyResults() {
        ScoreCard scoreCard = new ScoreCard(threeTeamMockMatchGroup);
        List<Team> rankedResults = new ArrayList<>();
        for (int i = 2; i >= 0; i--) {
            rankedResults.add(threeTeamMockMatchGroup.getTeams().get(i));
        }

        //add one team twice by an accident!
        rankedResults.add(threeTeamMockMatchGroup.getTeams().get(0));

        scoreCard.setRankedTeams(rankedResults);
    }
}
