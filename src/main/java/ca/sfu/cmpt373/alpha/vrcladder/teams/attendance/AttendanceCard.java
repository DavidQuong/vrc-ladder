package ca.sfu.cmpt373.alpha.vrcladder.teams.attendance;

import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
import ca.sfu.cmpt373.alpha.vrcladder.util.IdType;

import static ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.PlayTime.*;

public class AttendanceCard {

    private IdType id;
    private Team team;
    private PlayTime preferredPlayTime;
    private boolean isAttending;

    public AttendanceCard(IdType id, Team team, PlayTime playTime) {
        this.id = id;
        this.team = team;

        updateAttendance(playTime);
        this.preferredPlayTime = playTime;
    }

    public Team getTeam() {
        return team;
    }

    public PlayTime getPreferredPlayTime() {
        return preferredPlayTime;
    }

    public void setPreferredPlayTime(PlayTime playTime) {
        updateAttendance(playTime);
        preferredPlayTime = playTime;
    }

    public boolean isAttending() {
        return isAttending;
    }

    private void updateAttendance(PlayTime playTime) {
        if (playTime == null) {
            // TODO - Provide more descriptive error message.
            throw new NullPointerException();
        } else if (playTime == NONE) {
            isAttending = false;
        } else {
            isAttending = true;
        }
    }

}
