package ca.sfu.cmpt373.alpha.vrcladder.users.personal;

import ca.sfu.cmpt373.alpha.vrcladder.exceptions.ValidationException;
import org.apache.commons.lang3.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneNumber {

    private static final String REPLACED_CHARACTERS_REGEX_PATTERN = "[- \\(\\)]+";
    private static final String PHONE_NUM_FORMAT_REGEX_PATTERN = "^[0-9]{10,11}$";
    private static final String PHONE_NUM_FORMAT_ERROR_MSG = "%s is not a valid phone number.";

    private static final Pattern FORMAT_PATTERN = Pattern.compile(PHONE_NUM_FORMAT_REGEX_PATTERN,
        Pattern.CASE_INSENSITIVE);

    private String phoneNumber;

    public PhoneNumber(String phoneNumber) throws ValidationException {
        String strippedPhoneNumber = phoneNumber.replaceAll(REPLACED_CHARACTERS_REGEX_PATTERN, StringUtils.EMPTY);
        validateFormat(strippedPhoneNumber);
        this.phoneNumber = strippedPhoneNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    @Override
    public String toString() {
        return getPhoneNumber();
    }

    @Override
    public boolean equals(Object otherObj) {
        if (this == otherObj) {
            return true;
        }

        if (otherObj == null || getClass() != otherObj.getClass()) {
            return false;
        }

        PhoneNumber otherPhoneNumber = (PhoneNumber) otherObj;

        return phoneNumber.equals(otherPhoneNumber.phoneNumber);
    }

    private void validateFormat(String phoneNumber) throws ValidationException {
        Matcher matcher = FORMAT_PATTERN.matcher(phoneNumber);
        if(!matcher.find()){
            String errorMsg = String.format(PHONE_NUM_FORMAT_ERROR_MSG, phoneNumber);
            throw new ValidationException(errorMsg);
        }
    }

}
