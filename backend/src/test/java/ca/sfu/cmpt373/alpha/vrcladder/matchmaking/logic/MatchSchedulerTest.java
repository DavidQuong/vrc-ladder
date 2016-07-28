package ca.sfu.cmpt373.alpha.vrcladder.matchmaking.logic;

import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.Court;
import ca.sfu.cmpt373.alpha.vrcladder.persistence.MockDatabase;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.PlayTime;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;


public class MatchSchedulerTest {

    @Test
    public void testMaxTeamCount() {
        int testGroupCount = 12;
        List<Court> courts = MatchScheduler.scheduleMatches(MockDatabase.getMockMatchGroups(testGroupCount));

        //check that all courts are filled
        for (Court court : courts) {
            for(PlayTime playTime : PlayTime.values()) {
                if(playTime.isPlayable()) {
                    Assert.assertTrue(!court.isPlayTimeFree(playTime));
                }
            }
        }
    }
}
