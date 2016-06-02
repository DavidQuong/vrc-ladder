package ca.sfu.cmpt373.alpha.vrcladder.teams.attendance;

import ca.sfu.cmpt373.alpha.vrcladder.util.BuilderErrorConstants;
import ca.sfu.cmpt373.alpha.vrcladder.util.IdType;

public class AttendanceCardBuilder {

    private IdType id;
    private PlayTime preferredPlaytime;

    public AttendanceCardBuilder() {
        id = null;
        preferredPlaytime = null;
    }

    public AttendanceCardBuilder setId(IdType id) {
        this.id = id;

        return this;
    }

    public AttendanceCardBuilder setPreferredPlayTime(PlayTime playTime) {
        this.preferredPlaytime = playTime;

        return this;
    }

    public AttendanceCard buildAttendanceCard() {
        if (id == null) {
            throw new IllegalStateException(BuilderErrorConstants.ERROR_MSG_NULL_ID);
        }

        if (preferredPlaytime == null) {
            throw new IllegalStateException(BuilderErrorConstants.ERROR_MSG_NULL_PLAY_TIME);
        }

        AttendanceCard attendanceCard = new AttendanceCard();
        attendanceCard.setId(id.getId());
        attendanceCard.setPreferredPlayTime(preferredPlaytime);

        return attendanceCard;
    }

}
