package ca.sfu.cmpt373.alpha.vrcladder.users;

import ca.sfu.cmpt373.alpha.vrcladder.BaseTest;
import ca.sfu.cmpt373.alpha.vrcladder.users.authorization.UserRole;
import ca.sfu.cmpt373.alpha.vrcladder.users.personal.EmailAddress;
import ca.sfu.cmpt373.alpha.vrcladder.users.personal.PhoneNumber;
import ca.sfu.cmpt373.alpha.vrcladder.users.personal.UserId;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class UserManagerTest extends BaseTest {

    private static final UserId MOCK_USER_ID = new UserId("86432");
    private static final UserRole MOCK_USER_ROLE = UserRole.PLAYER;
    private static final String MOCK_FIRST_NAME = "Scott";
    private static final String MOCK_LAST_NAME = "Johnson";
    private static final EmailAddress MOCK_EMAIL_ADDRESS = new EmailAddress("scottj@vrc.ca");
    private static final PhoneNumber MOCK_PHONE_NUMBER = new PhoneNumber("(604) 111-2222");

    private UserManager userManager;

    @Before
    public void setup() {
        userManager = new UserManager(sessionManager);
    }

    @Test
    public void testCreateUser() {
        final UserId id = new UserId("12345");
        final UserRole userRole = UserRole.ADMINISTRATOR;
        final String firstName = "David";
        final String middleName = "James";
        final String lastName = "Matthews";
        final EmailAddress emailAddress = new EmailAddress("djmatthews@vrc.ca");
        final PhoneNumber phoneNumber = new PhoneNumber("604-000-1234");

        userManager.create(id.getUserId(), userRole, firstName, middleName, lastName,
            emailAddress.getEmailAddress(), phoneNumber.getPhoneNumber());

        Session session = sessionManager.getSession();
        User user = session.get(User.class, id.getUserId());
        session.close();

        Assert.assertNotNull(user);
        Assert.assertEquals(id.getUserId(), user.getUserId());
        Assert.assertEquals(userRole, user.getUserRole());
        Assert.assertEquals(firstName, user.getFirstName());
        Assert.assertEquals(middleName, user.getMiddleName());
        Assert.assertEquals(lastName, user.getLastName());
        Assert.assertEquals(emailAddress.getEmailAddress(), user.getEmailAddress());
        Assert.assertEquals(phoneNumber.getPhoneNumber(), user.getPhoneNumber());
    }

    @Test
    public void testGetUser() {
        User user = userManager.getById(MOCK_USER_ID.getUserId());

        Assert.assertNotNull(user);
        Assert.assertEquals(MOCK_USER_ID.getUserId(), user.getUserId());
        Assert.assertEquals(MOCK_USER_ROLE, user.getUserRole());
        Assert.assertEquals(MOCK_FIRST_NAME, user.getFirstName());
        Assert.assertEquals(StringUtils.EMPTY, user.getMiddleName());
        Assert.assertEquals(MOCK_LAST_NAME, user.getLastName());
        Assert.assertEquals(MOCK_EMAIL_ADDRESS.getEmailAddress(), user.getEmailAddress());
        Assert.assertEquals(MOCK_PHONE_NUMBER.getPhoneNumber(), user.getPhoneNumber());
    }

    @Test
    public void testUpdateUser() {
        final String newFirstName = "Billy";
        final String newMiddleName = "Jimmy";
        final PhoneNumber newPhoneNumber = new PhoneNumber("(778) 111-2222");
        final EmailAddress newEmailAddress = new EmailAddress("billyj@vrc.ca");

        userManager.updateUser(MOCK_USER_ID.toString(), MOCK_USER_ROLE, newFirstName, newMiddleName, MOCK_LAST_NAME,
            newEmailAddress.getEmailAddress(), newPhoneNumber.getPhoneNumber());

        Session session = sessionManager.getSession();
        User user = session.get(User.class, MOCK_USER_ID.getUserId());
        session.close();

        Assert.assertEquals(MOCK_USER_ID.getUserId(), user.getUserId());
        Assert.assertEquals(MOCK_USER_ROLE, user.getUserRole());
        Assert.assertEquals(newFirstName, user.getFirstName());
        Assert.assertEquals(newMiddleName, user.getMiddleName());
        Assert.assertEquals(MOCK_LAST_NAME, user.getLastName());
        Assert.assertEquals(newEmailAddress.getEmailAddress(), user.getEmailAddress());
        Assert.assertEquals(newPhoneNumber.getPhoneNumber(), user.getPhoneNumber());
    }

    @Test
    public void testDeleteUser() {
        User originalUser = userManager.deleteById(MOCK_USER_ID.getUserId());

        Session session = sessionManager.getSession();
        User user = session.get(User.class, MOCK_USER_ID.getUserId());
        session.close();

        Assert.assertNotNull(originalUser);
        Assert.assertNull(user);
    }

}
