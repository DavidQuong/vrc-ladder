package ca.sfu.cmpt373.alpha.vrcladder.persistance;

import ca.sfu.cmpt373.alpha.vrcladder.persistence.SessionManager;
import ca.sfu.cmpt373.alpha.vrcladder.users.User;
import ca.sfu.cmpt373.alpha.vrcladder.users.authorization.UserRole;
import ca.sfu.cmpt373.alpha.vrcladder.users.personal.EmailAddress;
import ca.sfu.cmpt373.alpha.vrcladder.users.personal.PhoneNumber;
import ca.sfu.cmpt373.alpha.vrcladder.users.personal.UserId;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SessionManagerTest {

    private static final UserId MOCK_USER_ID = new UserId("86432");
    private static final UserRole MOCK_USER_ROLE = UserRole.PLAYER;
    private static final String MOCK_FIRST_NAME = "Scott";
    private static final String MOCK_LAST_NAME = "Johnson";
    private static final PhoneNumber MOCK_PHONE_NUMBER = new PhoneNumber("(604) 111-2222");
    private static final EmailAddress MOCK_EMAIL_ADDRESS = new EmailAddress("scottj@vrc.ca");

    private SessionManager sessionManager;

    @Before
    public void setUp() {
        sessionManager = new SessionManager();

        Session session = sessionManager.getSession();
        createOrUpdateUser(
            session,
            MOCK_USER_ID.toString(),
            MOCK_USER_ROLE,
            MOCK_FIRST_NAME,
            null,
            MOCK_LAST_NAME,
            MOCK_PHONE_NUMBER.toString(),
            MOCK_EMAIL_ADDRESS.toString()
        );
        session.close();
    }

    @After
    public void tearDown() {
        sessionManager.shutDown();
    }

    @Test
    public void testGetEntity() {
        Session session = sessionManager.getSession();
        User user = session.get(User.class, MOCK_USER_ID.toString());
        session.close();

        Assert.assertNotNull(user);
        Assert.assertEquals(MOCK_USER_ID.getUserId(), user.getUserId());
        Assert.assertEquals(MOCK_USER_ROLE, user.getUserRole());
        Assert.assertEquals(MOCK_FIRST_NAME, user.getFirstName());
        Assert.assertEquals(MOCK_LAST_NAME, user.getLastName());
        Assert.assertEquals(MOCK_PHONE_NUMBER.getPhoneNumber(), user.getPhoneNumber());
        Assert.assertEquals(MOCK_EMAIL_ADDRESS.getEmailAddress(), user.getEmailAddress());
    }

    @Test
    public void testCreateEntity() {
        final UserId userId = new UserId("456789");
        final UserRole userRole = UserRole.PLAYER;
        final String firstName = "Bob";
        final String lastName = "Doe";
        final PhoneNumber phoneNumber = new PhoneNumber("(604) 456-7890");
        final EmailAddress emailAddress = new EmailAddress("bobdoe@vrc.ca");

        Session session = sessionManager.getSession();
        createOrUpdateUser(
            session,
            userId.toString(),
            userRole,
            firstName,
            null,
            lastName,
            phoneNumber.toString(),
            emailAddress.toString()
        );

        User user = session.get(User.class, userId.toString());
        session.close();

        Assert.assertNotNull(user);
    }

    private User createOrUpdateUser(Session session, String userId, UserRole userRole, String firstName, String middleName,
                                    String lastName, String phoneNumber, String emailAddress) {
        Transaction transaction = session.beginTransaction();
        User user = new User();
        user.setUserId(userId);
        user.setUserRole(userRole);
        user.setFirstName(firstName);
        user.setMiddleName(middleName);
        user.setLastName(lastName);
        user.setPhoneNumber(phoneNumber);
        user.setEmailAddress(emailAddress);
        session.saveOrUpdate(user);
        transaction.commit();

        return user;
    }

}
