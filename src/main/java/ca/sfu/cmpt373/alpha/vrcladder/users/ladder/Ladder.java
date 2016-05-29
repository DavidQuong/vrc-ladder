package ca.sfu.cmpt373.alpha.vrcladder.users.ladder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Amaterasu on 2016-05-26.
 */
public class Ladder {

    //ladderList of team objects
    List<Team> ladderList;


    public Ladder(){
        ladderList = new ArrayList<>();
    }

    public Ladder(List<Team> newLadder){
        ladderList = newLadder;
    }

    /**
     * Zero indexed
     */
    public Team getTeamAtPosition(int teamPosition){

        return ladderList.get(teamPosition);
    }

    public List<Team> getLadder(){
        return ladderList;
    }

    public void insertTeam(Team team){
        ladderList.add(team);
    }

    /**
     * Return total space the ladder takes up
     * @return
     */
    public int getLadderVolume(){
        return ladderList.size();
    }

    /**
     * Return  number of teams in the ladder
     * @return
     */
    public int getLadderTeamCount(){
        return -1;
    }


    /**
     * Swap
     */

}


