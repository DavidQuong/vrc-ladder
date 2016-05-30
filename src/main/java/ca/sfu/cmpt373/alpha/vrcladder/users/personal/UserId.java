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
    public boolean equals(Object comparedObj) {
        if (this == comparedObj) {
            return true;
        }

        if (comparedObj == null || getClass() != comparedObj.getClass()) {
            return false;
        }

        UserId otherId = (UserId) comparedObj;

        if (id != otherId.id) {
            return false;
        }

        return true;
    }

    private void verifyFormat(String userId) throws IllegalFormatException {

    }

}
