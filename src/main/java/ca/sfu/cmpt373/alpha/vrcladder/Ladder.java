package ca.sfu.cmpt373.alpha.vrcladder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Amaterasu on 2016-05-26.
 */
public class Ladder {

    //ladderList of team objects
    List<Integer> ladderList;

    public Ladder(){
        ladderList = new ArrayList<>();
    }

    public Ladder(List<Integer> newLadder){
        ladderList = newLadder;
    }

    /**
     * Zero indexed
     */
    public int getTeamAtPosition(int teamPosition){

        return -1;
    }

}


