package ca.sfu.cmpt373.alpha.vrcladder.matchmaking.logic;


import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.MatchGroup;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.PlayTime;

public class Waitlist {

    private MatchGroup matchGroup;
    private PlayTime playTime;

    public Waitlist(MatchGroup group, PlayTime time){
        this.matchGroup = group;
        this.playTime = time;
    }

    public MatchGroup getMatchGroup(){
        return this.matchGroup;
    }

    public PlayTime getPlayTime(){
        return this.playTime;
    }

}
