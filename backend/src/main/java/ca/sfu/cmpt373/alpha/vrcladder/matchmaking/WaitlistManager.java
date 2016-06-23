package ca.sfu.cmpt373.alpha.vrcladder.matchmaking;


import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.logic.Waitlist;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.PlayTime;
import java.util.ArrayList;
import java.util.List;

public class WaitlistManager<T> {

    private List<Waitlist> waitlist;

    public void addToWaitlist(T content, PlayTime time){
        Waitlist<T> current = new Waitlist<>(content, time);
        waitlist.add(current);
    }

    public List<Waitlist> getWaitlist(){
        return waitlist;
    }

    public List<Waitlist> getWaitlistByTime(PlayTime time){
        List<Waitlist> results = new ArrayList<>();
        for(Waitlist current : waitlist){
            if(current.getPlayTime().equals(time)){
                results.add(current);
            }
        }
        return results;
    }

    public void RemoveFromWaitlist(T content) {
        for(int i = 0; i < waitlist.size(); i++) {
            Waitlist waitlistContent = waitlist.get(i);
            if(waitlistContent.equals(content)){
                waitlist.remove(i);
            }
        }
    }

}
