package ca.sfu.cmpt373.alpha.vrcladder.users.personal;

import ca.sfu.cmpt373.alpha.vrcladder.exceptions.ValidationException;
import org.junit.Assert;
import org.junit.Test;

public class PhoneNumberTest {

    private static final String VALID_PHONE_NUM_SIMPLE = "1234567890";
    private static final String VALID_PHONE_NUM_DASHES = "1-800-123-4567";
    private static final String VALID_PHONE_NUM_SPACES = "604 123 4567";
    private static final String VALID_PHONE_NUM_MIXED = "(604) 123-4567";

    private static final String VALID_PHONE_NUM_1_PROCESSED = VALID_PHONE_NUM_SIMPLE;
    private static final String VALID_PHONE_NUM_2_PROCESSED = "18001234567";
    private static final String VALID_PHONE_NUM_3_PROCESSED = "6041234567";
    private static final String VALID_PHONE_NUM_4_PROCESSED = VALID_PHONE_NUM_3_PROCESSED;

    private static final String INVALID_PHONE_NUM_LETTERS = "604-abc-1234";
    private static final String INVALID_PHONE_NUM_TOO_LONG = "604-1234-5678-91011";

    @Test
    public void testValidPhoneNumber() {
        PhoneNumber phoneNumber = new PhoneNumber(VALID_PHONE_NUM_SIMPLE);
        Assert.assertEquals(VALID_PHONE_NUM_1_PROCESSED, phoneNumber.getValue());
    }

    @Test
    public void testValidPhoneNumberWithDashes() {
        PhoneNumber phoneNumber = new PhoneNumber(VALID_PHONE_NUM_DASHES);
        Assert.assertEquals(VALID_PHONE_NUM_2_PROCESSED, phoneNumber.getValue());
    }

    @Test
    public void testValidPhoneNumberWithSpaces() {
        PhoneNumber phoneNumber = new PhoneNumber(VALID_PHONE_NUM_SPACES);
        Assert.assertEquals(VALID_PHONE_NUM_3_PROCESSED, phoneNumber.getValue());
    }

    @Test
    public void testValidPhoneNumberWithMixedCharcaters() {
        PhoneNumber phoneNumber = new PhoneNumber(VALID_PHONE_NUM_MIXED);
        Assert.assertEquals(VALID_PHONE_NUM_4_PROCESSED, phoneNumber.getValue());
    }

    @Test(expected = ValidationException.class)
    public void testInvalidPhoneNumberWithLetters() {
        PhoneNumber phoneNumber = new PhoneNumber(INVALID_PHONE_NUM_LETTERS);
    }

    @Test(expected = ValidationException.class)
    public void testInvalidLongPhoneNumber() {
        PhoneNumber phoneNumber = new PhoneNumber(INVALID_PHONE_NUM_TOO_LONG);
    }

}
