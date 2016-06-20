package ca.sfu.cmpt373.alpha.vrcladder.ladder;

import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.MatchGroup;
import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.AttendanceCard;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.AttendanceStatus;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.PlayTime;
import ca.sfu.cmpt373.alpha.vrcladder.util.MockMatchGroupGenerator;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class LadderMethodsTest {
    private Ladder ladder;
    private List<MatchGroup> threeTeamMatchGroups;
    private List<MatchGroup> fourTeamMatchGroups;
    private List<Team> threeTeamMatchGroupTeams;
    private List<Team> fourTeamMatchGroupTeams;
    private static final int MATCHGROUP_COUNT = 1;

    @Before
    public void startUp() {
        ladder = new Ladder();
        fourTeamMatchGroups = new ArrayList<>();
        threeTeamMatchGroups = new ArrayList<>();
        threeTeamMatchGroupTeams = new ArrayList<>();
        fourTeamMatchGroupTeams = new ArrayList<>();

        for (int i = 0; i < MATCHGROUP_COUNT; i++) {
            threeTeamMatchGroups.add(MockMatchGroupGenerator.generateThreeTeamMatchGroup());
        }
        for (int i = 0; i < MATCHGROUP_COUNT; i++) {
            fourTeamMatchGroups.add(MockMatchGroupGenerator.generateFourTeamMatchGroup());
        }
        threeTeamMatchGroupTeams = convertMatchGroupListToTeamList(threeTeamMatchGroups);
        fourTeamMatchGroupTeams = convertMatchGroupListToTeamList(fourTeamMatchGroups);
    }

    private List<Team> convertMatchGroupListToTeamList(List<MatchGroup> matchGroups) {
        List<Team> teams = new ArrayList<>();
        for (MatchGroup matchGroup : matchGroups) {
            teams.addAll(matchGroup.getTeams());
        }
        return teams;
    }


    @Test
    public void testSwapTeams() {
        testSwapAllTeams(threeTeamMatchGroupTeams);
        testSwapAllTeams(fourTeamMatchGroupTeams);
    }

    private void testSwapAllTeams(List<Team> teams) {
        for (int i = 0; i < teams.size() - 1; i++) {
            int team1Index = i;
            int team2Index = i + 1;
            testSwap(teams, team1Index, team2Index);
            testSwap(teams, team2Index, team1Index);
        }
    }

    private void testSwap(List<Team> teams, int team1Index, int team2Index) {
        ladder = new Ladder(teams);
        List<Team> rankedTeams = ladder.getLadder();

        //make sure teams are in the correct order to begin with
        for (int i = 0; i < teams.size(); i++) {
            Assert.assertEquals(teams.get(i), rankedTeams.get(i));
        }

        ladder.swapTeams(teams.get(team1Index), teams.get(team2Index));

        rankedTeams = ladder.getLadder();
        Assert.assertEquals(rankedTeams.get(team1Index), teams.get(team2Index));
        Assert.assertEquals(rankedTeams.get(team2Index), teams.get(team1Index));
    }

    @Test
    public void testApplyPenalties() {
        testApplyPenalties(threeTeamMatchGroups);
        testApplyPenalties(fourTeamMatchGroups);
    }

    private void testApplyPenalties(List<MatchGroup> matchGroups) {
        //TODO: add case for testing NOT_ATTENDING penalty
        List<Team> matchGroupTeamList = convertMatchGroupListToTeamList(matchGroups);
        for (int i = 0; i < matchGroups.size(); i++) {
            testApplyPenalties(
                    matchGroups,
                    matchGroupTeamList.subList(0, i),
                    AttendanceStatus.NO_SHOW);
            testApplyPenalties(
                    matchGroups,
                    matchGroupTeamList.subList(0, i),
                    AttendanceStatus.LATE);
        }
    }

    /**
     * iterates through all the teams in each matchgroup, and applies the specified penalty
     * to all the teams that are also in the teamsToApplyPenalties list
     * @param teamsToApplyPenalties should be a list of teams in RANKED order
     */
    private void testApplyPenalties(List<MatchGroup> matchGroups, List<Team> teamsToApplyPenalties, AttendanceStatus penaltyToApply) {
        ladder = new Ladder(convertMatchGroupListToTeamList(matchGroups));

        //set ranks within MatchGroups to be unchanged in order to test penalties in isolation
        for (MatchGroup matchGroup : matchGroups) {
            matchGroup.getScoreCard().setRankedTeams(matchGroup.getTeams());
        }

        //set all teams to attending to test penalties in isolation
        for (MatchGroup matchGroup : matchGroups) {
            for (Team team : matchGroup.getTeams()) {
                AttendanceCard attendanceCard = team.getAttendanceCard();
                attendanceCard.setPreferredPlayTime(PlayTime.TIME_SLOT_A);
                attendanceCard.setAttendanceStatus(AttendanceStatus.PRESENT);
            }
        }

        //apply penalty to all teams being tested
        for (Team team : teamsToApplyPenalties) {
            AttendanceCard firstTeamAttendanceCard = team.getAttendanceCard();
            firstTeamAttendanceCard.setAttendanceStatus(AttendanceStatus.NO_SHOW);
        }

        //save the original positions before updating ladder
        List<Integer> originalLadderPositions = new ArrayList<>();
        for (Team team : teamsToApplyPenalties) {
            originalLadderPositions.add(ladder.findTeamPosition(team));
        }

        ladder.updateLadder(matchGroups);

        //verify teams are in their correct positions
        for (int i = 0; i < teamsToApplyPenalties.size(); i++) {
            int originalLadderPosition = originalLadderPositions.get(i);
            int newLadderPosition = ladder.findTeamPosition(teamsToApplyPenalties.get(i));
            int penalty = penaltyToApply.getPenalty();
            boolean isTeamPenaltyWithinLadderBounds =
                    (newLadderPosition + penalty <= ladder.getTeamCount());
            if (isTeamPenaltyWithinLadderBounds) {
                Assert.assertTrue(newLadderPosition == originalLadderPosition + penalty);
            } else {
                //all the teams that come after the current team should be beneath the current team
                int numPenalizedTeamsAfterCurrentTeam = (teamsToApplyPenalties.size() - 1) - i;
                int expectedLadderPosition = ladder.getTeamCount() - numPenalizedTeamsAfterCurrentTeam;
                Assert.assertEquals(expectedLadderPosition, newLadderPosition);
            }
        }
    }

    @Test
    public void testRankingsWithinMatchGroups() {
        testRankingsWithinMatchGroup(MockMatchGroupGenerator.generateThreeTeamMatchGroup());
        testRankingsWithinMatchGroup(MockMatchGroupGenerator.generateFourTeamMatchGroup());
    }

    private void testRankingsWithinMatchGroup(MatchGroup matchGroup) {
        List<MatchGroup> matchGroups = new ArrayList<>();
        matchGroups.add(matchGroup);

        //create a new list of the teams in the MatchGroup that's modifiable
        List<Team> matchGroupTeams = new ArrayList<>();
        matchGroupTeams.addAll(matchGroup.getTeams());

        //make sure everyone's set to attend
        for (Team team : matchGroupTeams) {
            team.getAttendanceCard().setPreferredPlayTime(PlayTime.TIME_SLOT_A);
        }

        List<List<Team>> permutations = generatePermutations(matchGroupTeams);

        for (List<Team> teamPermutation : permutations) {
            ladder = new Ladder(matchGroup.getTeams());
            matchGroup.getScoreCard().setRankedTeams(teamPermutation);
            ladder.updateLadder(matchGroups);
            Assert.assertEquals(ladder.getLadder(), teamPermutation);
        }
    }

    //TODO: cite this
    //from http://stackoverflow.com/questions/10305153/generating-all-possible-permutations-of-a-list-recursively
    private List<List<Team>> generatePermutations(List<Team> original) {
        if (original.size() == 0) {
            List<List<Team>> result = new ArrayList<List<Team>>();
            result.add(new ArrayList<Team>());
            return result;
        }
        Team firstElement = original.remove(0);
        List<List<Team>> returnValue = new ArrayList<List<Team>>();
        List<List<Team>> permutations = generatePermutations(original);
        for (List<Team> smallerPermutated : permutations) {
            for (int index=0; index <= smallerPermutated.size(); index++) {
                List<Team> temp = new ArrayList<Team>(smallerPermutated);
                temp.add(index, firstElement);
                returnValue.add(temp);
            }
        }
        return returnValue;
    }
}
