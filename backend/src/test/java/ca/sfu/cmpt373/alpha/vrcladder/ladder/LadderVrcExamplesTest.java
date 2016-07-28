package ca.sfu.cmpt373.alpha.vrcladder.ladder;

import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.MatchGroup;
import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.logic.MatchGroupGenerator;
import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.AttendanceStatus;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.PlayTime;
import ca.sfu.cmpt373.alpha.vrcladder.util.MockTeamGenerator;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class LadderVrcExamplesTest {
    /**
     * A Testcase example provided by the vrc
     */
    @Test
    public void testUpdateLadder() {
        int numTeams = 19;
        MockTeamGenerator.resetTeamCount();
        List<Team> teams = new ArrayList<>();
        for (int i = 0; i < numTeams; i++) {
            teams.add(MockTeamGenerator.generateTeam());
        }

        int lastAttendingTeamIndex = 19;
        for (int i = 0; i < lastAttendingTeamIndex; i++) {
            teams.get(i).getAttendanceCard().setPreferredPlayTime(PlayTime.TIME_SLOT_A);
        }

        int thirdTeamIndex = 2;
        teams.get(thirdTeamIndex).getAttendanceCard().setPreferredPlayTime(PlayTime.NONE);

        int[] noShowTeamIndices = new int[] {0, 4};
        for (int noShowTeamIndex : noShowTeamIndices) {
            teams.get(noShowTeamIndex).getAttendanceCard().setAttendanceStatus(AttendanceStatus.NO_SHOW);
        }

        //Note that our algorithm applies Penalties differently than in the VRC example, so the expected results have
        //been changed to reflect this. In our penalties algorithm, teams actually end up moving down the correct number
        //of spots (from their original position) that their penalties enforce. In the vrc's example, teams
        //don't always get moved down the correct number of spots from their original position (ex: in the vrc's example,
        //team 3 only moved one spot down from its original position).
        int[] expectedTeamIndices = new int[] {1, 3, 5, 6, 2, 7, 8, 9, 10, 11, 12, 13, 14, 15, 0, 16, 17, 4, 18};
        List<Team> expectedTeams = new ArrayList<>();
        for (int expectedTeamIndex : expectedTeamIndices) {
            expectedTeams.add(teams.get(expectedTeamIndex));
        }

        //This situation would never occur in real scenarios since the MatchGroupGenerator would generate
        //MatchGroups for all attending teams. Even if it was generating MatchGroups for just the first 9 teams,
        // (with 1 team away), it would generate 2 MatchGroups of 4 Teams each
        List<MatchGroup> matchGroups = new ArrayList<>();
        matchGroups.add(new MatchGroup(
                teams.get(0),
                teams.get(1),
                teams.get(3)));
        matchGroups.add(new MatchGroup(
                teams.get(4),
                teams.get(5),
                teams.get(6),
                teams.get(7)));
        for (MatchGroup matchGroup : matchGroups) {
            matchGroup.getScoreCard().setRankedTeams(matchGroup.getTeams());
        }

        Ladder ladder = new Ladder(teams);
        ladder.updateLadder(matchGroups);
        for (int i = 0; i < ladder.getLadder().size(); i++) {
            Assert.assertEquals(ladder.getLadder().get(i), expectedTeams.get(i));
        }
    }

    @Test
    public void testVrcPictureExample() {
        //generate teams
        int numTeams = 20;
        MockTeamGenerator.resetTeamCount();
        List<Team> teams = new ArrayList<>();
        for (int i = 0; i < numTeams; i++) {
            teams.add(MockTeamGenerator.generateTeam());
        }

        //set attending/non-attending teams
        for (Team team : teams) {
            team.getAttendanceCard().setPreferredPlayTime(PlayTime.TIME_SLOT_A);
        }
        int[] nonAttendingTeamRankings = new int[]{4, 5, 7, 8, 11, 12, 15};
        for (int nonAttendingTeamRanking : nonAttendingTeamRankings) {
            teams.get(nonAttendingTeamRanking - 1).getAttendanceCard().setPreferredPlayTime(PlayTime.NONE);
        }

        //set team 3 as a no show
        int team3Index = 2;
        teams.get(team3Index).getAttendanceCard().setAttendanceStatus(AttendanceStatus.NO_SHOW);

        //generate matchgroups and set matchgroup results
        //every matchgroup's results didn't change within the matchgroup except for matchgroup 3
        List<MatchGroup> matchGroups = MatchGroupGenerator.generateMatchGroupings(teams);
        for (MatchGroup matchGroup : matchGroups) {
            matchGroup.getScoreCard().setRankedTeams(matchGroup.getTeams());
        }
        int thirdMatchGroupIndex = 2;
        MatchGroup thirdMatchGroup = matchGroups.get(thirdMatchGroupIndex);
        List<Team> rankedTeams = new ArrayList<>();
        rankedTeams.add(thirdMatchGroup.getTeam2());
        rankedTeams.add(thirdMatchGroup.getTeam3());
        rankedTeams.add(thirdMatchGroup.getTeam1());
        thirdMatchGroup.getScoreCard().setRankedTeams(rankedTeams);

        //calculate new rankings
        Ladder ladder = new Ladder(teams);
        ladder.updateLadder(matchGroups);

        //make sure new rankings are as expected
        List<Team> newRankings = ladder.getLadder();
        int[] expectedResults = new int[]{1, 2, 6, 9, 14, 4, 5, 10, 7, 8, 16, 17, 11, 12, 13, 3, 15, 18, 19, 20};
        for (int i = 0; i < newRankings.size(); i++) {
            Assert.assertEquals(newRankings.get(i), teams.get(expectedResults[i] - 1));
        }
    }
}
