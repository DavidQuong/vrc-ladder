package ca.sfu.cmpt373.alpha.vrcladder.util;

import ca.sfu.cmpt373.alpha.vrcladder.persistence.PersistenceConstants;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.UUID;

@Embeddable
public class IdType implements Identifier {

    @Column (name = PersistenceConstants.COLUMN_ID)
    private String id;

    public IdType() {
        id = UUID.randomUUID().toString();
    }

    public IdType(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
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

        IdType otherIdType = (IdType) otherObj;

        return id.equals(otherIdType.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

}
