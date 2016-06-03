package ca.sfu.cmpt373.alpha.vrcladder.matchmaking;

import ca.sfu.cmpt373.alpha.vrcladder.exceptions.MatchMakingException;
import ca.sfu.cmpt373.alpha.vrcladder.exceptions.PlayTimeException;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.PlayTime;
import com.sun.istack.internal.NotNull;

import java.util.HashMap;
import java.util.Map;

public class Court {
    private static final String ERROR_MESSAGE_TIME_SLOT_FILLED = "Time slot is filled";
    private static final String ERROR_MESSAGE_NO_MATCH_SCHEDULED = "There is no match scheduled for this play time";

    private Map<PlayTime, MatchGroup> scheduledMatches = new HashMap<>();

    /**
     * @throws MatchMakingException if the time slot is filled
     * @throws PlayTimeException if the play time is unplayable
     */
    public void scheduleMatch(@NotNull MatchGroup matchGroup, @NotNull PlayTime playTime) {
        checkTimeSlotFilled(playTime);
        playTime.checkPlayablePlayTime();
        scheduledMatches.put(playTime, matchGroup);
    }

    /**
     * @throws PlayTimeException if the play time is unplayable
     */
    public boolean isPlayTimeFree(@NotNull PlayTime playTime) {
        playTime.checkPlayablePlayTime();
        return !scheduledMatches.containsKey(playTime);
    }

    /**
     * @throws PlayTimeException if the play time is unplayable
     */
    public MatchGroup getScheduledMatch(@NotNull PlayTime playTime) {
        boolean isMatchScheduled = !isPlayTimeFree(playTime);
        if (isMatchScheduled) {
            playTime.checkPlayablePlayTime();
            return scheduledMatches.get(playTime);
        } else {
            throw new MatchMakingException(ERROR_MESSAGE_NO_MATCH_SCHEDULED);
        }
    }

    public Map<PlayTime, MatchGroup> getScheduledMatches() {
        return new HashMap<>(scheduledMatches);
    }

    private void checkTimeSlotFilled(PlayTime playTime) {
        boolean isTimeSlotFilled = !isPlayTimeFree(playTime);
        if (isTimeSlotFilled) {
            throw new MatchMakingException(ERROR_MESSAGE_TIME_SLOT_FILLED);
        }
    }
}
