package ca.sfu.cmpt373.alpha.vrcladder.teams;

import ca.sfu.cmpt373.alpha.vrcladder.BaseTest;
import ca.sfu.cmpt373.alpha.vrcladder.exceptions.DuplicateTeamMemberException;
import ca.sfu.cmpt373.alpha.vrcladder.exceptions.EntityNotFoundException;
import ca.sfu.cmpt373.alpha.vrcladder.exceptions.ExistingTeamException;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.AttendanceCard;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.PlayTime;
import ca.sfu.cmpt373.alpha.vrcladder.users.User;
import ca.sfu.cmpt373.alpha.vrcladder.util.MockTeamGenerator;
import ca.sfu.cmpt373.alpha.vrcladder.util.MockUserGenerator;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

// TODO: Add more tests, testing the following:
//       - Attempting to set a preferred playtime for two teams with a common player
//       - Attempting to create a Team with a non-player role

public class TeamManagerTest extends BaseTest {

    private TeamManager teamManager;
    private Team teamFixture;

    @Before
    public void setUp() {
        teamManager = new TeamManager(sessionManager);
        teamFixture = MockTeamGenerator.generateTeam();

        Session session = sessionManager.getSession();
        Transaction transaction = session.beginTransaction();
        session.save(teamFixture.getFirstPlayer());
        session.save(teamFixture.getSecondPlayer());
        session.save(teamFixture);
        transaction.commit();
        session.close();
    }

    @Test
    public void testCreateTeamByUser() {
        Session session = sessionManager.getSession();
        User firstPlayer = MockUserGenerator.generatePlayer();
        User secondPlayer = MockUserGenerator.generatePlayer();

        // Save generated players
        Transaction transaction = session.beginTransaction();
        session.save(firstPlayer);
        session.save(secondPlayer);
        transaction.commit();

        Team newTeam = teamManager.create(firstPlayer, secondPlayer);

        Team team = session.get(Team.class, newTeam.getId());
        session.close();
        Assert.assertNotNull(team);
        Assert.assertNotNull(team.getAttendanceCard());
    }

    @Test
    public void testCreateTeamByUserId() {
        Session session = sessionManager.getSession();
        User firstPlayer = MockUserGenerator.generatePlayer();
        User secondPlayer = MockUserGenerator.generatePlayer();

        // Save generated players
        Transaction transaction = session.beginTransaction();
        session.save(firstPlayer);
        session.save(secondPlayer);
        transaction.commit();

        Team newTeam = teamManager.create(firstPlayer.getUserId(), secondPlayer.getUserId());

        Team team = session.get(Team.class, newTeam.getId());
        session.close();
        Assert.assertNotNull(team);
        Assert.assertNotNull(team.getAttendanceCard());
    }

    @Test
    public void testGetTeam() {
        Team existingTeam = teamManager.getById(teamFixture.getId());
        Assert.assertNotNull(existingTeam);

        AttendanceCard attendanceCard = existingTeam.getAttendanceCard();
        Assert.assertNotNull(attendanceCard);
        Assert.assertEquals(teamFixture.getFirstPlayer(), existingTeam.getFirstPlayer());
        Assert.assertEquals(teamFixture.getSecondPlayer(), existingTeam.getSecondPlayer());
        Assert.assertEquals(teamFixture.getAttendanceCard(), attendanceCard);
        Assert.assertEquals(teamFixture.getAttendanceCard().getPreferredPlayTime(),
            attendanceCard.getPreferredPlayTime());
    }

    @Test
    public void testUpdateTeamAttendance() {
        final PlayTime newPlayTime = PlayTime.TIME_SLOT_A;
        teamManager.updateAttendance(teamFixture.getId(), newPlayTime);

        Session session = sessionManager.getSession();
        Team team = session.get(Team.class, teamFixture.getId());
        AttendanceCard attendanceCard = session.get(AttendanceCard.class, team.getAttendanceCard().getId());
        session.close();

        Assert.assertEquals(newPlayTime, attendanceCard.getPreferredPlayTime());
    }

    @Test(expected = EntityNotFoundException.class)
    public void testUpdateNonExistentTeamAttendance() {
        final String nonExistentTeamId = "nonExistentTeamId";
        teamManager.updateAttendance(nonExistentTeamId, PlayTime.TIME_SLOT_A);
    }

    @Test
    public void testDeleteTeam() {
        Team originalTeam = teamManager.deleteById(teamFixture.getId());
        AttendanceCard originalAttendanceCard = originalTeam.getAttendanceCard();

        Session session = sessionManager.getSession();
        Team team = session.get(Team.class, teamFixture.getId());
        AttendanceCard attendanceCard = session.get(AttendanceCard.class, originalAttendanceCard.getId());
        User firstPlayer = session.get(User.class, teamFixture.getFirstPlayer().getUserId());
        User secondPlayer = session.get(User.class, teamFixture.getSecondPlayer().getUserId());
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

    @Test(expected = DuplicateTeamMemberException.class)
    public void testCreateTeamWithSamePlayer() {
        Session session = sessionManager.getSession();
        User player = MockUserGenerator.generatePlayer();

        // Should throw a DuplicateTeamMemberException.
        teamManager.create(player, player);
    }

    @Test(expected = ExistingTeamException.class)
    public void testCreateDuplicateTeam() {
        Session session = sessionManager.getSession();
        Team existingTeam = session.get(Team.class, teamFixture.getId());
        session.close();

        // Ensure team exists
        Assert.assertNotNull(existingTeam);

        // Should throw an ExistingTeamException.
        teamManager.create(existingTeam.getFirstPlayer(), existingTeam.getSecondPlayer());
    }

    @Test(expected = ExistingTeamException.class)
    public void testCreateDuplicateTeamReverseOrder() {
        Session session = sessionManager.getSession();
        Team existingTeam = session.get(Team.class, teamFixture.getId());
        session.close();

        // Ensure team exists
        Assert.assertNotNull(existingTeam);

        // Should throw an ExistingTeamException.
        // Note the reverse order of arguments from testCreateDuplicateTeam.
        teamManager.create(existingTeam.getSecondPlayer(), existingTeam.getFirstPlayer());
    }

}
