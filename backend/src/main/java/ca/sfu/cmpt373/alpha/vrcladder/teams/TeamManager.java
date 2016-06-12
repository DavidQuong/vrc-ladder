package ca.sfu.cmpt373.alpha.vrcladder.teams;

import ca.sfu.cmpt373.alpha.vrcladder.exceptions.DuplicateTeamMemberException;
import ca.sfu.cmpt373.alpha.vrcladder.exceptions.EntityNotFoundException;
import ca.sfu.cmpt373.alpha.vrcladder.exceptions.ExistingTeamException;
import ca.sfu.cmpt373.alpha.vrcladder.exceptions.MultiplePlayTimeException;
import ca.sfu.cmpt373.alpha.vrcladder.persistence.DatabaseManager;
import ca.sfu.cmpt373.alpha.vrcladder.persistence.SessionManager;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.AttendanceCard;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.PlayTime;
import ca.sfu.cmpt373.alpha.vrcladder.users.User;
import ca.sfu.cmpt373.alpha.vrcladder.util.CriterionConstants;
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

    private static final Class TEAM_CLASS_TYPE = Team.class;

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

    public Team create(String firstPlayerId, String secondPlayerId) {
        Session session = sessionManager.getSession();
        User firstPlayer = session.get(User.class, firstPlayerId);
        User secondPlayer = session.get(User.class, secondPlayerId);
        session.close();

        if (firstPlayer == null || secondPlayer == null) {
            throw new EntityNotFoundException();
        }

        if (isExistingTeam(firstPlayer, secondPlayer)) {
            throw new ExistingTeamException();
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

    public Team updateAttendance(String teamId, PlayTime playTime) {
        Session session = sessionManager.getSession();

        Team team = session.get(Team.class, teamId);
        if (team == null) {
            throw new EntityNotFoundException();
        }

        if (playTime.isPlayable()) {
            User firstPlayer = team.getFirstPlayer();
            Team playingTeam = isAlreadyPlaying(firstPlayer);
            if (playingTeam != null && !team.equals(playingTeam)) {
                throw new MultiplePlayTimeException(firstPlayer.getUserId(), playingTeam.getId());
            }

            User secondPlayer = team.getSecondPlayer();
            playingTeam = isAlreadyPlaying(secondPlayer);
            if (playingTeam != null && !team.equals(playingTeam)) {
                throw new MultiplePlayTimeException(secondPlayer.getUserId(), playingTeam.getId());
            }
        }

        AttendanceCard attendanceCard = team.getAttendanceCard();
        attendanceCard.setPreferredPlayTime(playTime);

        team = update(team, session);
        session.close();

        return team;
    }

    private boolean isExistingTeam(User firstPlayer, User secondPlayer) {
        Session session = sessionManager.getSession();

        Criterion playerPairCriterion = Restrictions.and(
            Restrictions.eq(CriterionConstants.FIRST_PLAYER_USER_ID_PROPERTY, firstPlayer.getUserId()),
            Restrictions.eq(CriterionConstants.SECOND_PLAYER_USER_ID_PROPERTY, secondPlayer.getUserId()));
        Criterion reversePlayerPairCriterion = Restrictions.and(
            Restrictions.eq(CriterionConstants.FIRST_PLAYER_USER_ID_PROPERTY, secondPlayer.getUserId()),
            Restrictions.eq(CriterionConstants.SECOND_PLAYER_USER_ID_PROPERTY, firstPlayer.getUserId()));
        Criteria playerPairCriteria = session.createCriteria(Team.class)
            .add(Restrictions.or(playerPairCriterion, reversePlayerPairCriterion));

        List<Team> matchedTeams = playerPairCriteria.list();
        session.close();

        return (!matchedTeams.isEmpty());
    }

    private Team isAlreadyPlaying(User player) {
        Session session = sessionManager.getSession();

        Criterion firstPlayerCriterion = Restrictions.eq(CriterionConstants.FIRST_PLAYER_USER_ID_PROPERTY,
            player.getUserId());
        Criterion secondPlayerCriterion = Restrictions.eq(CriterionConstants.SECOND_PLAYER_USER_ID_PROPERTY,
            player.getUserId());
        Criteria playingCriteria = session.createCriteria(Team.class)
            .add(Restrictions.or(firstPlayerCriterion, secondPlayerCriterion));

        List<Team> matchedTeams = playingCriteria.list();
        session.close();

        for (Team team : matchedTeams) {
            if (team.getAttendanceCard().getPreferredPlayTime().isPlayable()) {
                return team;
            }
        }

        return null;
    }

}
