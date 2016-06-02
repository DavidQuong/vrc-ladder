package ca.sfu.cmpt373.alpha.vrcladder.teams;

import ca.sfu.cmpt373.alpha.vrcladder.persistence.PersistenceConstants;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.AttendanceCard;
import ca.sfu.cmpt373.alpha.vrcladder.users.User;
import ca.sfu.cmpt373.alpha.vrcladder.util.IdType;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

// TODO - Ensure uniqueness of team (no team with same two players, i.e., in reverse order)
@Entity
@Table(name = PersistenceConstants.TABLE_TEAM)
public class Team {

    private IdType id;
    private User firstPlayer;
    private User secondPlayer;
    private AttendanceCard attendanceCard;

    public Team() {
        // Required by Hibernate
    }

    @Id
    @Column(name = PersistenceConstants.COLUMN_ID)
    public String getId() {
        return id.getId();
    }

    public void setId(String newId) {
        id = new IdType(newId);
    }

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = PersistenceConstants.COLUMN_FIRST_PLAYER_ID, nullable = false)
    public User getFirstPlayer() {
        return firstPlayer;
    }

    public void setFirstPlayer(User player) {
        firstPlayer = player;
    }

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = PersistenceConstants.COLUMN_SECOND_PLAYER_ID, nullable = false)
    public User getSecondPlayer() {
        return secondPlayer;
    }

    public void setSecondPlayer(User player) {
        secondPlayer = player;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = PersistenceConstants.COLUMN_ATTENDANCE_CARD_ID, nullable = false)
    public AttendanceCard getAttendanceCard() {
        return attendanceCard;
    }

    public void setAttendanceCard(AttendanceCard newAttendanceCard) {
        attendanceCard = newAttendanceCard;
    }

}
