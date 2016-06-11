package ca.sfu.cmpt373.alpha.vrcladder.users;

import ca.sfu.cmpt373.alpha.vrcladder.BaseTest;
import ca.sfu.cmpt373.alpha.vrcladder.users.authorization.UserRole;
import ca.sfu.cmpt373.alpha.vrcladder.users.personal.EmailAddress;
import ca.sfu.cmpt373.alpha.vrcladder.users.personal.PhoneNumber;
import ca.sfu.cmpt373.alpha.vrcladder.users.personal.UserId;
import ca.sfu.cmpt373.alpha.vrcladder.util.MockUserGenerator;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class UserManagerTest extends BaseTest {

    private UserManager userManager;
    private User userFixture;

    @Before
    public void setup() {
        userManager = new UserManager(sessionManager);
        userFixture = MockUserGenerator.generatePlayer();

        Session session = sessionManager.getSession();
        Transaction transaction = session.beginTransaction();
        session.save(userFixture);
        transaction.commit();
        session.close();
    }

    @Test
    public void testCreateUser() {
        User newUser = MockUserGenerator.generatePlayer();

        userManager.create(newUser.getUserId(), newUser.getUserRole(), newUser.getFirstName(), newUser.getMiddleName(),
            newUser.getLastName(), newUser.getEmailAddress(), newUser.getPhoneNumber());

        Session session = sessionManager.getSession();
        User user = session.get(User.class, newUser.getUserId());
        session.close();

        Assert.assertNotNull(user);
        Assert.assertEquals(newUser.getUserId(), user.getUserId());
        Assert.assertEquals(newUser.getUserRole(), user.getUserRole());
        Assert.assertEquals(newUser.getFirstName(), user.getFirstName());
        Assert.assertEquals(newUser.getMiddleName(), user.getMiddleName());
        Assert.assertEquals(newUser.getLastName(), user.getLastName());
        Assert.assertEquals(newUser.getEmailAddress(), user.getEmailAddress());
        Assert.assertEquals(newUser.getPhoneNumber(), user.getPhoneNumber());
    }

    @Test
    public void testGetUser() {
        User existingUser = userManager.getById(userFixture.getUserId());

        Assert.assertNotNull(existingUser);
        Assert.assertEquals(userFixture.getUserId(), existingUser.getUserId());
        Assert.assertEquals(userFixture.getUserRole(), existingUser.getUserRole());
        Assert.assertEquals(userFixture.getFirstName(), existingUser.getFirstName());
        Assert.assertEquals(userFixture.getMiddleName(), existingUser.getMiddleName());
        Assert.assertEquals(userFixture.getLastName(), existingUser.getLastName());
        Assert.assertEquals(userFixture.getEmailAddress(), existingUser.getEmailAddress());
        Assert.assertEquals(userFixture.getPhoneNumber(), existingUser.getPhoneNumber());
    }

    @Test
    public void testUpdateUser() {
        final String newFirstName = "Billy";
        final String newMiddleName = "Jimmy";
        final PhoneNumber newPhoneNumber = new PhoneNumber("(778) 111-2222");
        final EmailAddress newEmailAddress = new EmailAddress("billyj@vrc.ca");

        userManager.updateUser(userFixture.getUserId(), userFixture.getUserRole(), newFirstName, newMiddleName,
            userFixture.getLastName(), newEmailAddress.getEmailAddress(), newPhoneNumber.getPhoneNumber());

        Session session = sessionManager.getSession();
        User user = session.get(User.class, userFixture.getUserId());
        session.close();

        Assert.assertEquals(userFixture.getUserId(), user.getUserId());
        Assert.assertEquals(userFixture.getUserRole(), user.getUserRole());
        Assert.assertEquals(newFirstName, user.getFirstName());
        Assert.assertEquals(newMiddleName, user.getMiddleName());
        Assert.assertEquals(userFixture.getLastName(), user.getLastName());
        Assert.assertEquals(newEmailAddress.getEmailAddress(), user.getEmailAddress());
        Assert.assertEquals(newPhoneNumber.getPhoneNumber(), user.getPhoneNumber());
    }

    @Test
    public void testDeleteUser() {
        Session session = sessionManager.getSession();
        User user = session.get(User.class, userFixture.getUserId());
        Assert.assertNotNull(user);

        User originalUser = userManager.deleteById(userFixture.getUserId());
        session.clear();
        user = session.get(User.class, userFixture.getUserId());
        session.close();

        Assert.assertNotNull(originalUser);
        Assert.assertNull(user);
    }

}
