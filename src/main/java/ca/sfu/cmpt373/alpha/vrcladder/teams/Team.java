package ca.sfu.cmpt373.alpha.vrcladder.teams;

import ca.sfu.cmpt373.alpha.vrcladder.persistence.PersistenceConstants;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.AttendanceCard;
import ca.sfu.cmpt373.alpha.vrcladder.users.User;
import ca.sfu.cmpt373.alpha.vrcladder.users.authorization.UserRole;
import ca.sfu.cmpt373.alpha.vrcladder.util.IdType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

// TODO - Replace RuntimeExceptions on verification methods with something more meaningful.
@Entity
@Table(name = PersistenceConstants.TABLE_TEAM)
public class Team {

    private IdType id;
    private User firstPlayer;
    private User secondPlayer;
    private AttendanceCard attendanceCard;
    private Integer ladderPosition;

    public Team() {
        // Required by Hibernate
    }

    public Team(User firstPlayer, int ranking, User secondPlayer, AttendanceCard attendanceCard) {
        verifyPlayer(firstPlayer);
        verifyPlayer(secondPlayer);
        this.id = new IdType();
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
        this.attendanceCard = attendanceCard;
        this.ladderPosition = ranking;
    }

    public Team(IdType id, User firstPlayer, int ranking, User secondPlayer, AttendanceCard attendanceCard) {
        verifyPlayer(firstPlayer);
        verifyPlayer(secondPlayer);
        this.id = id;
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
        this.attendanceCard = attendanceCard;
        this.ladderPosition = ranking;
    }

    @Id
    @Column(name = PersistenceConstants.COLUMN_ID)
    public String getId() {
        return id.toString();
    }

    public void setId(String newId) {
        id = new IdType(newId);
    }

    @ManyToOne
    @JoinColumn(name = PersistenceConstants.COLUMN_FIRST_PLAYER_ID, nullable = false)
    public User getFirstPlayer() {
        return firstPlayer;
    }


    public void setFirstPlayer(User player) {
        verifyPlayer(player);
        firstPlayer = player;
    }

    @ManyToOne
    @JoinColumn(name = PersistenceConstants.COLUMN_SECOND_PLAYER_ID, nullable = false)
    public User getSecondPlayer() {
        return secondPlayer;
    }

    public void setSecondPlayer(User player) {
        verifyPlayer(player);
        secondPlayer = player;
    }

    @OneToOne
    @JoinColumn(name = PersistenceConstants.COLUMN_ATTENDANCE_CARD_ID)
    public AttendanceCard getAttendanceCard() {
        return attendanceCard;
    }

    public void setAttendanceCard(AttendanceCard newAttendanceCard) {
        attendanceCard = newAttendanceCard;
    }

    @Column(name = PersistenceConstants.COLUMN_LADDER_POSITION, nullable = false)
    public Integer getLadderPosition() {
        return this.ladderPosition;
    }

    public void setLadderPosition(Integer position) {
        verifyLadderPosition(position);
        ladderPosition = position;
    }

    private void verifyPlayer(User player) throws RuntimeException {
        if (player.getUserRole() != UserRole.PLAYER) {
            throw new RuntimeException();
        }
    }

    private void verifyLadderPosition(Integer position) throws RuntimeException {
        if (position <= 0) {
            throw new RuntimeException();
        }
    }

}
