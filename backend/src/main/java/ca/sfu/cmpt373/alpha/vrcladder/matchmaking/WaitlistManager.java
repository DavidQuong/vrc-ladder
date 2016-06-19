package ca.sfu.cmpt373.alpha.vrcladder.matchmaking;


import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.logic.Waitlist;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.PlayTime;
import java.util.ArrayList;
import java.util.List;

public class WaitlistManager {

    private static List<Waitlist> waitlist;

    public static void addToWaitlist(MatchGroup group, PlayTime time){
        Waitlist current = new Waitlist(group, time);
        waitlist.add(current);
    }

    public static List<Waitlist> getWaitlist(){
        return waitlist;
    }

    public static List<Waitlist> getWaitlistByTime(PlayTime time){
        List<Waitlist> results = new ArrayList<>();
        for(Waitlist current : waitlist){
            if(current.getPlayTime().equals(time)){
                results.add(current);
            }
        }
        return results;
    }

    public void RemoveFromWaitlist(MatchGroup group) {
        for(int i = 0; i < waitlist.size(); i++) {
            MatchGroup waitlistGroup = waitlist.get(i).getMatchGroup();
            if(waitlistGroup.equals(group)){
                waitlist.remove(i);
            }
        }
    }

}
