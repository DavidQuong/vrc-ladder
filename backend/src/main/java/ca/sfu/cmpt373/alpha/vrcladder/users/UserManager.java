package ca.sfu.cmpt373.alpha.vrcladder.users;

import ca.sfu.cmpt373.alpha.vrcladder.exceptions.EntityNotFoundException;
import ca.sfu.cmpt373.alpha.vrcladder.persistence.DatabaseManager;
import ca.sfu.cmpt373.alpha.vrcladder.persistence.SessionManager;
import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
import ca.sfu.cmpt373.alpha.vrcladder.users.authorization.UserRole;
import ca.sfu.cmpt373.alpha.vrcladder.util.CriterionConstants;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;

import java.util.List;

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

    public User update(String userId, UserRole userRole, String firstName, String middleName, String lastName,
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

    @Override
    public User delete(User user) {
        if (user.getUserRole() == UserRole.PLAYER) {

            Session session = sessionManager.getSession();
            Transaction transaction = session.beginTransaction();

            List<Team> teams = getTeamsOfPlayer(user.getUserId());
            teams.forEach(session::delete);
            session.delete(user);

            transaction.commit();
            session.close();
        } else {
            super.delete(user);
        }

        return user;
    }

    @Override
    public User deleteById(String userId) {
        Session session = sessionManager.getSession();
        User user = session.get(User.class, userId);
        session.close();

        if (user == null) {
            throw new EntityNotFoundException();
        }

        return delete(user);
    }

    private List<Team> getTeamsOfPlayer(String playerId) {
        Session session = sessionManager.getSession();

        Criterion firstPlayerCriterion = Restrictions.eq(CriterionConstants.TEAM_FIRST_PLAYER_USER_ID_PROPERTY,
            playerId);
        Criterion secondPlayerCriterion = Restrictions.eq(CriterionConstants.TEAM_SECOND_PLAYER_USER_ID_PROPERTY,
            playerId);
        Criteria teamCriteria = session.createCriteria(Team.class)
            .add(Restrictions.or(firstPlayerCriterion, secondPlayerCriterion));

        List<Team> matchedTeams = teamCriteria.list();
        session.close();

        return matchedTeams;
    }

}
