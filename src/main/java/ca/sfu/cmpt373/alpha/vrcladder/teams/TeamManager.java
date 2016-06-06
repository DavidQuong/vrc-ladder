package ca.sfu.cmpt373.alpha.vrcladder.teams;

import ca.sfu.cmpt373.alpha.vrcladder.persistence.DatabaseManager;
import ca.sfu.cmpt373.alpha.vrcladder.persistence.SessionManager;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.AttendanceCard;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.PlayTime;
import ca.sfu.cmpt373.alpha.vrcladder.users.User;
import ca.sfu.cmpt373.alpha.vrcladder.util.IdType;
import org.hibernate.Session;

import java.util.List;

/**
 * Provides an interface to perform create, read, update, and delete (CRUD) operations on,
 * teams in the database.
 */
public class TeamManager extends DatabaseManager<Team> {

    private static final Class TEAM_CLASS_TYPE = Team.class;

    public TeamManager(SessionManager sessionManager) {
        super(TEAM_CLASS_TYPE, sessionManager);
    }

    public Team create(User firstPlayer, User secondPlayer) {
        Team newTeam = new Team(firstPlayer, secondPlayer);

        return create(newTeam);
    }

    public Team create(String firstPlayerId, String secondPlayerId) {
        Session session = sessionManager.getSession();

        User firstPlayer = session.get(User.class, firstPlayerId);
        User secondPlayer = session.get(User.class, secondPlayerId);

        Team newTeam = new Team(firstPlayer, secondPlayer);

        return create(newTeam);
    }

    public Team updateAttendance(IdType teamId, PlayTime preferredPlayTime) {
        return updateAttendance(teamId.getId(), preferredPlayTime);
    }

    public Team updateAttendance(String teamId, PlayTime preferredPlayTime) {
        Session session = sessionManager.getSession();

        Team team = session.get(Team.class, teamId);
        AttendanceCard attendanceCard = team.getAttendanceCard();
        attendanceCard.setPreferredPlayTime(preferredPlayTime);

        team = update(team, session);
        session.close();

        return team;
    }

    public List<Team> getAllTeams() {
        return sessionManager.getSession().createCriteria(Team.class).list();
    }

}
