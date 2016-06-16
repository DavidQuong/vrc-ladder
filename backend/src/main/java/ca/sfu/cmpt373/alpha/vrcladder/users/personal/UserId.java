package ca.sfu.cmpt373.alpha.vrcladder.users.personal;

import ca.sfu.cmpt373.alpha.vrcladder.exceptions.ValidationException;
import ca.sfu.cmpt373.alpha.vrcladder.persistence.PersistenceConstants;
import ca.sfu.cmpt373.alpha.vrcladder.util.IdType;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Embeddable
public class UserId implements IdType {

    private static final String USER_ID_FORMAT_REGEX_PATTERN = "^[0-9]+$";
    private static final String USER_ID_FORMAT_ERROR_MSG = "%s is not a valid user ID.";

    private static final Pattern FORMAT_PATTERN = Pattern.compile(USER_ID_FORMAT_REGEX_PATTERN,
            Pattern.CASE_INSENSITIVE);

    private String id;

    private UserId() {
        // For Hibernate.
    }

    public UserId(String userId) {
        validateFormat(userId);
        id = userId;
    }

    @Column(name = PersistenceConstants.COLUMN_ID)
    @Override
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

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    private void validateFormat(String userId) throws ValidationException {
        Matcher matcher = FORMAT_PATTERN.matcher(userId);
        if (!matcher.find()) {
            String errorMsg = String.format(USER_ID_FORMAT_ERROR_MSG, userId);
            throw new ValidationException(errorMsg);
        }
    }

}
