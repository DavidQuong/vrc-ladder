package ca.sfu.cmpt373.alpha.vrcladder.persistence;

import ca.sfu.cmpt373.alpha.vrcladder.users.User;
import ca.sfu.cmpt373.alpha.vrcladder.users.authorization.UserRole;
import ca.sfu.cmpt373.alpha.vrcladder.users.personal.EmailAddress;
import ca.sfu.cmpt373.alpha.vrcladder.users.personal.PhoneNumber;
import ca.sfu.cmpt373.alpha.vrcladder.util.ConfigurationManager;
import ca.sfu.cmpt373.alpha.vrcladder.util.MockUserGenerator;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SessionManagerTest {

    private SessionManager sessionManager;

    // The User class was arbitrarily chosen to test data persistence.
    private User userFixture;

    @Before
    public void setUp() {
        sessionManager = new SessionManager(new ConfigurationManager());

        userFixture = MockUserGenerator.generatePlayer();
        saveOrUpdateUser(userFixture);
    }

    @After
    public void tearDown() {
        sessionManager.shutDown();
    }

    @Test
    public void testGetEntity() {
        Session session = sessionManager.getSession();
        User user = session.get(User.class, userFixture.getUserId());
        session.close();

        Assert.assertNotNull(user);
        Assert.assertEquals(userFixture.getUserRole(), user.getUserRole());
        Assert.assertEquals(userFixture.getFirstName(), user.getFirstName());
        Assert.assertEquals(userFixture.getMiddleName(), user.getMiddleName());
        Assert.assertEquals(userFixture.getLastName(), user.getLastName());
        Assert.assertEquals(userFixture.getEmailAddress(), user.getEmailAddress());
        Assert.assertEquals(userFixture.getPhoneNumber(), user.getPhoneNumber());
    }

    @Test
    public void testCreateEntity() {
        User newUser = MockUserGenerator.generatePlayer();
        saveOrUpdateUser(newUser);

        final UserRole userRole = UserRole.VOLUNTEER;
        final String firstName = "Bob";
        final String middleName = "David";
        final String lastName = "Doe";
        final PhoneNumber phoneNumber = new PhoneNumber("(604) 456-7890");
        final EmailAddress emailAddress = new EmailAddress("bobdoe@vrc.ca");

        newUser.setUserRole(userRole);
        newUser.setFirstName(firstName);
        newUser.setMiddleName(middleName);
        newUser.setLastName(lastName);
        newUser.setEmailAddress(emailAddress);
        newUser.setPhoneNumber(phoneNumber);

        Session session = sessionManager.getSession();
        User user = session.get(User.class, newUser.getUserId());
        session.close();

        Assert.assertNotNull(user);
        Assert.assertEquals(userRole, newUser.getUserRole());
        Assert.assertEquals(firstName, newUser.getFirstName());
        Assert.assertEquals(middleName, newUser.getMiddleName());
        Assert.assertEquals(lastName, newUser.getLastName());
        Assert.assertEquals(emailAddress, newUser.getEmailAddress());
        Assert.assertEquals(phoneNumber, newUser.getPhoneNumber());
    }

    private User saveOrUpdateUser(User user) {
        Session session = sessionManager.getSession();
        Transaction transaction = session.beginTransaction();
        session.saveOrUpdate(user);
        transaction.commit();

        return user;
    }

}
