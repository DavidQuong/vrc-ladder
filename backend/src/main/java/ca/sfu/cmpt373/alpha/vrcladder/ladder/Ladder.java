package ca.sfu.cmpt373.alpha.vrcladder.ladder;

import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.MatchGroup;
import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;


public class Ladder {


    List<Team> ladderList;
    final private int LADDER_VOLUME = 200;
    final private int ATTENDANCE_PENALTY =2;
    private int teamCount;

    public enum SHIFT_DIRECTION {UP, DOWN}


    public Ladder() {

        ladderList = new ArrayList<>(LADDER_VOLUME);
        teamCount = 0;
    }

    public Ladder(List<Team> newLadder) {

        ladderList = newLadder;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder("");

        for (int i = 0; i < ladderList.size(); i++) {
            stringBuilder.append(i + " :");
            stringBuilder.append(ladderList.get(i).toString() + "\n ");
        }

        String s = stringBuilder.toString();
        return s;
    }


    public Team findTeamAtPosition(int teamPosition) {

        return ladderList.get(teamPosition);
    }

    public int findTeamPosition(Team team) throws NoSuchElementException {
        for (Team t : ladderList) {
            if (t != null && team.getId().equals(t.getId())) return ladderList.indexOf(team);
        }
        throw new NoSuchElementException("Team with id: " + team.getId() + " does not exist in the ladder.");
    }



    public void pushTeam(Team team) {
        ladderList.add(team);
    }

    public void insertTeamAtPosition(int position, Team team) {

        ladderList.add(position, team);

    }

    public int getLadderVolume() {
        return LADDER_VOLUME;
    }

    public int getLadderTeamCount() {
        return ladderList.size();
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

        Team tempTeam = ladderList.get(team1Position);
        ladderList.remove(team1Position);
        insertTeamAtPosition(team1Position, ladderList.get(team2Position - 1));
        ladderList.remove(team2Position);
        insertTeamAtPosition(team2Position, tempTeam);
    }

    private boolean verifyPositions(int position) {
        return (position < 0 || position >= ladderList.size());
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
        for (int matchIndex =1; matchIndex<MatchGroup.length; matchIndex++) {
            swapBetweenMatchGroup(MatchGroup, matchIndex);
        }

        int ladderSize = this.getLadderTeamCount();
        //prevent loop from going out of bounds
        if (ladderSize>=getLadderVolume()){
            ladderSize-=ATTENDANCE_PENALTY;
        }

        for (int k =0;k<ladderSize; k++){
            if(!ladderList.get(k).getAttendanceCard().isAttending()){
                Team tempTeam = ladderList.get(k);
                ladderList.remove(k);
                ladderList.add(k-ATTENDANCE_PENALTY, tempTeam);
            }

        }
    }

    private void swapBetweenMatchGroup(MatchGroup[] MatchGroup, int index){
        List<Teams> teamOrder = match.getPlacement();


        int[] teamPos = {findTeamPosition(MatchGroup[index].getTeam1()),
                findTeamPosition(MatchGroup[index].getTeam2()),
                findTeamPosition(MatchGroup[index].getTeam3())};

        int lowTeam =
        if(teamOrder.size > 3)


        ilowTeam = findLowestNumber(teamPos);

        teamPos[0] = findTeamPosition(MatchGroup[index-1].getTeam1());
        teamPos[1] = findTeamPosition(MatchGroup[index-1].getTeam2());
        teamPos[2] = findTeamPosition(MatchGroup[index-1].getTeam3());

        int highTeam = findHighestNumber(teamPos);

        swapTeams(highTeam,lowTeam);
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
        List<Teams> teamOrder = match.getPlacement();
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
        if(findTeamPosition(teamOrder.get(1))>findTeamPosition(teamOrder.get(2))
            lowTeam =findTeamPosition(teamOrder.get(2));
        else
            lowTeam =findTeamPosition(teamOrder.get(1));

        //swap second and third, if applicable
        if(findTeamPosition(teamOrder.get(2))>findTeamPosition(teamOrder.get(2))
            highTeam =findTeamPosition(teamOrder.get(1));
        else
            highTeam =findTeamPosition(teamOrder.get(2));

        swapTeams(highTeam,lowTeam);
    }
    private void arrange3Teams(MatchGroup match){
        List<Teams> teamOrder = match.getPlacement();

        int[] teamPos = {
                findTeamPosition(teamOrder.get(0)),
                findTeamPosition(teamOrder.get(1)),
                findTeamPosition(teamOrder.get(2))};

        int lowTeam = findLowestNumber(teamPos);
        int highTeam = findHighestNumber(teamPos);
        swapTeams(highTeam,lowTeam);


    }

}
