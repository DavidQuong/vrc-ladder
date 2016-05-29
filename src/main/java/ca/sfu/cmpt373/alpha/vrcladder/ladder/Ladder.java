package ca.sfu.cmpt373.alpha.vrcladder.ladder;

import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
import ca.sfu.cmpt373.alpha.vrcladder.users.personal.UserName;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;
import java.util.Random;

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

    public int findTeamPosition(Team team) throws NoSuchElementException{
        for (Team t:ladderList){
            if(team.getId().equals(t.getId())) return ladderList.indexOf(t);
        }
        throw new NoSuchElementException("Team with id: " + team.getId() + " does not exist in the ladder.");
    }


    /**
     * Inserts team at the bottom of the ladder
     * @param team
     */
    public void pushTeam(Team team){
        ladderList.add(team);
    }

    /**
     * Inserts team at the position specified
     * @param position
     * @param team
     */
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


