package ca.sfu.cmpt373.alpha.vrcladder.users.personal;

import java.util.IllegalFormatException;

// TODO - Add verification of email format, such that its of the form: <email name>@<domain name><top level domain>
//      - Implement Comparable interface
public class EmailAddress {

    private String emailAddress;

    public EmailAddress(String emailAddress) throws IllegalFormatException {
        verifyFormat(emailAddress);
        this.emailAddress = emailAddress;
    }

    @Override
    public String toString() {
        return emailAddress;
    }

    private void verifyFormat(String emailAddress) throws IllegalFormatException {

    }
}
