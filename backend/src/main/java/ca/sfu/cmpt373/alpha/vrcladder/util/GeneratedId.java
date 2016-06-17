package ca.sfu.cmpt373.alpha.vrcladder.util;

import ca.sfu.cmpt373.alpha.vrcladder.persistence.PersistenceConstants;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.UUID;

@Embeddable
public class GeneratedId implements IdType {

    @Column (name = PersistenceConstants.COLUMN_ID)
    private String id;

    public GeneratedId() {
        id = UUID.randomUUID().toString();
    }

    public GeneratedId(String id) {
        this.id = id;
    }

    @Override
    public String getId() {
        return id;
    }

    private void setId(String id) {
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
