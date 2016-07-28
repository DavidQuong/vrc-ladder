package ca.sfu.cmpt373.alpha.vrcladder.matchmaking.logic;

import ca.sfu.cmpt373.alpha.vrcladder.exceptions.MatchMakingException;
import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.Court;
import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.MatchGroup;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.PlayTime;

import java.util.ArrayList;
import java.util.List;

public class MatchScheduler {
    private static final String ERROR_MESSAGE_NOT_ATTENDING = "A group has indicated they are not attending.";

    /**
     * @throws MatchMakingException if a group has indicated they are not attending
     */
    public static List<Court> scheduleMatches(List<MatchGroup> matchGroups) {
        // Schedule matches into the courts.
        List<Court> courts = new ArrayList<>();
        for(int i = 0;i < matchGroups.size();i++) {
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

            while(slotAGroups.size() - slotBGroups.size() > 1) {
                slotBGroups.add(slotAGroups.remove(slotAGroups.size() - 1));
            }

            while(slotBGroups.size() - slotAGroups.size() > 1) {
                slotAGroups.add(slotBGroups.remove(slotBGroups.size() - 1));
            }

            for(int i = 0;i < slotAGroups.size();i++) {
                courts.get(i).scheduleMatch(slotAGroups.get(i), PlayTime.TIME_SLOT_A);
            }

            for(int i = 0;i < slotBGroups.size();i++) {
                courts.get(i).scheduleMatch(slotBGroups.get(i), PlayTime.TIME_SLOT_B);
            }
        }

        return courts;
    }
}
