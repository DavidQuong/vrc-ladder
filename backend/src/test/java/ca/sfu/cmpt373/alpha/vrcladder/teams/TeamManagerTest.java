package ca.sfu.cmpt373.alpha.vrcladder.teams;

import ca.sfu.cmpt373.alpha.vrcladder.BaseTest;
import ca.sfu.cmpt373.alpha.vrcladder.exceptions.DuplicateTeamMemberException;
import ca.sfu.cmpt373.alpha.vrcladder.exceptions.ExistingTeamException;
import ca.sfu.cmpt373.alpha.vrcladder.exceptions.MultiplePlayTimeException;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.AttendanceCard;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.AttendanceStatus;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.PlayTime;
import ca.sfu.cmpt373.alpha.vrcladder.users.User;
import ca.sfu.cmpt373.alpha.vrcladder.util.GeneratedId;
import ca.sfu.cmpt373.alpha.vrcladder.util.MockTeamGenerator;
import ca.sfu.cmpt373.alpha.vrcladder.util.MockUserGenerator;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import javax.persistence.EntityNotFoundException;
import java.util.List;

// TODO: Add more tests, testing the following:
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

    @After
    public void tearDown() {
        MockUserGenerator.resetUserCount();
        MockTeamGenerator.resetTeamCount();
    }

    @Test
    public void testCreateTeamByUser() {
        User firstPlayer = MockUserGenerator.generatePlayer();
        User secondPlayer = MockUserGenerator.generatePlayer();

        // Save generated players
        Session session = sessionManager.getSession();
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
        User firstPlayer = MockUserGenerator.generatePlayer();
        User secondPlayer = MockUserGenerator.generatePlayer();

        // Save generated players
        Session session = sessionManager.getSession();
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
    public void testMultipleCreateTeams() {
        Session session = sessionManager.getSession();
        final int teamsCreated = 4;
        for (int i = 0; i < teamsCreated; i++) {
            Transaction transaction = session.beginTransaction();
            User firstPlayer = MockUserGenerator.generatePlayer();
            User secondPlayer = MockUserGenerator.generatePlayer();
            session.save(firstPlayer);
            session.save(secondPlayer);
            transaction.commit();

            teamManager.create(firstPlayer, secondPlayer);
        }

        List<Team> teams = teamManager.getAll();
        // Account for team created in setUp().
        Assert.assertEquals(teamsCreated + 1, teams.size());

        Integer positionCount = LadderPosition.FIRST_POSITION;
        for (Team team : teams) {
            LadderPosition expectedLadderPosition = new LadderPosition(positionCount);
            Assert.assertEquals(expectedLadderPosition, team.getLadderPosition());
            positionCount++;
        }
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
        final PlayTime newPlayTime1 = PlayTime.TIME_SLOT_A;
        final PlayTime newPlayTime2 = PlayTime.TIME_SLOT_B;
        final PlayTime newPlayTime3 = PlayTime.NONE;
        teamManager.updateAttendancePlaytime(teamFixture.getId(), newPlayTime1);

        Session session = sessionManager.getSession();
        Team team = session.get(Team.class, teamFixture.getId());

        AttendanceCard attendanceCard = session.get(AttendanceCard.class, team.getAttendanceCard().getId());
        Assert.assertEquals(newPlayTime1, attendanceCard.getPreferredPlayTime());
        session.clear();

        teamManager.updateAttendancePlaytime(teamFixture.getId(), newPlayTime2);
        session.refresh(attendanceCard);
        Assert.assertEquals(newPlayTime2, attendanceCard.getPreferredPlayTime());
        session.clear();

        teamManager.updateAttendancePlaytime(teamFixture.getId(), newPlayTime3);
        session.refresh(attendanceCard);
        Assert.assertEquals(newPlayTime3, attendanceCard.getPreferredPlayTime());
        session.close();
    }

    @Test
    public void testUpdateAttendanceStatus(){
        final AttendanceStatus newAttStatus1 = AttendanceStatus.PRESENT;
        final AttendanceStatus newAttStatus2 = AttendanceStatus.LATE;
        final AttendanceStatus newAttStatus3 = AttendanceStatus.NO_SHOW;
        teamManager.updateAttendanceStatus(teamFixture.getId(), newAttStatus1);

        Session session = sessionManager.getSession();
        Team team = session.get(Team.class, teamFixture.getId());

        AttendanceCard attendanceCard = session.get(AttendanceCard.class, team.getAttendanceCard().getId());
        Assert.assertEquals(newAttStatus1, attendanceCard.getAttendanceStatus());
        session.clear();

        teamManager.updateAttendanceStatus(teamFixture.getId(), newAttStatus2);
        session.refresh(attendanceCard);
        Assert.assertEquals(newAttStatus2, attendanceCard.getAttendanceStatus());
        session.clear();


        teamManager.updateAttendanceStatus(teamFixture.getId(), newAttStatus3);
        session.refresh(attendanceCard);
        Assert.assertEquals(newAttStatus3, attendanceCard.getAttendanceStatus());
        session.close();
    }

    @Test(expected = MultiplePlayTimeException.class)
    public void testUpdateTeamAttendanceWithAlreadyAttendingPlayer() {
        User commonPlayer = teamFixture.getFirstPlayer();
        User newMockPlayer = MockUserGenerator.generatePlayer();
        LadderPosition ladderPosition = MockTeamGenerator.generateLadderPosition();
        Team newTeam = new Team(commonPlayer, newMockPlayer, ladderPosition);

        Session session = sessionManager.getSession();
        Transaction transaction = session.beginTransaction();

        session.save(newMockPlayer);
        session.save(newTeam);

        transaction.commit();
        session.close();

        teamManager.updateAttendancePlaytime(teamFixture.getId(), PlayTime.TIME_SLOT_A);
        teamManager.updateAttendancePlaytime(newTeam.getId(), PlayTime.TIME_SLOT_B);
    }

    @Test(expected = EntityNotFoundException.class)
    public void testUpdateNonExistentTeamAttendance() {
        final GeneratedId nonExistentTeamId = new GeneratedId();
        teamManager.updateAttendancePlaytime(nonExistentTeamId, PlayTime.TIME_SLOT_A);
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

    @Test
    public void testUpdateLadderPositionsAfterDeleteTeam() {
        //note with the teamFixture, there will be +1 team in the database
        int teamCount = 3;
        List<Team> teamsToAdd = new ArrayList<>();
        for (int i = 0; i < teamCount; i++) {
            teamsToAdd.add(MockTeamGenerator.generateTeam());
        }

        Session session = sessionManager.getSession();
        Transaction transaction = session.beginTransaction();
        saveTeams(teamsToAdd, session);
        transaction.commit();
        session.close();

        int teamToDeleteIndex = 0;
        Team teamToDelete = teamsToAdd.get(teamToDeleteIndex);

        teamManager.deleteById(teamToDelete.getId());

        List<Team> teamsAfterDeletion = teamManager.getAll();

        Assert.assertTrue(!teamsAfterDeletion.contains(teamToDelete));
        for (Integer position = 1; position <= teamsAfterDeletion.size(); position++) {
            Team team = teamsAfterDeletion.get(position - 1);
            Integer teamLadderPosition = team.getLadderPosition().getValue();
            Assert.assertEquals(teamLadderPosition, position);
        }
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

    @Test
    public void testUpdateLadderPositions() {
        List<Team> teams = new ArrayList<>();
        int additionalTeamCount = 1;
        for (int i = 0; i < additionalTeamCount; i++) {
            teams.add(MockTeamGenerator.generateTeam());
        }

        Session session = sessionManager.getSession();
        Transaction transaction = session.beginTransaction();
        saveTeams(teams, session);
        transaction.commit();

        //add the teamFixture in so we are testing with all teams
        teams.add(0, teamFixture);

        //for this test we want to set the rankings in reverse order
        Collections.reverse(teams);

        teamManager.updateLadderPositions(teams);

        List<Team> newRankedTeams = teamManager.getAll();

        for (int i = 0; i < teams.size(); i++) {
            Assert.assertEquals(teams.get(i), newRankedTeams.get(i));
        }
    }

    private void saveTeams(List<Team> teams, Session session) {
        for (Team team : teams) {
            session.save(team.getFirstPlayer());
            session.save(team.getSecondPlayer());
            session.save(team);
        }
    }
}
