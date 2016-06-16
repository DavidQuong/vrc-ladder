package ca.sfu.cmpt373.alpha.vrcladder.util;

import ca.sfu.cmpt373.alpha.vrcladder.persistence.PersistenceConstants;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.UUID;

@Embeddable
public class GeneratedId implements IdType {

    private String id;

    public GeneratedId() {
        id = UUID.randomUUID().toString();
    }

    public GeneratedId(String id) {
        this.id = id;
    }

    @Column (name = PersistenceConstants.COLUMN_ID)
    public String getId() {
        return id;
    }

    @Override
    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return getId();
    }

    @Override
    public boolean equals(Object otherObj) {
        if (this == otherObj) {
            return true;
        }

        if (otherObj == null || getClass() != otherObj.getClass()) {
            return false;
        }

        GeneratedId otherIdType = (GeneratedId) otherObj;

        return id.equals(otherIdType.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

}
