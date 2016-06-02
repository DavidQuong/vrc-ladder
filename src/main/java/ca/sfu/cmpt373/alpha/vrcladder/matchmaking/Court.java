package ca.sfu.cmpt373.alpha.vrcladder.matchmaking;

import ca.sfu.cmpt373.alpha.vrcladder.exceptions.NoMatchScheduledException;
import ca.sfu.cmpt373.alpha.vrcladder.exceptions.TimeSlotFilledException;
import ca.sfu.cmpt373.alpha.vrcladder.exceptions.UnplayablePlayTimeException;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.PlayTime;
import com.sun.istack.internal.NotNull;

import java.util.HashMap;
import java.util.Map;

public class Court {
    private Map<PlayTime, MatchGroup> scheduledMatches = new HashMap<>();

    public void scheduleMatch(@NotNull MatchGroup matchGroup, @NotNull PlayTime playTime) {
        checkTimeSlotFilled(playTime);
        checkPlayablePlayTime(playTime);
        scheduledMatches.put(playTime, matchGroup);
    }

    public boolean isPlayTimeFree(@NotNull PlayTime playTime) {
        checkPlayablePlayTime(playTime);
        return !scheduledMatches.containsKey(playTime);
    }

    public MatchGroup getScheduledMatch(@NotNull PlayTime playTime) {
        boolean isMatchScheduled = !isPlayTimeFree(playTime);
        if (isMatchScheduled) {
            checkPlayablePlayTime(playTime);
            return scheduledMatches.get(playTime);
        } else {
            throw new NoMatchScheduledException(playTime);
        }
    }

    public Map<PlayTime, MatchGroup> getScheduledMatches() {
        return new HashMap<>(scheduledMatches);
    }

    private void checkTimeSlotFilled(PlayTime playTime) {
        boolean isTimeSlotFilled = !isPlayTimeFree(playTime);
        if (isTimeSlotFilled) {
            throw new TimeSlotFilledException(playTime);
        }

    }

    private void checkPlayablePlayTime(@NotNull PlayTime playTime) {
        if (!playTime.isPlayable()) {
            throw new UnplayablePlayTimeException(playTime);
        }
    }
}
