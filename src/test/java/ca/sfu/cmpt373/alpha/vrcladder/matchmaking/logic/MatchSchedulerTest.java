package ca.sfu.cmpt373.alpha.vrcladder.matchmaking.logic;

import ca.sfu.cmpt373.alpha.vrcladder.db.MockDatabase;
import ca.sfu.cmpt373.alpha.vrcladder.exceptions.MatchMakingException;
import org.junit.Test;


public class MatchSchedulerTest {
    private static final int NUM_COURTS = 6;

    //TODO: add more tests!

    @Test
    public void testMaxTeamCount() {
        int testTeamCount = 38;
        MatchScheduler.scheduleMatches(
                NUM_COURTS,
                MatchGroupGenerator.generateMatchGroupings(MockDatabase.getRankedLadderTeams(testTeamCount)));
    }

    @Test (expected = MatchMakingException.class)
    public void testCourtsFull() {
        int testTeamCount = 39;
        MatchScheduler.scheduleMatches(
                NUM_COURTS,
                MatchGroupGenerator.generateMatchGroupings(MockDatabase.getRankedLadderTeams(testTeamCount)));
    }
}
