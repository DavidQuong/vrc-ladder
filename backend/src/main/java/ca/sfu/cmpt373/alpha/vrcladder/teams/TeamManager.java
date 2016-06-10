package ca.sfu.cmpt373.alpha.vrcladder.teams;

import ca.sfu.cmpt373.alpha.vrcladder.exceptions.DuplicateTeamMemberException;
import ca.sfu.cmpt373.alpha.vrcladder.exceptions.ExistingTeamException;
import ca.sfu.cmpt373.alpha.vrcladder.exceptions.PersistenceException;
import ca.sfu.cmpt373.alpha.vrcladder.persistence.DatabaseManager;
import ca.sfu.cmpt373.alpha.vrcladder.persistence.SessionManager;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.AttendanceCard;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.PlayTime;
import ca.sfu.cmpt373.alpha.vrcladder.users.User;
import ca.sfu.cmpt373.alpha.vrcladder.util.IdType;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.ConstraintViolationException;

import java.util.List;

/**
 * Provides an interface to perform create, read, update, and delete (CRUD) operations on,
 * teams in the database.
 *
 * TODO - Ensure that a player cannot opt-in play for multiple teams in a given tournament.
 */
public class TeamManager extends DatabaseManager<Team> {
    private static final String ERROR_NO_TEAM = "There is no team for this team ID";
    private static final Class TEAM_CLASS_TYPE = Team.class;
    private static final String FIRST_PLAYER_USER_ID_PROPERTY = "firstPlayer.userId";
    private static final String SECOND_PLAYER_USER_ID_PROPERTY = "secondPlayer.userId";

    public TeamManager(SessionManager sessionManager) {
        super(TEAM_CLASS_TYPE, sessionManager);
    }

    public Team create(User firstPlayer, User secondPlayer) {
        if (isExistingTeam(firstPlayer, secondPlayer)) {
            throw new ExistingTeamException();
        }

        if (firstPlayer.getUserId().equals(secondPlayer.getUserId())) {
            throw new DuplicateTeamMemberException();
        }

        Team newTeam = new Team(firstPlayer, secondPlayer);

        try {
            create(newTeam);
        } catch (ConstraintViolationException exception) {
            throw new ExistingTeamException();
        }

        return newTeam;
    }

    public Team updateAttendance(IdType teamId, PlayTime preferredPlayTime) {
        return updateAttendance(teamId.getId(), preferredPlayTime);
    }

    public Team updateAttendance(String teamId, PlayTime preferredPlayTime) {
        Session session = sessionManager.getSession();

        Team team = session.get(Team.class, teamId);
        if (team == null) {
            throw new PersistenceException(ERROR_NO_TEAM);
        }
        AttendanceCard attendanceCard = team.getAttendanceCard();
        attendanceCard.setPreferredPlayTime(preferredPlayTime);

        team = update(team, session);
        session.close();

        return team;
    }

    public List<Team> getAllTeams() {
        return sessionManager.getSession().createCriteria(Team.class).list();
    }

    private boolean isExistingTeam(User firstPlayer, User secondPlayer) {
        Session session = sessionManager.getSession();
        Criterion playerPairCriterion = Restrictions.and(
            Restrictions.eq(FIRST_PLAYER_USER_ID_PROPERTY, firstPlayer.getUserId()),
            Restrictions.eq(SECOND_PLAYER_USER_ID_PROPERTY, secondPlayer.getUserId()));
        Criterion reversePlayerPairCriterion = Restrictions.and(
                Restrictions.eq(FIRST_PLAYER_USER_ID_PROPERTY, secondPlayer.getUserId()),
                Restrictions.eq(SECOND_PLAYER_USER_ID_PROPERTY, firstPlayer.getUserId()));

        Criteria playerPairCriterea = session.createCriteria(Team.class)
            .add(Restrictions.or(playerPairCriterion, reversePlayerPairCriterion));

        List results = playerPairCriterea.list();
        session.close();

        return (!results.isEmpty());
    }

}
