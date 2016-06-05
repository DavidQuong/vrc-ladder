package ca.sfu.cmpt373.alpha.vrcladder.teams;

import ca.sfu.cmpt373.alpha.vrcladder.persistence.SessionManager;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.AttendanceCard;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.PlayTime;
import ca.sfu.cmpt373.alpha.vrcladder.users.User;
import ca.sfu.cmpt373.alpha.vrcladder.util.IdType;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

/**
 * Provides an interface to perform create, read, update, and delete (CRUD) operations on,
 * teams in the database.
 */
public class TeamManager {

    private SessionManager sessionManager;

    public TeamManager(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    public Team createTeam(User firstPlayer, User secondPlayer) {
        Team newTeam = new Team(firstPlayer, secondPlayer);

        Session session = sessionManager.getSession();

        Transaction transaction = session.beginTransaction();
        session.save(newTeam);
        transaction.commit();

        session.close();

        return newTeam;
    }

    public Team createTeam(String firstPlayerId, String secondPlayerId) {
        Session session = sessionManager.getSession();

        User firstPlayer = session.get(User.class, firstPlayerId);
        User secondPlayer = session.get(User.class, secondPlayerId);

        Team newTeam = new Team(firstPlayer, secondPlayer);
        Transaction transaction = session.beginTransaction();
        session.save(newTeam);
        transaction.commit();

        return newTeam;
    }

    public Team getTeam(IdType teamId) {
        return getTeam(teamId.getId());
    }

    public Team getTeam(String teamId) {
        Session session = sessionManager.getSession();
        Team team = session.get(Team.class, teamId);
        session.close();

        return team;
    }

    public Team setTeamAttendance(IdType teamId, PlayTime preferredPlayTime) {
        return setTeamAttendance(teamId.getId(), preferredPlayTime);
    }

    public Team setTeamAttendance(String teamId, PlayTime preferredPlayTime) {
        Session session = sessionManager.getSession();

        Transaction transaction = session.beginTransaction();
        Team team = session.get(Team.class, teamId);
        AttendanceCard attendanceCard = team.getAttendanceCard();
        attendanceCard.setPreferredPlayTime(preferredPlayTime);

        session.save(team);
        transaction.commit();
        session.close();

        return team;
    }

    public Team deleteTeam(IdType teamId) {
        return deleteTeam(teamId.getId());
    }

    public Team deleteTeam(String teamId) {
        Session session = sessionManager.getSession();

        Transaction transaction = session.beginTransaction();
        Team team = session.get(Team.class, teamId);

        session.delete(team);
        transaction.commit();
        session.close();

        return team;
    }

    public List<Team> getAllTeams() {
        return sessionManager.getSession().createCriteria(Team.class).list();
    }

}
