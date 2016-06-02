package ca.sfu.cmpt373.alpha.vrcladder.teams;

import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.AttendanceCard;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.AttendanceCardBuilder;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.PlayTime;
import ca.sfu.cmpt373.alpha.vrcladder.users.User;
import ca.sfu.cmpt373.alpha.vrcladder.users.authorization.UserRole;
import ca.sfu.cmpt373.alpha.vrcladder.util.BuilderErrorConstants;
import ca.sfu.cmpt373.alpha.vrcladder.util.IdType;

public class TeamBuilder {

    private IdType id;
    private User firstPlayer;
    private User secondPlayer;
    private AttendanceCard attendanceCard;

    public TeamBuilder() {
        id = new IdType();
        attendanceCard = new AttendanceCardBuilder()
            .setId(new IdType())
            .setPreferredPlayTime(PlayTime.NONE)
            .buildAttendanceCard();
    }

    public TeamBuilder setFirstPlayer(User firstPlayer) {
        this.firstPlayer = firstPlayer;

        return this;
    }

    public TeamBuilder setSecondPlayer(User secondPlayer) {
        this.secondPlayer = secondPlayer;

        return this;
    }

    public Team buildTeam() {
        if (id == null) {
            throw new IllegalStateException(BuilderErrorConstants.ERROR_MSG_NULL_USER_ID);
        }

        if (firstPlayer == null) {
            throw new IllegalStateException(BuilderErrorConstants.ERROR_MSG_NULL_FIRST_PLAYER);
        }

        if (secondPlayer == null) {
            throw new IllegalStateException(BuilderErrorConstants.ERROR_MSG_NULL_SECOND_PLAYER);
        }

        if (attendanceCard == null) {
            throw new IllegalStateException(BuilderErrorConstants.ERROR_MSG_NULL_ATTENDANCE_CARDS);
        }

        if (firstPlayer.getUserId().equals(secondPlayer.getUserId())) {
            throw new IllegalStateException(BuilderErrorConstants.ERROR_MSG_IDENTICAL_PLAYER);
        }

        if (firstPlayer.getUserRole() != UserRole.PLAYER) {
            throw new IllegalStateException(BuilderErrorConstants.ERROR_MSG_NON_PLAYER_FIRST);
        }

        if (secondPlayer.getUserRole() != UserRole.PLAYER) {
            throw new IllegalStateException(BuilderErrorConstants.ERROR_MSG_NON_PLAYER_SECOND);
        }

        Team newTeam = new Team();
        newTeam.setId(id.getId());
        newTeam.setFirstPlayer(firstPlayer);
        newTeam.setSecondPlayer(secondPlayer);
        newTeam.setAttendanceCard(attendanceCard);

        return newTeam;
    }

}
