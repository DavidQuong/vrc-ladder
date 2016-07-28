package ca.sfu.cmpt373.alpha.vrcladder.matchmaking.logic;

import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.Court;
import ca.sfu.cmpt373.alpha.vrcladder.persistence.MockDatabase;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.PlayTime;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;


public class MatchSchedulerTest {

    @Test
    public void testOneTimeTeamCount() {
        int testGroupCount = 12;
        List<Court> courts = MatchScheduler.scheduleMatches(MockDatabase.getMockMatchGroups(testGroupCount));

        //check that all courts are at 8:00
        for(Court court : courts) {
            Assert.assertTrue(!court.isPlayTimeFree(PlayTime.TIME_SLOT_A));
        }
    }

    @Test
    public void testTwoTimesTeamCount() {
        int testGroupCount = 13;
        List<Court> courts = MatchScheduler.scheduleMatches(MockDatabase.getMockMatchGroups(testGroupCount));

        int slotATeamCount = 0;
        int slotBTeamCount = 0;

        for(Court court : courts) {
            if(!court.isPlayTimeFree(PlayTime.TIME_SLOT_A)) {
                slotATeamCount++;
            } else if(!court.isPlayTimeFree(PlayTime.TIME_SLOT_B)) {
                slotBTeamCount++;
            } else {
                Assert.assertTrue(false);
            }
        }

        Assert.assertTrue(Math.abs(slotATeamCount - slotBTeamCount) <= 1);
        Assert.assertTrue(slotATeamCount + slotBTeamCount == 13);
    }
}
