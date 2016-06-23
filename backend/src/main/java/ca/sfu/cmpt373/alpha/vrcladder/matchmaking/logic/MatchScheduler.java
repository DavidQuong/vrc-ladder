package ca.sfu.cmpt373.alpha.vrcladder.matchmaking.logic;

import ca.sfu.cmpt373.alpha.vrcladder.exceptions.MatchMakingException;
import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.Court;
import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.MatchGroup;
import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.WaitlistManager;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.PlayTime;

import java.util.ArrayList;
import java.util.List;

public class MatchScheduler {
    private static WaitlistManager<MatchGroup> waitListManager = new WaitlistManager();

    /**
     * @throws MatchMakingException if Courts are full, and matches cannot be scheduled
     */
    public static List<Court> scheduleMatches(int numCourts, List<MatchGroup> matchGroups) {
        // Schedule matches into the courts.
        List<Court> courts = new ArrayList<>();
        for (int i = 0; i < numCourts; i++) {
            courts.add(new Court());
        }

        for (MatchGroup matchGroup : matchGroups) {
            PlayTime preferredPlayTime = matchGroup.getPreferredGroupPlayTime();
            preferredPlayTime.checkPlayablePlayTime();

            //try to schedule a match group at their preferred time
            boolean isPreferredPlayTimeScheduled = false;
            for (Court court : courts) {
                if (court.isPlayTimeFree(preferredPlayTime)) {
                    court.scheduleMatch(matchGroup, preferredPlayTime);
                    isPreferredPlayTimeScheduled = true;
                    break;
                }
            }

            //if no courts have the available preferred play time for a group, choose another play time to schedule them in
            if (!isPreferredPlayTimeScheduled) {
                boolean isAnyPlayTimeScheduled = false;
                for (Court court : courts) {
                    for (PlayTime playTime : PlayTime.values()) {
                        if (playTime.isPlayable() && court.isPlayTimeFree(playTime)) {
                            court.scheduleMatch(matchGroup, playTime);
                            isAnyPlayTimeScheduled = true;
                            break;
                        }
                    }
                    if (isAnyPlayTimeScheduled) {
                        break;
                    }
                }
                if (!isAnyPlayTimeScheduled) {
                    waitListManager.addToWaitlist(matchGroup, preferredPlayTime);
                }
            }
        }
        return courts;
    }

    public WaitlistManager getWaitlist(){
        return waitListManager;
    }

}
