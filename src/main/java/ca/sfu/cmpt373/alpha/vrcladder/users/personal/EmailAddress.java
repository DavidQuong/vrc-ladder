package ca.sfu.cmpt373.alpha.vrcladder.users.personal;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailAddress {

    private static final String EMAIL_FORMAT_REGEX_PATTERN =  "^[A-Z0-9._-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$";
    private static final String EMAIL_FORMAT_ERROR_MSG = "%s is not a valid email address.";

    private static final Pattern FORMAT_PATTERN = Pattern.compile(EMAIL_FORMAT_REGEX_PATTERN,
        Pattern.CASE_INSENSITIVE);

    private String emailAddress;

    public EmailAddress(String emailAddress) throws IllegalArgumentException {
        verifyFormat(emailAddress);
        this.emailAddress = emailAddress;
    }

    public String getEmailAddress() {
        return emailAddress;
    }

    @Override
    public String toString() {
        return getEmailAddress();
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

    private void verifyFormat(String emailAddress) throws IllegalArgumentException {
        Matcher matcher = FORMAT_PATTERN.matcher(emailAddress);
        if(!matcher.find()){
            String errorMsg = String.format(EMAIL_FORMAT_ERROR_MSG, emailAddress);
            throw new IllegalArgumentException(errorMsg);
        }
    }
}
