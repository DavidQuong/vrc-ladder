package ca.sfu.cmpt373.alpha.vrcladder.users.personal;

import org.apache.commons.lang3.StringUtils;

import java.util.IllegalFormatException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// TODO - Add verification of phone number format, i.e., 1-800-123-4567
public class PhoneNumber {

    private static final String REPLACED_CHARACTERS_REGEX = "[- \\(\\)]+";
    private static final String PHONE_REGEX_PATTERN = "^[0-9]{10,11}$";
    private static final String PHONE_ERROR_MSG_FORMAT = "%s is not a valid phone number.";

    private String phoneNumber;

    public PhoneNumber(String phoneNumber) throws IllegalFormatException {
        String strippedPhoneNumber = phoneNumber.replaceAll(REPLACED_CHARACTERS_REGEX, StringUtils.EMPTY);
        verifyFormat(strippedPhoneNumber);
        this.phoneNumber = strippedPhoneNumber;
    }

    @Override
    public String toString() {
        return phoneNumber;
    }

    private void verifyFormat(String phoneNumber) throws IllegalFormatException {
        Pattern pattern = Pattern.compile(PHONE_REGEX_PATTERN, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(phoneNumber);
        if(!matcher.find()){
            String errorMsg = String.format(PHONE_ERROR_MSG_FORMAT, phoneNumber);
            throw new IllegalArgumentException(errorMsg);
        }
    }

}
