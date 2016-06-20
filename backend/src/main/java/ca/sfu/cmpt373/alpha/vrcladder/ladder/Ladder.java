package ca.sfu.cmpt373.alpha.vrcladder.ladder;

import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.MatchGroup;
import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.AttendanceCard;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.AttendanceStatus;

import java.util.*;
import java.util.function.Predicate;


public class Ladder {
    private static final String ERROR_MATCHGROUP_TEAM_NOT_ATTENDING = "Teams that did not attend should not change rankings within their matchgroups";
    final static private int NOT_ATTENDING_PENALTY = 2;
    final static private int LATE_PENALTY =4;
    final static private int NO_SHOW_PENALTY =10;


    private List<Team> ladder;

    public Ladder() {
        ladder = new ArrayList<>();
    }

    public Ladder(List<Team> newLadder) {
        ladder = new ArrayList<>(newLadder);
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

    //swap teams in a match based on rank
    public void updateLadder(List<MatchGroup> matchGroups){
        for (MatchGroup matchGroup : matchGroups){
            applyRankingsWithinMatchGroup(matchGroup);
        }

        //switch highest team of match with lowest team of previous match
        for (int matchIndex = 0; matchIndex<matchGroups.size(); matchIndex++) {
            //TODO: enable this
//            swapBetweenMatchGroup(matchGroups, matchIndex);
        }

        //TODO: check if it matters which order these are called in
        applyNotAttendingPenalty();
        applyAttendanceStatusPenalty(AttendanceStatus.NO_SHOW);
        applyAttendanceStatusPenalty(AttendanceStatus.LATE);
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

    private void swapBetweenMatchGroup(List<MatchGroup> matchGroups, int index) {
        if (matchGroups.size() >= index||index==0){
            return;
        }

        List<Team> teamOrder1 = matchGroups.get(index).getScoreCard().getRankedTeams();
        List<Team> teamOrder2 = matchGroups.get(index - 1).getScoreCard().getRankedTeams();

        if (teamOrder1.size() > 3){
            swapTeams(teamOrder1.get(3), teamOrder2.get(0));
        }
        else
            swapTeams(teamOrder1.get(2), teamOrder2.get(0));

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
            if (attendanceCard.isAttending() && attendanceCard.attended()) {
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
}
