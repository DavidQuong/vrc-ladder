package ca.sfu.cmpt373.alpha.vrcladder.users.personal;

import java.util.IllegalFormatException;

// TODO - Add verification of user ID format.
public class UserId {

    private String id;

    public UserId(String userId) {
        verifyFormat(userId);
        id = userId;
    }

    public String getUserId() {
        return id;
    }

    @Override
    public String toString() {
        return getUserId();
    }

    @Override
    public boolean equals(Object comparedObj) {
        if (this == comparedObj) {
            return true;
        }

        if (comparedObj == null || getClass() != comparedObj.getClass()) {
            return false;
        }

        UserId otherUserId = (UserId) comparedObj;

        return id.equals(otherUserId.id);
    }

    private void verifyFormat(String userId) throws IllegalFormatException {

    }

}
