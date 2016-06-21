package ca.sfu.cmpt373.alpha.vrcladder.ladder;

import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.MatchGroup;
import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.AttendanceCard;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.AttendanceStatus;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.PlayTime;
import ca.sfu.cmpt373.alpha.vrcladder.util.MockMatchGroupGenerator;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class LadderMethodsTest {
    private List<MatchGroup> threeTeamMatchGroups;
    private List<MatchGroup> fourTeamMatchGroups;
    private static final int MATCHGROUP_COUNT = 3;

    @Before
    public void startUp() {
        threeTeamMatchGroups = new ArrayList<>();
        fourTeamMatchGroups = new ArrayList<>();
        for (int i = 0; i < MATCHGROUP_COUNT; i++) {
            threeTeamMatchGroups.add(MockMatchGroupGenerator.generateThreeTeamMatchGroup());
        }
        for (int i = 0; i < MATCHGROUP_COUNT; i++) {
            fourTeamMatchGroups.add(MockMatchGroupGenerator.generateFourTeamMatchGroup());
        }

        setAllMatchGroupTeamsPresent(threeTeamMatchGroups);
        setAllMatchGroupTeamsPresent(fourTeamMatchGroups);
    }

    private void setAllMatchGroupTeamsPresent(List<MatchGroup> matchGroups) {
        for (MatchGroup matchGroup : matchGroups) {
            for (Team team : matchGroup.getTeams()) {
                AttendanceCard attendanceCard = team.getAttendanceCard();
                attendanceCard.setPreferredPlayTime(PlayTime.TIME_SLOT_A);
                attendanceCard.setAttendanceStatus(AttendanceStatus.PRESENT);
            }
        }
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
        testSwapAllTeams(convertMatchGroupListToTeamList(threeTeamMatchGroups));
        testSwapAllTeams(convertMatchGroupListToTeamList(fourTeamMatchGroups));
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
        Ladder ladder = new Ladder(teams);
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
        // TODO: Add case for testing NOT_ATTENDING penalty
        // TODO: Clarify requirements for when multiple teams are penalized, right now tests only penalize one team at a time
        testApplyPenalties(threeTeamMatchGroups);
        testApplyPenalties(fourTeamMatchGroups);
    }

    private void testApplyPenalties(List<MatchGroup> matchGroups) {
        List<Team> matchGroupTeamList = convertMatchGroupListToTeamList(matchGroups);
        for (int i = 0; i < matchGroups.size(); i++) {
            testApplyPenalties(
                    matchGroups,
                    matchGroupTeamList.get(i),
                    AttendanceStatus.NO_SHOW);
            testApplyPenalties(
                    matchGroups,
                    matchGroupTeamList.get(i),
                    AttendanceStatus.LATE);
        }
    }

    private void testApplyPenalties(List<MatchGroup> matchGroups, Team penalizedTeam, AttendanceStatus penaltyToApply) {
        Ladder ladder = new Ladder(convertMatchGroupListToTeamList(matchGroups));

        //set ranks within MatchGroups to be unchanged in order to test penalties in isolation
        for (MatchGroup matchGroup : matchGroups) {
            matchGroup.getScoreCard().setRankedTeams(matchGroup.getTeams());
        }

        //reinitialize all teams to present
        setAllMatchGroupTeamsPresent(matchGroups);

        //apply penalty just to the team being tested
        AttendanceCard firstTeamAttendanceCard = penalizedTeam.getAttendanceCard();
        firstTeamAttendanceCard.setAttendanceStatus(penaltyToApply);

        int originalLadderPosition = ladder.findTeamPosition(penalizedTeam);

        ladder.updateLadder(matchGroups);

        //verify the team is in the correct position
        int newLadderPosition = ladder.findTeamPosition(penalizedTeam);
        int penalty = penaltyToApply.getPenalty();
        boolean isTeamPenaltyWithinLadderBounds =
                (originalLadderPosition + penalty <= ladder.getTeamCount());
        if (isTeamPenaltyWithinLadderBounds) {
            Assert.assertEquals(newLadderPosition, originalLadderPosition + penalty);
        } else {
            int lastPosition = ladder.getTeamCount();
            Assert.assertEquals(lastPosition, newLadderPosition);
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
            Ladder ladder = new Ladder(matchGroup.getTeams());
            matchGroup.getScoreCard().setRankedTeams(teamPermutation);
            ladder.updateLadder(matchGroups);
            Assert.assertEquals(ladder.getLadder(), teamPermutation);
        }
    }

   private List<List<Team>> generatePermutations(List<Team> original) {
        if (original.size() == 0) {
            List<List<Team>> result = new ArrayList<>();
            result.add(new ArrayList<>());
            return result;
        }
        Team firstElement = original.remove(0);
        List<List<Team>> returnValue = new ArrayList<>();
        List<List<Team>> permutations = generatePermutations(original);
        for (List<Team> smallerPermutated : permutations) {
            for (int index=0; index <= smallerPermutated.size(); index++) {
                List<Team> temp = new ArrayList<
                        >(smallerPermutated);
                temp.add(index, firstElement);
                returnValue.add(temp);
            }
        }
        return returnValue;
    }

    @Test
    public void testSwapTeamsBetweenMatchGroups() {
        testSwapTeamsBetweenMatchGroups(threeTeamMatchGroups);
        testSwapTeamsBetweenMatchGroups(fourTeamMatchGroups);
    }

    private void testSwapTeamsBetweenMatchGroups(List<MatchGroup> matchGroups) {
        Ladder ladder = new Ladder(convertMatchGroupListToTeamList(matchGroups));

        for (MatchGroup matchGroup : matchGroups) {
            matchGroup.getScoreCard().setRankedTeams(matchGroup.getTeams());
        }

        List<Pair<Integer, Integer>> originalLastAndFirstTeamPositions = new ArrayList<>();
        for (int i = 0; i < matchGroups.size() - 1; i++) {
            MatchGroup currMatchGroup = matchGroups.get(i);
            MatchGroup nextMatchGroup = matchGroups.get(i + 1);
            originalLastAndFirstTeamPositions.add(getLastAndFirstTeamPositions(ladder, currMatchGroup, nextMatchGroup));
        }

        ladder.updateLadder(matchGroups);

        for (int i = 0; i < matchGroups.size() - 1; i++) {
            MatchGroup currMatchGroup = matchGroups.get(i);
            MatchGroup nextMatchGroup = matchGroups.get(i + 1);
            Pair<Integer, Integer> newLastAndFirstTeamPositions = getLastAndFirstTeamPositions(ladder, currMatchGroup, nextMatchGroup);

            Assert.assertEquals(originalLastAndFirstTeamPositions.get(i).getLeft(), newLastAndFirstTeamPositions.getRight());
            Assert.assertEquals(originalLastAndFirstTeamPositions.get(i).getRight(), newLastAndFirstTeamPositions.getLeft());
        }
    }

    /**
     * @return the current ladder positions of the lowest ranked team in the firstMatchGroup,
     * and the highest ranked team in the nextMatchGroup
     */
    private Pair<Integer, Integer> getLastAndFirstTeamPositions(Ladder ladder, MatchGroup firstMatchGroup, MatchGroup nextMatchGroup) {
        List<Team> firstMatchGroupRankedTeams = firstMatchGroup.getScoreCard().getRankedTeams();
        int positionLastTeamOfFirstMatchGroup = ladder.findTeamPosition(firstMatchGroupRankedTeams.get(firstMatchGroupRankedTeams.size() - 1));

        List<Team> nextMatchGroupRankedTeams = nextMatchGroup.getScoreCard().getRankedTeams();
        int positionFirstTeamOfNextMatchGroup = ladder.findTeamPosition(nextMatchGroupRankedTeams.get(0));

        return new ImmutablePair<>(positionLastTeamOfFirstMatchGroup, positionFirstTeamOfNextMatchGroup);
    }
}
