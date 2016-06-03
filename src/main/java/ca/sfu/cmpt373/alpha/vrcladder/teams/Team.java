package ca.sfu.cmpt373.alpha.vrcladder.teams;

import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.AttendanceCard;
import ca.sfu.cmpt373.alpha.vrcladder.users.User;
import ca.sfu.cmpt373.alpha.vrcladder.users.authorization.UserRole;
import ca.sfu.cmpt373.alpha.vrcladder.util.IdType;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.AttendanceStatus;

// TODO - Potentially add more fields
public class Team {

    private IdType id;
    private User firstPlayer;
    private User secondPlayer;
    private AttendanceCard attendanceCard;
    private AttendanceStatus attendanceStatus;
    private int ranking;

    public Team(IdType id,
                User firstPlayer,
                User secondPlayer,
                int ranking,
                AttendanceCard attendanceCard) {
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

    public void setRanking(int ranking){
        this.ranking = ranking;
    }

    public int getRanking() {
        return this.ranking;
    }

    public void setAttendance(AttendanceStatus attendance){
        this.attendanceStatus = attendance;
    }

    public AttendanceStatus getAttendace(){
        return this.attendanceStatus;
    }

    // TODO - Replace RuntimeException with more specific (possibly custom) type of error.
    private void verifyPlayers(User firstPlayer, User secondPlayer) throws RuntimeException {
        if (firstPlayer.getUserRole() != UserRole.PLAYER || secondPlayer.getUserRole() != UserRole.PLAYER) {
            throw new RuntimeException();
        }
    }

}
