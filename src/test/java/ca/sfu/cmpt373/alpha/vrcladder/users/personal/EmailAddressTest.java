package ca.sfu.cmpt373.alpha.vrcladder.users.personal;

import org.junit.Assert;
import org.junit.Test;

public class EmailAddressTest {

    private static final String VALID_EMAIL_1 = "validEmail@sfu.ca";
    private static final String VALID_EMAIL_2 = "valid@gmail.com";
    private static final String INVALID_EMAIL_1 = "invalidEmail";
    private static final String INVALID_EMAIL_2 = "invalid@sfu";

    @Test
    public void testValidEmailAddress1() {
        EmailAddress emailAddress = new EmailAddress(VALID_EMAIL_1);
        Assert.assertEquals(VALID_EMAIL_1, emailAddress.toString());
    }

    @Test
    public void testValidEmailAddress2() {
        EmailAddress emailAddress = new EmailAddress(VALID_EMAIL_2);
        Assert.assertEquals(VALID_EMAIL_2, emailAddress.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidEmail1() {
        EmailAddress emailAddress = new EmailAddress(INVALID_EMAIL_1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidEmail2() {
        EmailAddress emailAddress = new EmailAddress(INVALID_EMAIL_2);
    }

}
