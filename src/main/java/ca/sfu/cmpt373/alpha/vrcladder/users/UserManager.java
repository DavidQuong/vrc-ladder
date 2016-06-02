package ca.sfu.cmpt373.alpha.vrcladder.users;

import ca.sfu.cmpt373.alpha.vrcladder.persistence.SessionManager;
import ca.sfu.cmpt373.alpha.vrcladder.users.authorization.UserRole;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Session;
import org.hibernate.Transaction;

/**
 * Provides an interface to perform create, read, update, and delete (CRUD) operations on
 * users in the database.
 */
public class UserManager {

    private SessionManager sessionManager;

    public UserManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public User createUser(String userId, UserRole userRole, String firstName, String lastName, String emailAddress,
        String phoneNumber) {
        return createUser(userId, userRole, firstName, StringUtils.EMPTY, lastName, emailAddress, phoneNumber);
    }

    public User createUser(String userId, UserRole userRole, String firstName, String middleName, String lastName,
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
        saveProvidedUser(createdUser);

        return createdUser;
    }

    public User getUser(String userId) {
        Session session = sessionManager.getSession();
        User user = session.get(User.class, userId);
        session.close();

        return user;
    }

    public User updateUser(String userId, UserRole userRole, String firstName, String middleName, String lastName,
        String emailAddress, String phoneNumber) {
        Session session = sessionManager.getSession();

        User user = session.get(User.class, userId);
        updateProvidedUser(session, user, userRole, firstName, middleName, lastName, emailAddress, phoneNumber);
        session.close();

        return user;
    }

    public User deleteUser(String userId) {
        Session session = sessionManager.getSession();

        Transaction transaction = session.beginTransaction();
        User user = session.get(User.class, userId);

        session.delete(user);
        transaction.commit();
        session.close();

        return user;
    }

    private void saveProvidedUser(User user) {
        Session session = sessionManager.getSession();
        Transaction transaction = session.beginTransaction();

        session.save(user);
        transaction.commit();

        session.close();
    }

    private void updateProvidedUser(Session session, User user, UserRole userRole, String firstName, String middleName,
        String lastName, String emailAddress, String phoneNumber) {
        Transaction transaction = session.beginTransaction();

        user.setUserRole(userRole);
        user.setFirstName(firstName);
        user.setMiddleName(middleName);
        user.setLastName(lastName);
        user.setEmailAddress(emailAddress);
        user.setPhoneNumber(phoneNumber);

        session.update(user);
        transaction.commit();
    }

}
