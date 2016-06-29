package ca.sfu.cmpt373.alpha.vrcladder.util;

import ca.sfu.cmpt373.alpha.vrcladder.users.User;
import ca.sfu.cmpt373.alpha.vrcladder.users.UserBuilder;
import ca.sfu.cmpt373.alpha.vrcladder.users.authentication.Password;
import ca.sfu.cmpt373.alpha.vrcladder.users.authentication.SecurityManager;
import ca.sfu.cmpt373.alpha.vrcladder.users.authorization.UserRole;
import ca.sfu.cmpt373.alpha.vrcladder.users.personal.EmailAddress;
import ca.sfu.cmpt373.alpha.vrcladder.users.personal.PhoneNumber;
import ca.sfu.cmpt373.alpha.vrcladder.users.personal.UserId;
import org.apache.commons.lang3.StringUtils;

public class MockUserGenerator {

    private static final String EMAIL_FORMAT = "user%s@vrc.ca";
    private static final int PHONE_NUMBER_SIZE = 10;
    private static final char PHONE_NUMBER_PADDING_CHAR = '0';
    private static final String FIRST_NAME_BASE = "firstName";
    private static final String LAST_NAME_BASE = "lastName";
    private static final String HASH_BASE = "hash";

    private static SecurityManager securityManager;
    private static int userCount;

    static {
        securityManager = new SecurityManager();
        userCount = 0;
    }

    public static User generatePlayer() {
        userCount++;

        String userCountStr = String.valueOf(userCount);

        UserId userId = new UserId(userCountStr);
        String firstName = FIRST_NAME_BASE + userId;
        String lastName = LAST_NAME_BASE + userId;
        EmailAddress emailAddress = generateEmailAddress(userCountStr);
        PhoneNumber phoneNumber = generatePhoneNumber(userCountStr);
        Password password = new Password(HASH_BASE + userId);

        User newUser = new UserBuilder()
            .setUserId(userId)
            .setUserRole(UserRole.PLAYER)
            .setFirstName(firstName)
            .setMiddleName(StringUtils.EMPTY)
            .setLastName(lastName)
            .setEmailAddress(emailAddress)
            .setPhoneNumber(phoneNumber)
            .setPassword(password)
            .buildUser();

        return newUser;
    }

    public static void resetUserCount() {
        userCount = 0;
    }

    private static EmailAddress generateEmailAddress(String userNum) {
        String emailAddress = String.format(EMAIL_FORMAT, userNum);

        return new EmailAddress(emailAddress);
    }

    private static PhoneNumber generatePhoneNumber(String userNum) {
        int digitsNeeded = PHONE_NUMBER_SIZE - userNum.length();

        StringBuilder numberPadder = new StringBuilder(userNum);
        for (int i = 0; i < digitsNeeded; i++) {
            numberPadder.insert(0, PHONE_NUMBER_PADDING_CHAR);
        }

        return new PhoneNumber(numberPadder.toString());
    }

}

