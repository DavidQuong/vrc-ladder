package ca.sfu.cmpt373.alpha.vrcladder.ladder;

import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.MatchGroup;
import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.AttendanceCard;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.AttendanceStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;


public class Ladder {

    private static final String ERROR_MATCHGROUPS_NOT_RANKED = "MatchGroups are not in ranked order";
    private static final String ERROR_DUPLICATE_TEAM = "The Ladder already contains this team. The ladder may not hold duplicate elements";

    private List<Team> ladder;
    public Ladder(List<Team> teams) {
        ladder = new ArrayList<>();
        for (Team team : teams) {
            if (ladder.contains(team)) {
                throw new IllegalStateException(ERROR_DUPLICATE_TEAM);
            }
            ladder.add(team);
        }
    }

    public List<Team> getLadder() {
        return Collections.unmodifiableList(ladder);
    }

    public Team findTeamAtPosition(int teamPosition) {
        //search the actual ranking of the team, not the index
        return ladder.get(teamPosition - 1);
    }

    public int findTeamPosition(Team team) throws NoSuchElementException {
        int teamPosition = ladder.indexOf(team);
        if (teamPosition == -1) {
            throw new NoSuchElementException();
        }
        //return the actual ranking of the team, not the index
        return teamPosition + 1;
    }

    public void addTeam(Team team) {
        if (ladder.contains(team)) {
            throw new IllegalStateException(ERROR_DUPLICATE_TEAM);
        }
        ladder.add(team);
    }

    public int getTeamCount() {
        return ladder.size();
    }

    private void swapTeams(Team team1, Team team2) {
        int team1Position;
        int team2Position;
        try {
            team1Position = findTeamPosition(team1);
            team2Position = findTeamPosition(team2);
        } catch (NoSuchElementException e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
            return;
        }

        swapTeams(team1Position, team2Position);
    }

    private void swapTeams(int team1Position, int team2Position) {
        int team1Index = team1Position - 1;
        int team2Index = team2Position - 1;
        Collections.swap(ladder, team1Index, team2Index);
    }

    /**
     * @param matchGroups should be ranked in the same order as the ladder itself
     * @throws IllegalStateException if @matchGroups are not in ranked order
     */
    public void updateLadder(List<MatchGroup> matchGroups){
        for (MatchGroup matchGroup : matchGroups){
            applyRankingsWithinMatchGroup(matchGroup);
        }

        System.out.println("Teams After sorthing within MatchGroup: ");
        for (Team team : ladder) {
            System.out.println(team.getLadderPosition().getValue());
        }

        swapTeamsBetweenMatchGroup(matchGroups);

        applyPenalties();
    }

    private void applyRankingsWithinMatchGroup(MatchGroup matchGroup){
        //find the ladder indices for 1st, 2nd, 3rd, 4th... positions
        //these need to be found before, because we will be overwriting teams below
        List<Integer> rankedIndices = new ArrayList<>();
        for (Team team : matchGroup.getTeams()) {
            rankedIndices.add(findTeamPosition(team) - 1);
        }

        //overwrite the team in each position with the teams specified in the ScoreCard
        List<Team> rankedTeams = matchGroup.getScoreCard().getRankedTeams();
        for (int i = 0; i < matchGroup.getTeamCount(); i++) {
            ladder.set(rankedIndices.get(i), rankedTeams.get(i));
        }
    }

    private void swapTeamsBetweenMatchGroup(List<MatchGroup> matchGroups) {
        for (int i = 0; i < matchGroups.size() - 1; i++) {
            List<Team> rankedMatchGroupTeams1 = matchGroups.get(i).getScoreCard().getRankedTeams();
            List<Team> rankedMatchGroupTeams2 = matchGroups.get(i + 1).getScoreCard().getRankedTeams();

            Team lastPlaceTeamInMatchGroup1 = rankedMatchGroupTeams1.get(rankedMatchGroupTeams1.size() - 1);
            int firstPlaceIndex = 0;
            Team firstPlaceTeamInMatchGroup2 = rankedMatchGroupTeams2.get(firstPlaceIndex);

            if (findTeamPosition(lastPlaceTeamInMatchGroup1) > findTeamPosition(firstPlaceTeamInMatchGroup2)) {
                throw new IllegalStateException(ERROR_MATCHGROUPS_NOT_RANKED);
            }

            swapTeams(lastPlaceTeamInMatchGroup1, firstPlaceTeamInMatchGroup2);
        }
    }

    private void applyPenalties() {
        System.out.println("ladder before penalties: ");
        for (Team team : ladder) {
            System.out.println(team.getLadderPosition().getValue());
        }
        List<TeamIndexPenaltyTuple> teamsToApplyPenaltiesTo = getTeamsToApplyPenaltiesTo();
        removePenalizedTeams(teamsToApplyPenaltiesTo);

        System.out.println("Ladder Teams");
        for (Team team : ladder) {
            System.out.println(team.getLadderPosition().getValue());
        }
        System.out.println("teams to apply penalties to");
        for (TeamIndexPenaltyTuple tuple : teamsToApplyPenaltiesTo) {
            System.out.println(
                    "Team: "  + tuple.getTeam().getLadderPosition().getValue() + " " +
                    "Original Index: " + tuple.getOriginalIndex() + " " +
                    "Penalty: " + tuple.getPenalty() + " " +
                    "New Index: " + tuple.getNewIndex());
        }

        //sort so that highest ranked teams are re-added first
        //this way, we don't have to worry about the list indices shifting around
        //which guarantees that a team has shifted down the correct amount
        Collections.sort(teamsToApplyPenaltiesTo, TeamIndexPenaltyTuple.getNewIndexComparator());

        //re-add penalized teams at their new indices
        int prevNewIndex = 0;
        int prevConflictOffset = 0;
        for (int i = 0; i < teamsToApplyPenaltiesTo.size(); i++) {
            TeamIndexPenaltyTuple team = teamsToApplyPenaltiesTo.get(i);
            List<TeamIndexPenaltyTuple> teamsWithSameNewIndex = findTeamsWithSameNewIndex(teamsToApplyPenaltiesTo, team);
            boolean isConflictCompensationNeeded = prevConflictOffset + prevNewIndex >= team.getNewIndex();
            if (isConflictCompensationNeeded) {
                insertTeamsAtSameNewIndex(teamsWithSameNewIndex, prevConflictOffset);
            } else {
                insertTeamsAtSameNewIndex(teamsWithSameNewIndex, 0);
                prevConflictOffset = 0;
            }
            teamsWithSameNewIndex
                    .stream()
                    .forEach(teamsToApplyPenaltiesTo::remove);
            //roll back an index since we just removed the team at the current index
            i--;

            //if there was more than one team at an index, then record the conflict's offset
            //so that the next group of teams to be added can compensate appropriately
            prevConflictOffset += teamsWithSameNewIndex.size() - 1;
            prevNewIndex = team.getNewIndex();
        }
    }

    private List<TeamIndexPenaltyTuple> getTeamsToApplyPenaltiesTo() {
        List<TeamIndexPenaltyTuple> teamsToApplyPenaltiesTo = new ArrayList<>();

        for (int i = 0; i < ladder.size(); i++) {
            Team team = ladder.get(i);
            AttendanceCard attendanceCard = team.getAttendanceCard();
            if (!attendanceCard.isAttending()) {
                teamsToApplyPenaltiesTo.add(new TeamIndexPenaltyTuple(team, i, AttendanceCard.NOT_ATTENDING_PENALTY));
            } else if (attendanceCard.getAttendanceStatus() == AttendanceStatus.LATE) {
                teamsToApplyPenaltiesTo.add(new TeamIndexPenaltyTuple(team, i, AttendanceStatus.LATE.getPenalty()));
            } else if (attendanceCard.getAttendanceStatus() == AttendanceStatus.NO_SHOW) {
                teamsToApplyPenaltiesTo.add(new TeamIndexPenaltyTuple(team, i, AttendanceStatus.NO_SHOW.getPenalty()));
            }
        }

        return teamsToApplyPenaltiesTo;
    }

    private void removePenalizedTeams(List<TeamIndexPenaltyTuple> teamsToApplyPenalties) {
        for (TeamIndexPenaltyTuple team : teamsToApplyPenalties) {
            ladder.remove(team.getTeam());
        }
    }

    private List<TeamIndexPenaltyTuple> findTeamsWithSameNewIndex(List<TeamIndexPenaltyTuple> teams, TeamIndexPenaltyTuple indexTeam) {
        List<TeamIndexPenaltyTuple> teamsWithSameNewIndex = new ArrayList<>();
        for (TeamIndexPenaltyTuple currTeam : teams) {
            if (indexTeam.getNewIndex() == currTeam.getNewIndex()) {
                teamsWithSameNewIndex.add(currTeam);
            }
        }
        return teamsWithSameNewIndex;
    }

    private void insertTeamsAtSameNewIndex(List<TeamIndexPenaltyTuple> teamsWithSameNewIndex, int previousConflictOffset) {
        //sort teams by penalty so that for conflicting teams, the teams with the lowest penalty get priority
        Collections.sort(teamsWithSameNewIndex, TeamIndexPenaltyTuple.getPenaltyComparator());

        for (int conflictOffset = 0; conflictOffset < teamsWithSameNewIndex.size(); conflictOffset++) {
            TeamIndexPenaltyTuple currTeam = teamsWithSameNewIndex.get(conflictOffset);
            // for each following team, the index must be offset by i to
            // compensate for prior teams in the tuples list being added to the ladder
            // additionally, previousConflictOffset is needed if a previous conflict occurred, and the
            // current teams being re-added need to shift down to compensate for the previous conflict
            int newOffsetIndex = currTeam.getNewIndex() + previousConflictOffset + conflictOffset;
            if (newOffsetIndex < ladder.size()) {
                ladder.add(newOffsetIndex, currTeam.getTeam());
            } else {
                //if the new index is beyond the ladder bounds, add the element to the end of the list
                ladder.add(currTeam.getTeam());
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("");

        for (int i = 0; i < ladder.size(); i++) {
            stringBuilder.append(i + " :");
            stringBuilder.append(ladder.get(i).toString() + "\n ");
        }

        return stringBuilder.toString();
    }
}
