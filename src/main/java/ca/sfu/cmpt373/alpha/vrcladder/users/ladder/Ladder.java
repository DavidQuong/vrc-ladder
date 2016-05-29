package ca.sfu.cmpt373.alpha.vrcladder.users.ladder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Amaterasu on 2016-05-26.
 */
public class Ladder {

    //ladderList of team objects
    List<Team> ladderList;
    final private int LADDER_VOLUME = 200;
    private int teamCount;


    public Ladder(){

        ladderList = new ArrayList<>(LADDER_VOLUME);
        teamCount = 0;
    }

    public Ladder(List<Team> newLadder){

        ladderList = newLadder;
    }

    @Override
    public String toString(){

        return "Ladder Data";
    }

    /**
     * Zero indexed
     */
    public Team findTeamAtPosition(int teamPosition){

        return ladderList.get(teamPosition);
    }

    public int findTeamPosition(Team team){
        for (Team t:ladderList){
            if(t.getTeamID())
        }
    }

    public List<Team> getLadder(){
        return ladderList;
    }

    public void pushTeam(Team team){

        ladderList.add(team);
    }

    public void insertTeamAtPosition(int position, Team team){
        ladderList.add(position, team);
    }

    /**
     * Return total space the ladder takes up
     * @return
     */
    public int getLadderVolume(){
        return LADDER_VOLUME;
    }

    /**
     * Return  number of teams in the ladder
     * @return
     */
    public int getLadderTeamCount(){
        return ladderList.size();
    }


    /**
     * Swap the positions of two Teams in the ladder
     * @param team1
     * @param team2
     */
    public void swapTeams(Team team1, Team team2){

    }

    /**
     * Updates database with data based on current ladder
     */
    public void updateDatabase(){

    }

}


