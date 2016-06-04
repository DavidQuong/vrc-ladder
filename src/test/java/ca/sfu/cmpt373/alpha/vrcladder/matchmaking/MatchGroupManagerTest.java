package ca.sfu.cmpt373.alpha.vrcladder.matchmaking;

import ca.sfu.cmpt373.alpha.vrcladder.BaseTest;
import ca.sfu.cmpt373.alpha.vrcladder.exceptions.PersistenceException;
import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
import ca.sfu.cmpt373.alpha.vrcladder.teams.TeamManager;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class MatchGroupManagerTest extends BaseTest {
    //MOCK_MATCH_GROUP_TEAM_IDS = ids of teams that exist inside MOCK_MATCH_GROUP_ID
    private static final String MOCK_MATCH_GROUP_ID = "1";
    private static final String[] MOCK_MATCH_GROUP_TEAM_IDS = new String [] {"1", "2", "3"};
    //teams that don't exist in any current group
    private static final String[] MOCK_TEAM_IDS = new String[] {"4", "5", "6", "7"};

    @Test
    public void testGetMatchGroup() {
        MatchGroupManager matchGroupManager = new MatchGroupManager(sessionManager);
        MatchGroup matchGroup = matchGroupManager.getMatchGroup(MOCK_MATCH_GROUP_ID);
        List<Team> teams = matchGroup.getTeams();
        for (int i = 0; i < 3; i++) {
            Assert.assertTrue(teams.get(i).getId().equals(MOCK_MATCH_GROUP_TEAM_IDS[i]));
        }
    }

    @Test
    public void testCreateMatchGroup() {
        //TODO: figure out how to test without depending on TeamManager
        MatchGroupManager matchGroupManager = new MatchGroupManager(sessionManager);
        TeamManager teamManager = new TeamManager(sessionManager);

        List<Team> teams = new ArrayList<>();
        for (String teamId : MOCK_TEAM_IDS) {
            teams.add(teamManager.getTeam(teamId));
        }

        MatchGroup matchGroupCreated = matchGroupManager.createMatchGroup(teams);
        MatchGroup matchGroupRetrieved = matchGroupManager.getMatchGroup(matchGroupCreated.getId());
        Assert.assertTrue(matchGroupCreated.getId().equals(matchGroupRetrieved.getId()));
    }

    @Test (expected = PersistenceException.class)
    public void testDeleteMatchGroup() {
        MatchGroupManager matchGroupManager = new MatchGroupManager(sessionManager);
        matchGroupManager.deleteMatchGroup(MOCK_MATCH_GROUP_ID);

        //this should throw a PersistenceException when trying to retrieve an object that doesn't exist
        MatchGroup matchGroup = matchGroupManager.getMatchGroup(MOCK_MATCH_GROUP_ID);
    }
}
