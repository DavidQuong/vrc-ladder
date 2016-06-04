package ca.sfu.cmpt373.alpha.vrcladder.matchmaking;

import ca.sfu.cmpt373.alpha.vrcladder.persistence.SessionManager;
import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
import ca.sfu.cmpt373.alpha.vrcladder.teams.TeamManager;
import ca.sfu.cmpt373.alpha.vrcladder.users.UserManager;
import ca.sfu.cmpt373.alpha.vrcladder.users.authorization.UserRole;
import org.junit.Test;

import java.util.List;

public class MatchGroupManagerTest {
    private static final String MOCK_MATCH_GROUP_ID = "1";

    @Test
    public void testGetMatchGroup() {
        MatchGroupManager matchGroupManager = new MatchGroupManager(new SessionManager());
        MatchGroup matchGroup = matchGroupManager.getMatchGroup(MOCK_MATCH_GROUP_ID);
        List<Team> teams = matchGroup.getTeams();
        for (Team team : teams) {
            System.out.println(team.getFirstPlayer().getDisplayName());
        }
    }

    @Test
    public void testCreateMatchGroup() {
        SessionManager sessionManager = new SessionManager();
        UserManager userManager = new UserManager(sessionManager);
        TeamManager teamManager = new TeamManager(sessionManager);
        MatchGroupManager matchGroupManager = new MatchGroupManager(new SessionManager());
        Team team1 = teamManager.createTeam(
                userManager.createUser("3", UserRole.PLAYER, "Trevor", "Clelland", "tclellan@sfu.ca", "604-469-2021"),
                userManager.createUser("4", UserRole.PLAYER, "Meow", "Clelland", "meow@sfu.ca", "111-111-1111"));
        Team team2 = teamManager.createTeam(
                userManager.createUser("5", UserRole.PLAYER, "Trevor", "Clelland", "tclellan@sfu.ca", "604-469-2021"),
                userManager.createUser("6", UserRole.PLAYER, "Meow", "Clelland", "meow@sfu.ca", "111-111-1111"));
        Team team3 = teamManager.createTeam(
                userManager.createUser("7", UserRole.PLAYER, "Trevor", "Clelland", "tclellan@sfu.ca", "604-469-2021"),
                userManager.createUser("8", UserRole.PLAYER, "Meow", "Clelland", "meow@sfu.ca", "111-111-1111"));
        MatchGroup matchGroup = matchGroupManager.createMatchGroup(team1, team2, team3);
        for (Team team : matchGroup.getTeams()) {
            System.out.println(team.getFirstPlayer().getUserId());
        }
    }
}
