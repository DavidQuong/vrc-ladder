package ca.sfu.cmpt373.alpha.vrcladder.ladder;

import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
//import ca.sfu.cmpt373.alpha.vrcladder.users.personal.UserName;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.*;

/**
 * Created by Amaterasu on 2016-05-26.
 */
public class Ladder {

    //ladderList of team objects
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
            //System.out.println(ladderList.indexOf(team) + ": " + team.getId() + "\n"
            //       + ladderList.indexOf(t) + ": " + t.getId().toString() + "\n\n");

            if (t != null && team.getId().equals(t.getId())) return ladderList.indexOf(team);
        }
        throw new NoSuchElementException("Team with id: " + team.getId() + " does not exist in the ladder.");
    }


    /**
     * Inserts team at the bottom of the ladder
     *
     * @param team
     */
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


    /**
     * Swap the positions of two Teams in the ladder
     *
     * @param team1
     * @param team2
     */
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

    /**
     * Swap the positions of two Teams in the ladder
     *
     * @param team1Position
     * @param team2Position
     */
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

    /**
     * Updates database with data based on current ladder
     */
    private void updateDatabase() {


    }

    private int findLowestNumber(int init, int mid, int end)
    {
        if(init<=mid&&init<=end)
        return init;

        if(mid<=init&&mid<=end)
            return init;

        if(end<=mid&&end<=init)
            return init;

        return -1;
    }
    private int findHighestNumber(int init, int mid, int end)
    {
        if(init>=mid&&init>=end)
            return init;

        if(mid>=init&&mid>=end)
            return init;

        if(end>=mid&&end>=init)
            return init;

        return -1;
    }

    //swap teams in a match based on rank, then switch highest team of match with lowest team of previous match
    //then, apply penalties
    public void updateLadder(Matches[] matches){
        arrangeMatchResults(matches[0]);//have to do 0 first, to arrang top of ladder
        for (int i =1;i<matches.length; i++){
            arrangeMatchResults(matches[i]);

            if(matches[i].teamsInvolved()==3)
                swapBetweenMatches(matches, i);


        }
        int ladderSize = this.getLadderTeamCount();
        //prevent loop from going out of bounds
        if (ladderSize>=getLadderVolume())
            ladderSize-=ATTENDANCE_PENALTY;

        for (int k =0;k<ladderSize; k++){
            if(!ladderList.get(k).getAttendanceCard().isAttending()){
                Team tempTeam = ladderList.get(k);
                ladderList.remove(k);
                ladderList.add(k-ATTENDANCE_PENALTY, tempTeam);

            }

        }
    }
    private void swapBetweenMatches(Matches[] matches, int index){
        int team1Pos = findTeamPosition(matches[index].getTeam1());
        int team2Pos = findTeamPosition(matches[index].getTeam2());
        int team3Pos = findTeamPosition(matches[index].getTeam3());

        int lowTeam = findLowestNumber(team1Pos, team2Pos, team3Pos);

        team1Pos = findTeamPosition(matches[index-1].getTeam1());
        team2Pos = findTeamPosition(matches[index-1].getTeam2());
        team3Pos = findTeamPosition(matches[index-1].getTeam3());

        int highTeam = findHighestNumber(team1Pos, team2Pos, team3Pos);

        swapTeams(highTeam,lowTeam);

    }
    /**
     * Shift all teams below the cutoff(exclusive) down or up a specified number of slots
     */
    private void shiftLadder(SHIFT_DIRECTION shiftDirection, int cutoff, int slots) {
        if (shiftDirection == SHIFT_DIRECTION.DOWN) {
            shiftLadderDown(cutoff, slots);
        } else if (shiftDirection == SHIFT_DIRECTION.UP) {
            shiftLadderUp(cutoff, slots);
        }

    }

    private void shiftLadderDown(int cutoff, int slots) {
        //start at the bottom and go up
        int decr = ladderList.size() - 1;
        while (decr > cutoff) {
            swapTeams(decr, decr + 1);
        }
    }

    private void shiftLadderUp(int cutoff, int slots) {

    }

    private void arrangeMatchResults(Matches match){
        if (match.teamsInvolved()==3)
            arrange3Teams(match);
        else if(match.teamsInvolved()==4)
            arrange4Teams(match);

    }

    private void arrange4Teams(Matches match){


    }
    private void arrange3Teams(Matches match){
        if(match.team1Wins()==0){
            if(match.team2Wins() ==2){
                swapTeams(match.getTeam1(), match.getTeam2());
            }
            else if(match.team3Wins() ==2){
                swapTeams(match.getTeam1(), match.getTeam3());
            }
        }
        if (match.team2Wins ==0){
            if (match.team3Wins()==2)
                swapTeams(match.getTeam2(),match.getTeam3());

        }

    }

}


