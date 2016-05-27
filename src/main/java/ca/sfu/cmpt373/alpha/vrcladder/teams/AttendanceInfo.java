package ca.sfu.cmpt373.alpha.vrcladder.teams;

/**
 * Created by Trevor on 5/26/2016.
 * A class to provide info on a team's attendance
 */
public class AttendanceInfo {
    private final boolean isAttending;
    private final TimeSlot preferedTimeSlot;
    private final AttendanceStatus attendanceStatus;

    public enum TimeSlot {
        FIRST, SECOND
    }

    public enum AttendanceStatus {
        ON_TIME,
        LATE,
        NO_SHOW
    }

    public AttendanceInfo (boolean isAttending, TimeSlot preferredTimeSlot, AttendanceStatus attendanceStatus) {
        this.isAttending = isAttending;
        this.preferedTimeSlot = preferredTimeSlot;
        this.attendanceStatus = attendanceStatus;
    }

    /**
     * @return whether or not the team has said that they are attending the next meet
     */
    public boolean getIsAttending() {
        return this.isAttending;
    }

    /**
     * @return the team's actual attendance status. i.e. whether or not they actually showed up to the last meeting
     */
    public AttendanceStatus getAttendanceStatus() {
        return attendanceStatus;
    }

    public TimeSlot getPreferedTimeSlot() {
        return this.preferedTimeSlot;
    }
}
