package ca.sfu.cmpt373.alpha.vrcladder.matchmaking;

import ca.sfu.cmpt373.alpha.vrcladder.exceptions.MatchMakingException;
import ca.sfu.cmpt373.alpha.vrcladder.exceptions.PlayTimeException;
import ca.sfu.cmpt373.alpha.vrcladder.persistence.PersistenceConstants;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.PlayTime;
import ca.sfu.cmpt373.alpha.vrcladder.util.GeneratedId;
import ca.sfu.cmpt373.alpha.vrcladder.util.IdType;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashMap;
import java.util.Map;

/**
 * A class used for scheduling matches on a court in a particular time slot
 * essentially just a mapping between a PlayTime and a MatchGroup
 */
@Entity
@Table(name = PersistenceConstants.TABLE_COURT)
public class Court {

    private static final String ERROR_MESSAGE_TIME_SLOT_FILLED = "Time slot is filled";
    private static final String ERROR_MESSAGE_NO_MATCH_SCHEDULED = "There is no match scheduled for this play time";

    @EmbeddedId
    private GeneratedId id;

    @OneToMany (fetch = FetchType.EAGER)
    private Map<PlayTime, MatchGroup> scheduledMatches = new HashMap<>();

    public Court() {
        id = new GeneratedId();
    }

    /**
     * @throws MatchMakingException if the time slot is filled
     * @throws PlayTimeException if the play time is unplayable
     */
    public void scheduleMatch(MatchGroup matchGroup, PlayTime playTime) {
        checkTimeSlotFilled(playTime);
        playTime.checkPlayablePlayTime();
        scheduledMatches.put(playTime, matchGroup);
    }

    /**
     * @throws PlayTimeException if the play time is unplayable
     */
    public boolean isPlayTimeFree(PlayTime playTime) {
        playTime.checkPlayablePlayTime();
        return !scheduledMatches.containsKey(playTime);
    }

    /**
     * @throws PlayTimeException if the play time is unplayable
     */
    public MatchGroup getScheduledMatch(PlayTime playTime) {
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

    public IdType getId() {
        return id;
    }

    @Override
    public boolean equals(Object otherObj) {
        if (this == otherObj) {
            return true;
        }

        if (otherObj == null || getClass() != otherObj.getClass()) {
            return false;
        }

        Court court = (Court) otherObj;

        return id.equals(court.id);

    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
