package ca.sfu.cmpt373.alpha.vrcladder.matchmaking;


import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.logic.Waitlist;
import java.util.List;

public class WaitlistManager<T> {

    private List<Waitlist> waitlist;

    public void addToWaitlist(T content){
        Waitlist<T> current = new Waitlist<>(content);
        waitlist.add(current);
    }

    public List<Waitlist> getWaitlist(){
        return waitlist;
    }

    public void RemoveFromWaitlist(T content) {
        for(int i = 0; i < waitlist.size(); i++) {
            Waitlist waitListContent = waitlist.get(i);
            if(waitListContent.equals(content)){
                waitlist.remove(i);
            }
        }
    }

}
