package ca.sfu.cmpt373.alpha.vrcladder.exceptions;

import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.PlayTime;

/**
 * An exception that is thrown when attempting to schedule a match on a court in a time slot that's already booked
 */
public class TimeSlotFilledException extends RuntimeException {
    public TimeSlotFilledException(PlayTime playTime) {
        super("Time slot " + playTime + " is already booked on this court");
    }

    public TimeSlotFilledException(String message) {
        super(message);
    }
}
