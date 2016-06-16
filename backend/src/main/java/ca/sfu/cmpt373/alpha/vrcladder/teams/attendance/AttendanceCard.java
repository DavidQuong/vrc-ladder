package ca.sfu.cmpt373.alpha.vrcladder.teams.attendance;

import ca.sfu.cmpt373.alpha.vrcladder.persistence.PersistenceConstants;
import ca.sfu.cmpt373.alpha.vrcladder.util.IdType;

import javax.persistence.Column;
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

    private IdType id;
    private PlayTime preferredPlayTime;
    private AttendanceStatus attendStatus;

    public AttendanceCard() {
        this.id = new IdType();
        this.preferredPlayTime = DEFAULT_PLAYTIME;
    }

    @Id
    @Column(name = PersistenceConstants.COLUMN_ID)
    public String getId() {
        return id.getId();
    }

    public void setId(String newId) {
        id = new IdType(newId);
    }

    @Enumerated(EnumType.STRING)
    @Column(name = PersistenceConstants.COLUMN_PLAY_TIME, nullable = false)
    public PlayTime getPreferredPlayTime() {
        return preferredPlayTime;
    }

    public void setPreferredPlayTime(PlayTime playTime) {
        preferredPlayTime = playTime;
    }

    public void setAttendanceStatus(AttendanceStatus status) {
        this.attendStatus = status;
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

    public boolean late(){
        switch(attendStatus){
            case LATE:
                return true;
        }
        return false;
    }

    public boolean noShow(){
        switch(attendStatus){
            case NO_SHOW:
            return true;
    }

        return false;}
    public boolean attended(){
        switch(attendStatus){
            case PRESENT:
            return true;
        }
        return false;
    }


}