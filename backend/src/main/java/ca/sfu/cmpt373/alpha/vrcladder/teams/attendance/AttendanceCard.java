package ca.sfu.cmpt373.alpha.vrcladder.teams.attendance;

import ca.sfu.cmpt373.alpha.vrcladder.persistence.PersistenceConstants;
import ca.sfu.cmpt373.alpha.vrcladder.util.GeneratedId;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

@Entity
@Table(name = PersistenceConstants.TABLE_ATTENDANCE_CARD)
public class AttendanceCard {

    private static final PlayTime DEFAULT_PLAYTIME = PlayTime.NONE;

    private GeneratedId id;
    private PlayTime preferredPlayTime;

    public AttendanceCard() {
        this.id = new GeneratedId();
        this.preferredPlayTime = DEFAULT_PLAYTIME;
    }

    @EmbeddedId
    public GeneratedId getId() {
        return id;
    }

    public void setId(GeneratedId newId) {
        id = newId;
    }

    @Enumerated(EnumType.STRING)
    @Column(name = PersistenceConstants.COLUMN_PLAY_TIME, nullable = false)
    public PlayTime getPreferredPlayTime() {
        return preferredPlayTime;
    }

    public void setPreferredPlayTime(PlayTime playTime) {
        preferredPlayTime = playTime;
    }

    @Transient
    public boolean isAttending() {
        return  (preferredPlayTime != PlayTime.NONE);
    }

    @Override
    public boolean equals(Object otherObj) {
        if (this == otherObj) {
            return true;
        }

        if (otherObj == null || getClass() != otherObj.getClass()) {
            return false;
        }

        AttendanceCard otherAttendanceCard = (AttendanceCard) otherObj;

        return id.equals(otherAttendanceCard.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

}