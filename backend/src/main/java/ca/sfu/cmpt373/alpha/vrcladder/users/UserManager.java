package ca.sfu.cmpt373.alpha.vrcladder.users;

import ca.sfu.cmpt373.alpha.vrcladder.persistence.DatabaseManager;
import ca.sfu.cmpt373.alpha.vrcladder.persistence.SessionManager;
import ca.sfu.cmpt373.alpha.vrcladder.users.authorization.UserRole;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;

/**
 * Provides an interface to perform create, read, update, and delete (CRUD) operations on
 * users in the database.
 *
 * TODO - Handle errors that can arise from User creation (i.e., non-unique email or user ID).
 */
public class UserManager extends DatabaseManager<User> {

    private static final Class USER_CLASS_TYPE = User.class;

    public UserManager(SessionManager sessionManager) {
        super(USER_CLASS_TYPE, sessionManager);
    }

    @Override
    public User create(User user) {
        return super.create(user);
    }

    public User create(String userId, UserRole userRole, String firstName, String lastName, String emailAddress,
        String phoneNumber) {
        return create(userId, userRole, firstName, StringUtils.EMPTY, lastName, emailAddress, phoneNumber);
    }

    public User create(String userId, UserRole userRole, String firstName, String middleName, String lastName,
        String emailAddress, String phoneNumber) {
        User createdUser = new UserBuilder()
            .setUserId(userId)
            .setUserRole(userRole)
            .setFirstName(firstName)
            .setMiddleName(middleName)
            .setLastName(lastName)
            .setEmailAddress(emailAddress)
            .setPhoneNumber(phoneNumber)
            .buildUser();
        create(createdUser);

        return createdUser;
    }

    public User updateUser(String userId, UserRole userRole, String firstName, String middleName, String lastName,
        String emailAddress, String phoneNumber) {
        Session session = sessionManager.getSession();

        User user = session.get(User.class, userId);
        user.setUserRole(userRole);
        user.setFirstName(firstName);
        user.setMiddleName(middleName);
        user.setLastName(lastName);
        user.setEmailAddress(emailAddress);
        user.setPhoneNumber(phoneNumber);

        return update(user, session);
    }
}
