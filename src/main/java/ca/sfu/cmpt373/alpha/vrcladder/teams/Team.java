package ca.sfu.cmpt373.alpha.vrcladder.teams;

import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.AttendanceCard;
import ca.sfu.cmpt373.alpha.vrcladder.users.User;
import ca.sfu.cmpt373.alpha.vrcladder.users.authorization.UserRole;
import ca.sfu.cmpt373.alpha.vrcladder.util.IdType;

// TODO - Potentially add more fields
public class Team {

    private IdType id;
    private User firstPlayer;
    private User secondPlayer;
    private AttendanceCard attendanceCard;
    private int ranking;

    public Team(IdType id, User firstPlayer, int ranking, User secondPlayer, AttendanceCard attendanceCard) {
        verifyPlayers(firstPlayer, secondPlayer);
        this.id = id;
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
        this.attendanceCard = attendanceCard;
        this.ranking = ranking;
    }

    public IdType getId() {
        return id;
    }

    public User getFirstPlayer() {
        return firstPlayer;
    }

    public User getSecondPlayer() {
        return secondPlayer;
    }

    public AttendanceCard getAttendanceCard() {
        return attendanceCard;
    }

    public int getRanking() {
        return this.ranking;
    }

    // TODO - Replace RuntimeException with more specific (possibly custom) type of error.
    private void verifyPlayers(User firstPlayer, User secondPlayer) throws RuntimeException {
        if (firstPlayer.getUserRole() != UserRole.PLAYER || secondPlayer.getUserRole() != UserRole.PLAYER) {
            throw new RuntimeException();
        }
    }

}
