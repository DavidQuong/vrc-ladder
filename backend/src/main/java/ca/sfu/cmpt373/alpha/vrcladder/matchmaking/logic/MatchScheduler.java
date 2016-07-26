package ca.sfu.cmpt373.alpha.vrcladder.matchmaking.logic;

import ca.sfu.cmpt373.alpha.vrcladder.exceptions.MatchMakingException;
import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.Court;
import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.MatchGroup;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.PlayTime;
import com.fasterxml.jackson.databind.deser.DataFormatReaders;

import java.util.ArrayList;
import java.util.List;

public class MatchScheduler {
    private static final String ERROR_MESSAGE_COURTS_FULL = "There were not enough courts available to schedule all matches";
    private static final String ERROR_MESSAGE_NOT_ATTENDING = "A team in a group has indicated they are not attending.";

    /**
     * @throws MatchMakingException if Courts are full, and matches cannot be scheduled
     */
    public static List<Court> scheduleMatches(List<MatchGroup> matchGroups) {
        // Schedule matches into the courts.
        List<Court> courts = new ArrayList<>();
        for (int i = 0; i < matchGroups.size(); i++) {
            courts.add(new Court());
        }

        if(matchGroups.size() < 13) {
            assert(matchGroups.size() == courts.size());

            for(int index = 0;index < matchGroups.size();index++) {
                courts.get(index).scheduleMatch(matchGroups.get(index), PlayTime.TIME_SLOT_A);
            }
        } else {
            List<MatchGroup> slotAGroups = new ArrayList<>();
            List<MatchGroup> slotBGroups = new ArrayList<>();

            for(MatchGroup matchGroup : matchGroups) {
                if(matchGroup.getPreferredGroupPlayTime() == PlayTime.TIME_SLOT_A) {
                    slotAGroups.add(matchGroup);
                } else if(matchGroup.getPreferredGroupPlayTime() == PlayTime.TIME_SLOT_B) {
                    slotBGroups.add(matchGroup);
                } else {
                    throw new MatchMakingException(ERROR_MESSAGE_NOT_ATTENDING);
                }
            }
        }

        /*for (MatchGroup matchGroup : matchGroups) {
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
                    throw new MatchMakingException(ERROR_MESSAGE_COURTS_FULL);
                }
            }
        }*/
        return courts;
    }
}
