package ca.sfu.cmpt373.alpha.vrcladder.scores;

import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.MatchGroup;
import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.PlayTime;
import ca.sfu.cmpt373.alpha.vrcladder.util.MockMatchGroupGenerator;
import ca.sfu.cmpt373.alpha.vrcladder.util.MockTeamGenerator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ScoreCardTest {
    private static final int LAST_INDEX_FOUR_TEAMS = 3;
    private static final int LAST_INDEX_THREE_TEAMS = 2;

    private MatchGroup threeTeamMockMatchGroup;
    private MatchGroup fourTeamMockMatchGroup;

    @Before
    public void setUp() {
        threeTeamMockMatchGroup = MockMatchGroupGenerator.generateThreeTeamMatchGroup();
        fourTeamMockMatchGroup = MockMatchGroupGenerator.generateFourTeamMatchGroup();
        setAttending(threeTeamMockMatchGroup.getTeams());
        setAttending(fourTeamMockMatchGroup.getTeams());
    }

    private void setAttending(List<Team> teams) {
        for (Team team : teams) {
            team.getAttendanceCard().setPreferredPlayTime(PlayTime.TIME_SLOT_A);
        }
    }

    @Test
    public void testSetValidResultsFourTeams() {
        testSetValidResults(fourTeamMockMatchGroup);
    }

    @Test
    public void testSetValidResultsThreeTeams() {
        testSetValidResults(threeTeamMockMatchGroup);
    }

    private void testSetValidResults(MatchGroup matchGroup) {
        ScoreCard scoreCard = new ScoreCard(matchGroup);
        List<Team> rankedResults = new ArrayList<>();
        for (int i = matchGroup.getTeamCount() - 1; i >= 0; i--) {
            rankedResults.add(matchGroup.getTeams().get(i));
        }
        scoreCard.setRankedTeams(rankedResults);
        Assert.assertEquals(rankedResults, scoreCard.getRankedTeams());
    }

    @Test (expected = IllegalStateException.class)
    public void testSetIllegalResults() {
        ScoreCard scoreCard = new ScoreCard(fourTeamMockMatchGroup);
        List<Team> rankedResults = new ArrayList<>();
        for (int i = 0; i < LAST_INDEX_FOUR_TEAMS; i++) {
            rankedResults.add(MockTeamGenerator.generateTeam());
        }
        scoreCard.setRankedTeams(rankedResults);
    }

    @Test (expected = IllegalStateException.class)
    public void testSetTooManyResults() {
        ScoreCard scoreCard = new ScoreCard(threeTeamMockMatchGroup);
        List<Team> rankedResults = new ArrayList<>();
        for (int i = LAST_INDEX_THREE_TEAMS; i >= 0; i--) {
            rankedResults.add(threeTeamMockMatchGroup.getTeams().get(i));
        }

        //add one team twice by an accident!
        rankedResults.add(threeTeamMockMatchGroup.getTeams().get(0));

        scoreCard.setRankedTeams(rankedResults);
    }
}
