package ca.sfu.cmpt373.alpha.vrcladder.matchmaking.logic;

import ca.sfu.cmpt373.alpha.vrcladder.exceptions.MatchMakingException;
import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.Court;
import ca.sfu.cmpt373.alpha.vrcladder.persistance.MockDatabase;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.PlayTime;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;


public class MatchSchedulerTest {
    private static final int NUM_COURTS = 6;

    //TODO: add more tests!

    @Test
    public void testMaxTeamCount() {
        int testGroupCount = 12;
        List<Court> courts = MatchScheduler.scheduleMatches(
                NUM_COURTS,
                MockDatabase.getMockMatchGroups(testGroupCount));

        //check that all courts are filled
        for (Court court : courts) {
            for (PlayTime playTime : PlayTime.values()) {
                if (playTime.isPlayable()) {
                    Assert.assertTrue(!court.isPlayTimeFree(playTime));
                }
            }
        }
    }

    @Test (expected = MatchMakingException.class)
    public void testCourtsFull() {
        int testGroupCount = 13;
        MatchScheduler.scheduleMatches(
                NUM_COURTS,
                MockDatabase.getMockMatchGroups(testGroupCount));
    }
}
