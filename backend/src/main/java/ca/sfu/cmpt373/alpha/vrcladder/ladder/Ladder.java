package ca.sfu.cmpt373.alpha.vrcladder.ladder;

import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.MatchGroup;
import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;



//public enum SHIFT_DIRECTION {UP, DOWN}


import java.util.*;


public class Ladder {

    private int teamCount;
    private List<Team> ladder;
    final private int ATTENDANCE_PENALTY = 2;
    final private int LATE_PENALTY =4;
    final private int NO_SHOW_PENALTY =10;

    public Ladder() {

        ladder = new ArrayList<>();
        teamCount = 0;
    }

    public Ladder(List<Team> newLadder) {

        ladder = newLadder;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("");

        for (int i = 0; i < ladder.size(); i++) {
            stringBuilder.append(i + " :");
            stringBuilder.append(ladder.get(i).toString() + "\n ");
        }

        String s = stringBuilder.toString();
        return s;
    }


    public Team findTeamAtPosition(int teamPosition) {

        return ladder.get(teamPosition);
    }

    public int findTeamPosition(Team team) throws NoSuchElementException {
        for (Team t : ladder) {
            if (t != null && team.getId().equals(t.getId())) return ladder.indexOf(team);
        }
        throw new NoSuchElementException("Team with id: " + team.getId() + " does not exist in the ladder.");
    }



    public void pushTeam(Team team) {
        ladder.add(team);
    }

    public void insertTeamAtPosition(int position, Team team) {

        ladder.add(position, team);

    }



    public int getLadderTeamCount() {
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

    public void swapTeams(int team1Position, int team2Position) {
        if (!verifyPositions(team1Position) || !verifyPositions(team2Position)) return;

        Team tempTeam = ladder.get(team1Position);
        ladder.remove(team1Position);
        insertTeamAtPosition(team1Position, ladder.get(team2Position - 1));
        ladder.remove(team2Position);
        insertTeamAtPosition(team2Position, tempTeam);
    }

    private boolean verifyPositions(int position) {
        return (position < 0 || position >= ladder.size());
    }


    private void updateDatabase() {


    }

    private int findLowestNumber(int[] array) {
        int lowest = array[0];
        for (int i=0;i<array.length;i++) {
            if (array[i] < lowest) {
                lowest = array[i];
            }
        }
        return lowest;

    }
    private int findHighestNumber(int[] array) {
        int highest = 0;
        for (int i=0;i<array.length;i++) {
            if (array[i] > highest){
                highest = array[i];
            }
        }
        return highest;
    }

    //swap teams in a match based on rank, then switch highest team of match with lowest team of previous match
    //then, apply penalties
    public void updateLadder(MatchGroup[] MatchGroup){
        arrangeMatchResults(MatchGroup[0]);
        for (int matchIndex =1; matchIndex<MatchGroup.length; matchIndex++){
            arrangeMatchResults(MatchGroup[matchIndex]);
        }
        for (int matchIndex =0; matchIndex<MatchGroup.length; matchIndex++) {
            swapBetweenMatchGroup(MatchGroup, matchIndex);
        }

        int ladderSize = this.getLadderTeamCount() - ATTENDANCE_PENALTY;
        for (int k =0;k<ladderSize; k++){
            if(!ladder.get(k).getAttendanceCard().willAttend()){
                Team tempTeam = ladder.get(k);
                ladder.remove(k);
                ladder.add(k-ATTENDANCE_PENALTY, tempTeam);
            }

        }
        ladderSize = this.getLadderTeamCount() - NO_SHOW_PENALTY;
        for (int k =0;k<ladderSize; k++){
            if(ladder.get(k).getAttendanceCard().noShow()){
                Team tempTeam = ladder.get(k);
                ladder.remove(k);
                ladder.add(k-NO_SHOW_PENALTY, tempTeam);
            }

        }

        ladderSize = this.getLadderTeamCount() - LATE_PENALTY;
        for (int k =0;k<ladderSize; k++){
            if(ladder.get(k).getAttendanceCard().late()){
                Team tempTeam = ladder.get(k);
                ladder.remove(k);
                ladder.add(k-LATE_PENALTY, tempTeam);
            }

        }

    }

    private void swapBetweenMatchGroup(MatchGroup[] MatchGroup, int index) {
        if (MatchGroup.length >= index){
            return;
        }
        List<Team> teamOrder1 = MatchGroup[index].getPlacement();
        List<Team> teamOrder2 = MatchGroup[index - 1].getPlacement();

        if (teamOrder1.size() > 3){
            swapTeams(teamOrder1.get(3), teamOrder2.get(0));
        }
        else
            swapTeams(teamOrder1.get(2), teamOrder2.get(0));

    }


    private void arrangeMatchResults(MatchGroup match){
        if (match.getTeams().size()==3){
            arrange3Teams(match);
        }
        else if(match.getTeams().size()==4){
            arrange4Teams(match);
        }

    }

    private void arrange4Teams(MatchGroup match){
        List<Team> teamOrder = match.getPlacement();
        int[] teamPos={
                findTeamPosition(teamOrder.get(0)),
                findTeamPosition(teamOrder.get(1)),
                findTeamPosition(teamOrder.get(2)),
                findTeamPosition(teamOrder.get(3))
        };

        int lowTeam = findLowestNumber(teamPos);
        int highTeam = findHighestNumber(teamPos);
        swapTeams(highTeam,lowTeam);
        //swap first and fourth, if applicable
        if(findTeamPosition(teamOrder.get(1))>findTeamPosition(teamOrder.get(2))){
            lowTeam =findTeamPosition(teamOrder.get(2));
        }
        else
            lowTeam =findTeamPosition(teamOrder.get(1));

        //swap second and third, if applicable
        if(findTeamPosition(teamOrder.get(2))>findTeamPosition(teamOrder.get(2))){
            highTeam = findTeamPosition(teamOrder.get(1));
        }
        else{
            highTeam =findTeamPosition(teamOrder.get(2));
        }

        swapTeams(highTeam,lowTeam);
    }
    private void arrange3Teams(MatchGroup match){
        List<Team> teamOrder = match.getPlacement();

        int[] teamPos ={
                findTeamPosition(teamOrder.get(0)),
                findTeamPosition(teamOrder.get(1)),
                findTeamPosition(teamOrder.get(2))};

        int lowTeam = findLowestNumber(teamPos);
        int highTeam = findHighestNumber(teamPos);
        swapTeams(highTeam,lowTeam);


    }

}
