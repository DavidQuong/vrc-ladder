package ca.sfu.cmpt373.alpha.vrcladder.matchmaking.logic;


import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.MatchGroup;
import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.PlayTime;

import java.util.Objects;

public class Waitlist<T> {

    private T content;
    private PlayTime playTime;

    public Waitlist(T content, PlayTime time){
        this.content = content;
        this.playTime = time;
    }

    public T getContent(){
        return this.content;
    }

    public PlayTime getPlayTime(){
        return this.playTime;
    }

}
