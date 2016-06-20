package ca.sfu.cmpt373.alpha.vrcladder.ladder;

import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.MatchGroup;
import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.AttendanceCard;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.AttendanceStatus;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Predicate;


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

        //switch highest team of match with lowest team of previous match
        swapTeamsBetweenMatchGroup(matchGroups);

        //TODO: check if it matters which order these are called in
        applyNotAttendingPenalty();
        applyAttendanceStatusPenalty(AttendanceStatus.LATE);
        applyAttendanceStatusPenalty(AttendanceStatus.NO_SHOW);
    }

    private void applyNotAttendingPenalty() {
        applyPenalty(
                (team -> !team.getAttendanceCard().isAttending()),
                NOT_ATTENDING_PENALTY);
    }

    private void applyAttendanceStatusPenalty(AttendanceStatus attendanceStatus) {
        applyPenalty(
                (team -> team.getAttendanceCard().getAttendanceStatus() == attendanceStatus),
                attendanceStatus.getPenalty());
    }

    private void applyPenalty(Predicate<Team> condition, int penalty) {
        List<Team> teamsToApplyPenaltiesTo = new ArrayList<>();
        for (Team team : ladder) {
            if (condition.test(team)) {
                teamsToApplyPenaltiesTo.add(team);
            }
        }

        for (Team team : teamsToApplyPenaltiesTo) {
            // we need to get the index at the moment of swapping
            // because other team penalties being applied shift the indices around
            int ladderIndex = findTeamPosition(team) - 1;
            ladder.remove(ladderIndex);
            if (ladderIndex < ladder.size() - penalty) {
                ladder.add(ladderIndex + penalty, team);
            } else {
                ladder.add(team);
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
            if (attendanceCard.isAttending() && attendanceCard.isPresent()) {
                return teams.get(i);
            }
        }
        throw new IllegalStateException(ERROR_MATCHGROUP_NO_TEAMS_PRESENT);
    }

    private Team getFirstAttendingTeam(List<Team> teams) {
        for (Team team : teams) {
            AttendanceCard attendanceCard = team.getAttendanceCard();
            if (attendanceCard.isAttending() && attendanceCard.isPresent()) {
                return team;
            }
        }
        throw new IllegalStateException(ERROR_MATCHGROUP_NO_TEAMS_PRESENT);
    }

    private void applyRankingsWithinMatchGroup(MatchGroup matchGroup){
        //find the ladder indices for 1st, 2nd, 3rd, 4th... positions
        //these need to be found before, because we will be overwriting teams below
        List<Integer> rankedIndexes = new ArrayList<>();
        for (Team team : matchGroup.getTeams()) {
            rankedIndexes.add(findTeamPosition(team) - 1);
        }

        //overwrite the team in each position with the teams specified in the ScoreCard
        List<Team> rankedTeams = matchGroup.getScoreCard().getRankedTeams();
        for (int i = 0; i < matchGroup.getTeamCount(); i++) {
            Team team = matchGroup.getTeams().get(i);
            AttendanceCard attendanceCard = team.getAttendanceCard();
            if (attendanceCard.isAttending() && attendanceCard.isPresent()) {
                ladder.set(rankedIndexes.get(i), rankedTeams.get(i));
            } else {
                //if a team does not attend, its position within its group should remain the same
                boolean hasTeamMovedWithinGroup = !team.equals(rankedTeams.get(i));
                if (hasTeamMovedWithinGroup) {
                    throw new IllegalStateException(ERROR_MATCHGROUP_TEAM_NOT_ATTENDING);
                }
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
