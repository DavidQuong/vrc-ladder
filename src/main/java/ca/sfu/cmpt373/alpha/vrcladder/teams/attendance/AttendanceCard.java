package ca.sfu.cmpt373.alpha.vrcladder.teams.attendance;

import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
import ca.sfu.cmpt373.alpha.vrcladder.util.IdType;

import static ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.PlayTime.*;

public class AttendanceCard {

    private IdType id;
    private PlayTime preferredPlayTime;

    public AttendanceCard(IdType id, PlayTime playTime) {
        this.id = id;
        this.preferredPlayTime = playTime;
    }

    public PlayTime getPreferredPlayTime() {
        return preferredPlayTime;
    }

    public void setPreferredPlayTime(PlayTime playTime) {
        preferredPlayTime = playTime;
    }

    public boolean isAttending() {
        return  preferredPlayTime != PlayTime.NONE;
    }
}
