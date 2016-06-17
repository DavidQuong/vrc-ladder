package ca.sfu.cmpt373.alpha.vrcladder.teams;

import ca.sfu.cmpt373.alpha.vrcladder.persistence.PersistenceConstants;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.AttendanceCard;
import ca.sfu.cmpt373.alpha.vrcladder.users.User;
import ca.sfu.cmpt373.alpha.vrcladder.util.GeneratedId;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = PersistenceConstants.TABLE_TEAM)
public class Team {

    private GeneratedId id;

    private AttendanceCard attendanceCard;
    private User firstPlayer;
    private User secondPlayer;
    private LadderPosition ladderPosition;

    private Team() {
        // Required by Hibernate.
    }

    public Team(User firstPlayer, User secondPlayer, LadderPosition ladderPosition) {
        this.id = new GeneratedId();
        this.attendanceCard = new AttendanceCard();
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
        this.ladderPosition = ladderPosition;
    }

    public Team(User firstPlayer, User secondPlayer, int position) {
        this.id = new GeneratedId();
        this.attendanceCard = new AttendanceCard();
        this.firstPlayer = firstPlayer;
        this.secondPlayer = secondPlayer;
        this.ladderPosition = new LadderPosition(position);
    }

    @EmbeddedId
    public GeneratedId getId() {
        return id;
    }

    public void setId(GeneratedId id) {
        this.id = id;
    }

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = PersistenceConstants.COLUMN_ATTENDANCE_CARD_ID, nullable = false)
    public AttendanceCard getAttendanceCard() {
        return attendanceCard;
    }

    private void setAttendanceCard(AttendanceCard newAttendanceCard) {
        attendanceCard = newAttendanceCard;
    }

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = PersistenceConstants.COLUMN_FIRST_PLAYER_ID, nullable = false)
    public User getFirstPlayer() {
        return firstPlayer;
    }

    private void setFirstPlayer(User player) {
        firstPlayer = player;
    }

    @ManyToOne(cascade = CascadeType.REFRESH)
    @JoinColumn(name = PersistenceConstants.COLUMN_SECOND_PLAYER_ID, nullable = false)
    public User getSecondPlayer() {
        return secondPlayer;
    }

    private void setSecondPlayer(User player) {
        secondPlayer = player;
    }

    @Embedded
    public LadderPosition getLadderPosition() {
        return ladderPosition;
    }

    public void setLadderPosition(LadderPosition ladderPosition) {
        this.ladderPosition = ladderPosition;
    }

    @Override
    public boolean equals(Object otherObj) {
        if (this == otherObj) {
            return true;
        }

        if (otherObj == null || getClass() != otherObj.getClass()) {
            return false;
        }

        Team otherTeam = (Team) otherObj;

        return id.equals(otherTeam.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

}