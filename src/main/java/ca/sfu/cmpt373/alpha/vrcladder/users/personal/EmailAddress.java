package ca.sfu.cmpt373.alpha.vrcladder.users.personal;

import java.util.IllegalFormatException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//      - Implement Comparable interface
public class EmailAddress {

    private String emailAddress;
    private static final String REGEX_PATTERN =  "^[A-Z0-9._-]+" +
                                                 "@[A-Z0-9.-]+" +
                                                 "\\.[A-Z]{2,6}$";

    public void setEmailAddress(String emailAddress) throws IllegalFormatException {
        verifyFormat(emailAddress);
        this.emailAddress = emailAddress;
    }

    public String getEmailAddress() {
        return this.emailAddress;
    }

    @Override
    public String toString() {
        return emailAddress;
    }

    public void verifyFormat(String emailAddress) {
        Pattern pattern = Pattern.compile(REGEX_PATTERN, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(emailAddress);
        if(!matcher.find()){
            throw new IllegalArgumentException("Email format is not correct");
        }
    }
}
