package ca.sfu.cmpt373.alpha.vrcladder.users.personal;

import ca.sfu.cmpt373.alpha.vrcladder.exceptions.ValidationException;
import org.junit.Assert;
import org.junit.Test;

public class UserIdTest {

    private String VALID_USER_ID = "6642";
    private String VALID_USER_ID_LONGER = "10000";

    private String INVALID_USER_ID_LETTERS = "45A21H";
    private String INVALID_USER_ID_SPECIAL_CHAR = "66$42";

    @Test
    public void testValidUserId() {
        UserId userId = new UserId(VALID_USER_ID);
        Assert.assertEquals(VALID_USER_ID, userId.getUserId());
    }

    @Test
    public void testLongerValidUserId() {
        UserId userId = new UserId(VALID_USER_ID_LONGER);
        Assert.assertEquals(VALID_USER_ID_LONGER, userId.getUserId());
    }

    @Test(expected = ValidationException.class)
    public void testInvalidUserIdWithLetters() {
        UserId userId = new UserId(INVALID_USER_ID_LETTERS);
    }

    @Test(expected = ValidationException.class)
    public void testInvalidUserIdWithSpecialCharacter() {
        UserId userId = new UserId(INVALID_USER_ID_SPECIAL_CHAR);
    }

}
