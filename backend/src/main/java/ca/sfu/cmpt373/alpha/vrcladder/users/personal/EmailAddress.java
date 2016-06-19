package ca.sfu.cmpt373.alpha.vrcladder.users.personal;

import ca.sfu.cmpt373.alpha.vrcladder.exceptions.ValidationException;
import ca.sfu.cmpt373.alpha.vrcladder.persistence.PersistenceConstants;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Embeddable
public class EmailAddress {

    private static final String EMAIL_FORMAT_REGEX_PATTERN =  "^[A-Z0-9._-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
    private static final String EMAIL_FORMAT_ERROR_MSG = "%s is not a valid email address.";

    private static final Pattern FORMAT_PATTERN = Pattern.compile(EMAIL_FORMAT_REGEX_PATTERN,
        Pattern.CASE_INSENSITIVE);

    @Column(name = PersistenceConstants.COLUMN_EMAIL_ADDRESS, nullable = false, unique = true)
    private String emailAddress;

    private EmailAddress() {
        // Required by Hibernate.
    }

    public EmailAddress(String emailAddress) throws ValidationException {
        validateFormat(emailAddress);
        this.emailAddress = emailAddress;
    }

    public String getValue() {
        return emailAddress;
    }

    @Override
    public String toString() {
        return getValue();
    }

    @Override
    public boolean equals(Object otherObj) {
        if (this == otherObj) {
            return true;
        }

        if (otherObj == null || getClass() != otherObj.getClass()) {
            return false;
        }

        EmailAddress otherEmail = (EmailAddress) otherObj;

        return emailAddress.equals(otherEmail.emailAddress);
    }

    private void validateFormat(String emailAddress) throws ValidationException {
        Matcher matcher = FORMAT_PATTERN.matcher(emailAddress);
        if(!matcher.find()){
            String errorMsg = String.format(EMAIL_FORMAT_ERROR_MSG, emailAddress);
            throw new ValidationException(errorMsg);
        }
    }

}
