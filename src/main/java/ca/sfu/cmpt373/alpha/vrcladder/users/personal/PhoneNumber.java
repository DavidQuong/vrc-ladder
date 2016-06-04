package ca.sfu.cmpt373.alpha.vrcladder.users.personal;

import java.util.IllegalFormatException;

// TODO - Add verification of phone number format, i.e., 1-800-123-4567
//      - Implement Comparable interface
public class PhoneNumber {

    private String phoneNumber;

    public PhoneNumber(String phoneNumber) throws IllegalFormatException {
        verifyFormat(phoneNumber);
        this.phoneNumber = phoneNumber;
    }

    @Override
    public String toString() {
        return phoneNumber;
    }

    private void verifyFormat(String phoneNumber) throws IllegalFormatException {

    }
}
