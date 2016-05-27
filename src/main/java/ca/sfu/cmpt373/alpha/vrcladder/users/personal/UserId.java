package ca.sfu.cmpt373.alpha.vrcladder.users.personal;

// TODO - Implement Comparable interface
public class UserId {

    private Integer id;

    public UserId(Integer userId) {
        id = userId;
    }

    public Integer getUserId() {
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

}
