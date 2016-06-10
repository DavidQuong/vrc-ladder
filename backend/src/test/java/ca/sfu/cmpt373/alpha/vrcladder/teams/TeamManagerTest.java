package ca.sfu.cmpt373.alpha.vrcladder.teams;

import ca.sfu.cmpt373.alpha.vrcladder.BaseTest;
import ca.sfu.cmpt373.alpha.vrcladder.exceptions.PersistenceException;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.AttendanceCard;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.PlayTime;
import ca.sfu.cmpt373.alpha.vrcladder.users.User;
import ca.sfu.cmpt373.alpha.vrcladder.users.personal.UserId;
import ca.sfu.cmpt373.alpha.vrcladder.util.IdType;
import org.hibernate.Session;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

// TODO: Add more tests, testing the following:
//       - Attempting to create an existing team
//       - Attempting to create an existing team, but reverse order of players
//       - Attempting to set a preferred playtime for two teams with a common player

public class TeamManagerTest extends BaseTest {

    private static final IdType MOCK_TEAM_ID = new IdType("35abe4c1-1e5b-4e6a-ab5c-9fffa5c89bf8");
    private static final UserId MOCK_FIRST_PLAYER_ID = new UserId("59152");
    private static final UserId MOCK_SECOND_PLAYER_ID = new UserId("59153");
    private static final IdType MOCK_ATTENDANCE_CARD_ID = new IdType("8250c624-a8de-4078-9b6c-701666541ed3");
    private static final PlayTime MOCK_PLAY_TIME = PlayTime.NONE;

    private static final UserId MOCK_NEW_FIRST_PLAYER_ID = new UserId("59154");
    private static final UserId MOCK_NEW_SECOND_PLAYER_ID = new UserId("59155");

    private TeamManager teamManager;

    @Before
    public void setUp() {
        teamManager = new TeamManager(sessionManager);
    }

    @Test
    public void testCreateTeamByUser() {
        Session session = sessionManager.getSession();
        User firstPlayer = session.get(User.class, MOCK_NEW_FIRST_PLAYER_ID.getUserId());
        User secondPlayer = session.get(User.class, MOCK_NEW_SECOND_PLAYER_ID.getUserId());

        // Ensure users exist
        Assert.assertNotNull(firstPlayer);
        Assert.assertNotNull(secondPlayer);

        Team newTeam = teamManager.create(firstPlayer, secondPlayer);

        Team team = session.get(Team.class, newTeam.getId());
        Assert.assertNotNull(team);
        Assert.assertNotNull(team.getAttendanceCard());
    }

    @Test
    public void testCreateTeamByUserId() {
        Session session = sessionManager.getSession();
        User firstPlayer = session.get(User.class, MOCK_NEW_FIRST_PLAYER_ID.getUserId());
        User secondPlayer = session.get(User.class, MOCK_NEW_SECOND_PLAYER_ID.getUserId());

        // Ensure users exist
        Assert.assertNotNull(firstPlayer);
        Assert.assertNotNull(secondPlayer);

        Team newTeam = teamManager.create(MOCK_NEW_FIRST_PLAYER_ID.getUserId(),
            MOCK_NEW_SECOND_PLAYER_ID.getUserId());

        Team team = session.get(Team.class, newTeam.getId());
        Assert.assertNotNull(team);
        Assert.assertNotNull(team.getAttendanceCard());
    }

    @Test
    public void testGetTeam() {
        Team team = teamManager.getById(MOCK_TEAM_ID.getId());
        AttendanceCard attendanceCard = team.getAttendanceCard();

        Assert.assertEquals(MOCK_FIRST_PLAYER_ID.getUserId(), team.getFirstPlayer().getUserId());
        Assert.assertEquals(MOCK_SECOND_PLAYER_ID.getUserId(), team.getSecondPlayer().getUserId());
        Assert.assertEquals(MOCK_ATTENDANCE_CARD_ID.getId(), attendanceCard.getId());
        Assert.assertEquals(MOCK_PLAY_TIME, attendanceCard.getPreferredPlayTime());
    }

    @Test
    public void testUpdateTeamAttendance() {
        final PlayTime newPlayTime = PlayTime.TIME_SLOT_A;
        teamManager.updateAttendance(MOCK_TEAM_ID, newPlayTime);

        Session session = sessionManager.getSession();
        Team team = session.get(Team.class, MOCK_TEAM_ID.getId());
        AttendanceCard attendanceCard = session.get(AttendanceCard.class, team.getAttendanceCard().getId());
        session.close();

        Assert.assertEquals(newPlayTime, attendanceCard.getPreferredPlayTime());
    }

    @Test (expected = PersistenceException.class)
    public void testUpdateNonExistentTeamAttendance() {
        String nonExistentTeamId = "jfasjfapwefja";
        teamManager.updateAttendance(nonExistentTeamId, PlayTime.TIME_SLOT_A);
    }

    @Test
    public void testDeleteTeam() {
        Team originalTeam = teamManager.deleteById(MOCK_TEAM_ID.getId());
        AttendanceCard originalAttendanceCard = originalTeam.getAttendanceCard();

        Session session = sessionManager.getSession();
        Team team = session.get(Team.class, MOCK_TEAM_ID.getId());
        AttendanceCard attendanceCard = session.get(AttendanceCard.class, originalAttendanceCard.getId());
        User firstPlayer = session.get(User.class, MOCK_FIRST_PLAYER_ID.getUserId());
        User secondPlayer = session.get(User.class, MOCK_SECOND_PLAYER_ID.getUserId());
        session.close();

        Assert.assertNotNull(originalTeam);
        Assert.assertNotNull(originalAttendanceCard);

        // Ensure both the team and their attendance card are deleted.
        Assert.assertNull(team);
        Assert.assertNull(attendanceCard);

        // Ensure that the individual players are not deleted.
        Assert.assertNotNull(firstPlayer);
        Assert.assertNotNull(secondPlayer);
    }

}
