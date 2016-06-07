package ca.sfu.cmpt373.alpha.vrcladder.util;

import java.util.UUID;

public class IdType {

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

}
