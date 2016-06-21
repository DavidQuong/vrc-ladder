package ca.sfu.cmpt373.alpha.vrcladder.ladder;

import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.MatchGroup;
import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.AttendanceCard;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.AttendanceStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;


public class Ladder {

    private static final String ERROR_MATCHGROUP_NO_TEAMS_PRESENT = "No teams were present in this MatchGroup";
    private static final String ERROR_MATCHGROUP_TEAM_NOT_ATTENDING = "Teams that did not attend should not change rankings within their matchgroups";
    private static final String ERROR_MATCHGROUPS_NOT_RANKED = "MatchGroups are not in ranked order";
    private static final String ERROR_DUPLICATE_TEAM = "The Ladder already contains this team. The ladder may not hold duplicate elements";

    private static final int NOT_ATTENDING_PENALTY = 2;

    private List<Team> ladder;

    public Ladder() {
        ladder = new ArrayList<>();
    }

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

    public void swapTeams(Team team1, Team team2) {
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
            Team team = matchGroup.getTeams().get(i);
            AttendanceCard attendanceCard = team.getAttendanceCard();
            if (attendanceCard.isPresent()) {
                ladder.set(rankedIndices.get(i), rankedTeams.get(i));
            } else {
                //if a team does not attend, its position within its group should remain the same
                boolean hasTeamMovedWithinGroup = !team.equals(rankedTeams.get(i));
                if (hasTeamMovedWithinGroup) {
                    throw new IllegalStateException(ERROR_MATCHGROUP_TEAM_NOT_ATTENDING);
                }
            }
        }
    }

    private void swapTeamsBetweenMatchGroup(List<MatchGroup> matchGroups) {
        for (int i = 0; i < matchGroups.size() - 1; i++) {
            List<Team> rankedMatchGroupTeams1 = matchGroups.get(i).getScoreCard().getRankedTeams();
            List<Team> rankedMatchGroupTeams2 = matchGroups.get(i + 1).getScoreCard().getRankedTeams();

            // we must only consider teams within MatchGroups that are attending.
            // Teams that are not attending should not be considered in any ranking changes within/between MatchGroups
            // Instead, these teams are penalized separately after everything else is done
            Team lastPlaceAttendingTeamInMatchGroup1 = getLastAttendingTeam(rankedMatchGroupTeams1);
            Team firstPlaceAttendingTeamInMatchGroup2 = getFirstAttendingTeam(rankedMatchGroupTeams2);

            if (findTeamPosition(lastPlaceAttendingTeamInMatchGroup1) > findTeamPosition(firstPlaceAttendingTeamInMatchGroup2)) {
                throw new IllegalStateException(ERROR_MATCHGROUPS_NOT_RANKED);
            }

            swapTeams(lastPlaceAttendingTeamInMatchGroup1, firstPlaceAttendingTeamInMatchGroup2);
        }
    }

    private Team getLastAttendingTeam(List<Team> teams) {
        for (int i = teams.size() - 1; i >= 0; i--) {
            AttendanceCard attendanceCard = teams.get(i).getAttendanceCard();
            if (attendanceCard.isPresent()) {
                return teams.get(i);
            }
        }
        throw new IllegalStateException(ERROR_MATCHGROUP_NO_TEAMS_PRESENT);
    }

    private Team getFirstAttendingTeam(List<Team> teams) {
        for (Team team : teams) {
            AttendanceCard attendanceCard = team.getAttendanceCard();
            if (attendanceCard.isPresent()) {
                return team;
            }
        }
        throw new IllegalStateException(ERROR_MATCHGROUP_NO_TEAMS_PRESENT);
    }

    private void applyPenalties() {
        List<TeamIndexPenaltyTuple> teamsToApplyPenaltiesTo = getAndRemoveTeamsToApplyPenaltiesTo();

        //sort so that highest ranked teams are re-added first
        //this way, we don't have to worry about the list indices shifting around
        Collections.sort(teamsToApplyPenaltiesTo, TeamIndexPenaltyTuple.getNewIndexComparator());

        //re-add penalized teams at their new indices
        for (TeamIndexPenaltyTuple tuple : teamsToApplyPenaltiesTo) {
            //if more than one team is supposed to be positioned at the same new index, resolve the conflict between them.
            List<TeamIndexPenaltyTuple> tuplesWithSameNewIndex = findTuplesWithSameNewIndex(teamsToApplyPenaltiesTo, tuple);
            insertTuplesAtSameNewIndex(tuplesWithSameNewIndex);
        }
    }

    private List<TeamIndexPenaltyTuple> getAndRemoveTeamsToApplyPenaltiesTo() {
        List<TeamIndexPenaltyTuple> teamsToApplyPenaltiesTo = new ArrayList<>();

        for (int i = 0; i < ladder.size(); i++) {
            Team team = ladder.get(i);
            AttendanceCard attendanceCard = team.getAttendanceCard();
            if (!attendanceCard.isAttending()) {
                teamsToApplyPenaltiesTo.add(new TeamIndexPenaltyTuple(team, i, NOT_ATTENDING_PENALTY));
            } else if (attendanceCard.getAttendanceStatus() == AttendanceStatus.LATE) {
                teamsToApplyPenaltiesTo.add(new TeamIndexPenaltyTuple(team, i, AttendanceStatus.LATE.getPenalty()));
            } else if (attendanceCard.getAttendanceStatus() == AttendanceStatus.NO_SHOW) {
                teamsToApplyPenaltiesTo.add(new TeamIndexPenaltyTuple(team, i, AttendanceStatus.NO_SHOW.getPenalty()));
            } else {
                continue;
            }
            ladder.remove(i);
            i--;
        }
        return teamsToApplyPenaltiesTo;
    }

    private List<TeamIndexPenaltyTuple> findTuplesWithSameNewIndex(List<TeamIndexPenaltyTuple> tuples, TeamIndexPenaltyTuple indexTuple) {
        List<TeamIndexPenaltyTuple> tuplesWithSameNewIndex = new ArrayList<>();
        for (TeamIndexPenaltyTuple currTuple : tuples) {
            if (indexTuple.getNewIndex() == currTuple.getNewIndex()) {
                tuplesWithSameNewIndex.add(currTuple);
            }
        }
        return tuplesWithSameNewIndex;
    }

    private void insertTuplesAtSameNewIndex(List<TeamIndexPenaltyTuple> tuplesWithSameNewIndex) {
        //sort tuples by penalty so that for conflicting teams, the teams with the lowest penalty get priority
        Collections.sort(tuplesWithSameNewIndex, TeamIndexPenaltyTuple.getPenaltyComparator());

        for (int i = 0; i < tuplesWithSameNewIndex.size(); i++) {
            TeamIndexPenaltyTuple currTuple = tuplesWithSameNewIndex.get(i);
            int newIndex = currTuple.getNewIndex();
            if (newIndex < ladder.size()) {
                // for each following team, the index must be offset to
                // compensate for prior teams in the tuples list being added to the ladder
                ladder.add(newIndex + i, currTuple.getTeam());
            } else {
                //if the new index is beyond the ladder bounds, add the element to the end of the list
                //
                ladder.add(currTuple.getTeam());
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
