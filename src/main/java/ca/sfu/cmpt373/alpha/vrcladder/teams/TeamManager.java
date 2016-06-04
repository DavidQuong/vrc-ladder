package ca.sfu.cmpt373.alpha.vrcladder.teams;

import ca.sfu.cmpt373.alpha.vrcladder.persistence.DatabaseManager;
import ca.sfu.cmpt373.alpha.vrcladder.persistence.SessionManager;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.AttendanceCard;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.PlayTime;
import ca.sfu.cmpt373.alpha.vrcladder.users.User;
import ca.sfu.cmpt373.alpha.vrcladder.util.IdType;
import org.hibernate.Session;

/**
 * Provides an interface to perform create, read, update, and delete (CRUD) operations on,
 * teams in the database.
 */
public class TeamManager extends DatabaseManager<Team> {

    private static final Class TEAM_CLASS_TYPE = Team.class;

    public TeamManager(SessionManager sessionManager) {
        super(TEAM_CLASS_TYPE, sessionManager);
    }

    public Team createTeam(User firstPlayer, User secondPlayer) {
        Team newTeam = new Team(firstPlayer, secondPlayer);

        return create(newTeam);
    }

    public Team createTeam(String firstPlayerId, String secondPlayerId) {
        Session session = sessionManager.getSession();

        User firstPlayer = session.get(User.class, firstPlayerId);
        User secondPlayer = session.get(User.class, secondPlayerId);

        Team newTeam = new Team(firstPlayer, secondPlayer);

        return create(newTeam);
    }

    public Team getTeam(IdType teamId) {
        return getById(teamId.getId());
    }

    public Team getTeam(String teamId) {
        return getById(teamId);
    }

    public Team setTeamAttendance(IdType teamId, PlayTime preferredPlayTime) {
        return setTeamAttendance(teamId.getId(), preferredPlayTime);
    }

    public Team setTeamAttendance(String teamId, PlayTime preferredPlayTime) {
        Session session = sessionManager.getSession();

        Team team = session.get(Team.class, teamId);
        AttendanceCard attendanceCard = team.getAttendanceCard();
        attendanceCard.setPreferredPlayTime(preferredPlayTime);

        team = update(team, session);
        session.close();

        return team;
    }

    public Team deleteTeam(IdType teamId) {
        return deleteById(teamId.getId());
    }

    public Team deleteTeam(String teamId) {
        return deleteById(teamId);
    }

}
