package ca.sfu.cmpt373.alpha.vrcladder.teams;

import ca.sfu.cmpt373.alpha.vrcladder.exceptions.DuplicateTeamMemberException;
import ca.sfu.cmpt373.alpha.vrcladder.exceptions.ExistingTeamException;
import ca.sfu.cmpt373.alpha.vrcladder.exceptions.MultiplePlayTimeException;
import ca.sfu.cmpt373.alpha.vrcladder.persistence.DatabaseManager;
import ca.sfu.cmpt373.alpha.vrcladder.persistence.SessionManager;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.AttendanceCard;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.PlayTime;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.AttendanceStatus;
import ca.sfu.cmpt373.alpha.vrcladder.users.User;
import ca.sfu.cmpt373.alpha.vrcladder.users.personal.UserId;
import ca.sfu.cmpt373.alpha.vrcladder.util.CriterionConstants;
import ca.sfu.cmpt373.alpha.vrcladder.util.IdType;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.exception.ConstraintViolationException;

import javax.persistence.EntityNotFoundException;
import java.util.List;

/**
 * Provides an interface to perform create, read, update, and delete (CRUD) operations on,
 * teams in the database.
 */
public class TeamManager extends DatabaseManager<Team> {

    private static final Class TEAM_CLASS_TYPE = Team.class;
    private static final Integer FIRST_POSITION = 1;
    private static final Order ASCENDING_POSITION_ORDER = Order.asc(CriterionConstants.TEAM_LADDER_POSITION_PROPERTY);
    private static final String ERROR_NOT_ALL_TEAMS = "All teams must be present in order to update ladder positions";

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

        LadderPosition newLadderPosition = generateNewLadderPosition();
        Team newTeam = new Team(firstPlayer, secondPlayer, newLadderPosition);

        try {
            create(newTeam);
        } catch (ConstraintViolationException exception) {
            throw new ExistingTeamException();
        }

        return newTeam;
    }

    public Team create(UserId firstPlayerId, UserId secondPlayerId) {
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

        if (firstPlayer.getUserId().equals(secondPlayer.getUserId())) {
            throw new DuplicateTeamMemberException();
        }

        LadderPosition newLadderPosition = generateNewLadderPosition();
        Team newTeam = new Team(firstPlayer, secondPlayer, newLadderPosition);

        try {
            create(newTeam);
        } catch (ConstraintViolationException exception) {
            throw new ExistingTeamException();
        }

        return newTeam;
    }

    /**
     * @return A List of Team's stored in the database in ascending LadderPosition order.
     */
    @Override
    public List<Team> getAll() {
        Session session = sessionManager.getSession();
        List<Team> teams = session.createCriteria(Team.class)
                .addOrder(ASCENDING_POSITION_ORDER)
                .list();
        session.close();

        return teams;
    }

    public Team updateAttendancePlaytime(IdType teamId, PlayTime playTime) {
        Team team = getById(teamId);

        if (playTime.isPlayable()) {
            checkForActiveTeam(team);
        }

        AttendanceCard attendanceCard = team.getAttendanceCard();
        attendanceCard.setPreferredPlayTime(playTime);
        team = update(team);

        return team;
    }

    public Team updateAttendanceStatus(IdType teamId, AttendanceStatus status) {
        Team team = getById(teamId);

        AttendanceCard attendanceCard = team.getAttendanceCard();
        attendanceCard.setAttendanceStatus(status);
        team = update(team);

        return team;
    }
    private boolean isExistingTeam(User firstPlayer, User secondPlayer) {
        Session session = sessionManager.getSession();

        Criterion playerPairCriterion = Restrictions.and(
            Restrictions.eq(CriterionConstants.TEAM_FIRST_PLAYER_USER_ID_PROPERTY, firstPlayer.getUserId()),
            Restrictions.eq(CriterionConstants.TEAM_SECOND_PLAYER_USER_ID_PROPERTY, secondPlayer.getUserId()));
        Criterion reversePlayerPairCriterion = Restrictions.and(
            Restrictions.eq(CriterionConstants.TEAM_FIRST_PLAYER_USER_ID_PROPERTY, secondPlayer.getUserId()),
            Restrictions.eq(CriterionConstants.TEAM_SECOND_PLAYER_USER_ID_PROPERTY, firstPlayer.getUserId()));
        Criteria playerPairCriteria = session.createCriteria(Team.class)
            .add(Restrictions.or(playerPairCriterion, reversePlayerPairCriterion));

        List<Team> matchedTeams = playerPairCriteria.list();
        session.close();

        return (!matchedTeams.isEmpty());
    }

    private void checkForActiveTeam(Team team) {
        User firstPlayer = team.getFirstPlayer();
        Team activeTeam = findActiveTeam(firstPlayer);
        if (activeTeam != null && !team.equals(activeTeam)) {
            String userId = firstPlayer.getUserId().toString();
            String teamId = activeTeam.getId().toString();
            throw new MultiplePlayTimeException(userId, teamId);
        }

        User secondPlayer = team.getSecondPlayer();
        activeTeam = findActiveTeam(secondPlayer);
        if (activeTeam != null && !team.equals(activeTeam)) {
            String userId = secondPlayer.getUserId().toString();
            String teamId = activeTeam.getId().toString();
            throw new MultiplePlayTimeException(userId, teamId);
        }
    }

    private Team findActiveTeam(User player) {
        Session session = sessionManager.getSession();

        Criterion firstPlayerCriterion = Restrictions.eq(CriterionConstants.TEAM_FIRST_PLAYER_USER_ID_PROPERTY,
            player.getUserId());
        Criterion secondPlayerCriterion = Restrictions.eq(CriterionConstants.TEAM_SECOND_PLAYER_USER_ID_PROPERTY,
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

    private LadderPosition generateNewLadderPosition() {
        Session session = sessionManager.getSession();
        Criteria lastPositionCriteria = session.createCriteria(Team.class)
            .setProjection(Projections.max(CriterionConstants.TEAM_LADDER_POSITION_PROPERTY));
        LadderPosition lastPosition = (LadderPosition) lastPositionCriteria.uniqueResult();

        if (lastPosition == null) {
            return new LadderPosition(FIRST_POSITION);
        } else {
            int nextPositionCount = lastPosition.getValue() + 1;
            return new LadderPosition(nextPositionCount);
        }
    }

    /**
     * Erases every Team's ranking in the database, and replaces each team's ranking
     * with their position in the list of teams
     * @param teams A list that contains every team in the database in the ranked order to be applied
     * @throws IllegalStateException if not every team in the database is passed in
     */
    public List<Team> updateLadderPositions(List<Team> teams) {
        //TODO: add more verification that every team in the ladder is actually passed in
        Session session = sessionManager.getSession();
        Long numTeamsInLadder = (Long) session.createCriteria(Team.class)
                .setProjection(Projections.rowCount())
                .uniqueResult();
        if (teams.size() != numTeamsInLadder) {
            throw new IllegalStateException(ERROR_NOT_ALL_TEAMS);
        }

        //TODO: figure out a less-hacky way to do this!
        //since ladderPositions have a unique constraint,
        //we must overwrite all the values with dummy values before continuing
        Transaction transaction = session.beginTransaction();
        for (int i = 0; i < teams.size(); i++) {
            long placeHolderLadderPosition = numTeamsInLadder + i + 1;
            Team team = teams.get(i);
            team.setLadderPosition(new LadderPosition((int) placeHolderLadderPosition));
            session.update(team);
        }
        transaction.commit();

        transaction = session.beginTransaction();
        for (int i = 0; i < teams.size(); i++) {
            int newTeamLadderPosition = i + 1;
            Team team = teams.get(i);
            team.setLadderPosition(new LadderPosition(newTeamLadderPosition));
            session.update(team);
        }
        transaction.commit();

        session.close();
        return teams;
    }

}
