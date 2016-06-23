package ca.sfu.cmpt373.alpha.vrcladder.matchmaking.logic;

import ca.sfu.cmpt373.alpha.vrcladder.exceptions.MatchMakingException;
import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.MatchGroup;
import ca.sfu.cmpt373.alpha.vrcladder.persistance.MockDatabase;
import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class MatchGroupGeneratorTest {
    private static final int IMPOSSIBLE_TEAM_COUNT = 5;
    //could be any number, but we've got to stop testing somewhere
    private static final int MAX_TEST_TEAM_COUNT = 49;

    @Test
    public void testGroupSizes() {
        List<MatchGroup> matchGroups = MatchGroupGenerator.generateMatchGroupings(MockDatabase.getRankedLadderTeams(MAX_TEST_TEAM_COUNT));
        for (MatchGroup matchGroup : matchGroups) {
            List<Team> teams = matchGroup.getTeams();
            Assert.assertTrue(teams.size() == MatchGroup.MAX_NUM_TEAMS || teams.size() == MatchGroup.MIN_NUM_TEAMS);
        }
    }

    /**
     * tests that only groups of the minimum permissible group members
     * are generated when the number of players can be evenly divided
     */
    @Test
    public void testIdealGroupSize() {
        //there should only be groups of MatchGroup.MIN_NUM_TEAMS for teamCounts that are evenly divisible by MatchGroup.MIN_NUM_TEAMS
        int idealGroupSize = MatchGroup.MIN_NUM_TEAMS;
        //arbitrary number of test runs, could be any multiple of MIN_NUM_TEAMS
        for (int teamCount = idealGroupSize; teamCount < MAX_TEST_TEAM_COUNT; teamCount += idealGroupSize) {
            Assert.assertTrue(teamCount % MatchGroup.MIN_NUM_TEAMS == 0);
            List<MatchGroup> matchGroups = MatchGroupGenerator.generateMatchGroupings(MockDatabase.getRankedLadderTeams(teamCount));
            for (MatchGroup matchGroup : matchGroups) {
                Assert.assertTrue(matchGroup.getTeams().size() == idealGroupSize);
            }
        }
    }

    @Test(expected = MatchMakingException.class)
    public void testUndesiredGroupSize() {
        //this could be any number, but we've got to stop testing somewhere!
        //start after corner cases
        for (int teamCount = IMPOSSIBLE_TEAM_COUNT + 1; teamCount < MAX_TEST_TEAM_COUNT; teamCount++) {
            boolean isUndesiredGroupSize = teamCount % MatchGroup.MIN_NUM_TEAMS != 0;
            if (isUndesiredGroupSize) {
                int undesiredGroupCount = 0;
                List<MatchGroup> matchGroups = MatchGroupGenerator.generateMatchGroupings(MockDatabase.getRankedLadderTeams(teamCount));
                for (MatchGroup matchGroup : matchGroups) {
                    int matchGroupSize = matchGroup.getTeams().size();
                    boolean isNotIdealSize = matchGroupSize != MatchGroup.MIN_NUM_TEAMS;
                    if (isNotIdealSize) {
                        Assert.assertTrue(matchGroupSize == MatchGroup.MAX_NUM_TEAMS);
                        undesiredGroupCount++;
                    }
                }
                //TODO assert that there are only ever groups of 4 at the end of the list of groups
                //there should never be more than 1 or 2 groups of 3
                Assert.assertTrue(undesiredGroupCount == 1 || undesiredGroupCount == 2);
            }
        }
    }

    @Test(expected = MatchMakingException.class)
    public void testIllegalTeamCounts() {
        int failureCount = 0;
        int teamCount;
        for (teamCount = 0; teamCount < MatchGroup.MIN_NUM_TEAMS; teamCount++) {
            try {
                MatchGroupGenerator.generateMatchGroupings(MockDatabase.getRankedLadderTeams(teamCount));
            } catch (IllegalStateException e) {
                failureCount++;
            }
        }

        //an exception should be thrown for every team count that's unable to be sorted into a group
        Assert.assertTrue(teamCount - 1 == failureCount);

        //it's impossible to sort 5 teams into groups of 3 or 4
        MatchGroupGenerator.generateMatchGroupings(MockDatabase.getRankedLadderTeams(IMPOSSIBLE_TEAM_COUNT));
    }
}
