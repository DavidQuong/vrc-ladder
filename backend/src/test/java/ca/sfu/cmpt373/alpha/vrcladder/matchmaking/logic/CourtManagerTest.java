package ca.sfu.cmpt373.alpha.vrcladder.matchmaking.logic;

import ca.sfu.cmpt373.alpha.vrcladder.BaseTest;
import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.Court;
import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.CourtManager;
import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.MatchGroup;
import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.PlayTime;
import ca.sfu.cmpt373.alpha.vrcladder.util.MockMatchGroupGenerator;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class CourtManagerTest extends BaseTest {
    private CourtManager courtManager;
    private MatchGroup threeTeamMatchGroupFixture;
    private MatchGroup fourTeamMatchGroupFixture;
    private Court courtFixture;

    @Before
    public void setUp() {
        courtManager = new CourtManager(sessionManager);
        threeTeamMatchGroupFixture = MockMatchGroupGenerator.generateThreeTeamMatchGroup();
        fourTeamMatchGroupFixture = MockMatchGroupGenerator.generateFourTeamMatchGroup();

        Session session = sessionManager.getSession();
        Transaction transaction = session.beginTransaction();
        saveTeams(threeTeamMatchGroupFixture.getTeams(), session);
        session.save(threeTeamMatchGroupFixture);
        saveTeams(fourTeamMatchGroupFixture.getTeams(), session);
        session.save(fourTeamMatchGroupFixture);

        courtFixture = new Court();
        session.save(courtFixture);

        transaction.commit();
        session.close();
    }

    private void saveTeams(List<Team> teams, Session session) {
        for (Team team : teams) {
            session.save(team.getFirstPlayer());
            session.save(team.getSecondPlayer());
            session.save(team);
        }
    }

    @Test
    public void testCreateCourt() {
        Court court = new Court();
        court.scheduleMatch(threeTeamMatchGroupFixture, PlayTime.TIME_SLOT_A);
        court.scheduleMatch(fourTeamMatchGroupFixture, PlayTime.TIME_SLOT_B);
        Court newCourt = courtManager.create(court);

        Session session = sessionManager.getSession();
        Court retrievedCourt = session.get(Court.class, newCourt.getId());
        session.close();

        Assert.assertEquals(retrievedCourt, newCourt);

        Assert.assertEquals(
                retrievedCourt.getScheduledMatch(PlayTime.TIME_SLOT_A),
                newCourt.getScheduledMatch(PlayTime.TIME_SLOT_A));

        Assert.assertEquals(
                retrievedCourt.getScheduledMatch(PlayTime.TIME_SLOT_B),
                newCourt.getScheduledMatch(PlayTime.TIME_SLOT_B));
    }

    @Test
    public void testDeleteCourt() {
        Session session = sessionManager.getSession();
        Court retrievedCourt = session.get(Court.class, courtFixture.getId());
        session.close();
        Assert.assertNotNull(retrievedCourt);

        courtManager.delete(courtFixture);

        session = sessionManager.getSession();
        retrievedCourt = session.get(Court.class, courtFixture.getId());
        session.close();
        Assert.assertNull(retrievedCourt);
    }

    @Test
    public void testUpdateCourt() {
        courtFixture.scheduleMatch(fourTeamMatchGroupFixture, PlayTime.TIME_SLOT_A);
        courtFixture.scheduleMatch(threeTeamMatchGroupFixture, PlayTime.TIME_SLOT_B);

        courtManager.update(courtFixture);

        Session session = sessionManager.getSession();
        Court retrievedCourt = session.get(Court.class, courtFixture.getId());
        session.close();

        Assert.assertEquals(retrievedCourt, courtFixture);

        Assert.assertEquals(
                courtFixture.getScheduledMatch(PlayTime.TIME_SLOT_A),
                retrievedCourt.getScheduledMatch(PlayTime.TIME_SLOT_A));

        Assert.assertEquals(
                courtFixture.getScheduledMatch(PlayTime.TIME_SLOT_B),
                retrievedCourt.getScheduledMatch(PlayTime.TIME_SLOT_B));
    }

    @Test
    public void testGetCourt() {
        Court retrievedCourt = courtManager.getById(courtFixture.getId());
        Assert.assertEquals(retrievedCourt, courtFixture);
    }

    @Test
    public void testDeleteAllCourts() {
        Court newCourt = new Court();

        Session session = sessionManager.getSession();
        Transaction transaction = session.beginTransaction();
        session.save(newCourt);
        transaction.commit();

        transaction = session.beginTransaction();
        Court retrievedNewCourt = session.get(Court.class, newCourt.getId());
        Court retrievedCourtFixture = session.get(Court.class, courtFixture.getId());
        transaction.commit();
        Assert.assertEquals(retrievedNewCourt, newCourt);
        Assert.assertEquals(retrievedCourtFixture, courtFixture);

        courtManager.deleteAll();


        Assert.assertEquals(0, courtManager.getAll().size());
        //TODO: make this work without depending on courtManager.getAll()
//        transaction = session.beginTransaction();
//        retrievedNewCourt = session.get(Court.class, newCourt.getId());
//        retrievedCourtFixture = session.get(Court.class, courtFixture.getId());
//        transaction.commit();
//        Assert.assertNull(retrievedNewCourt);
//        Assert.assertNull(retrievedCourtFixture);

        session.close();
    }
}
