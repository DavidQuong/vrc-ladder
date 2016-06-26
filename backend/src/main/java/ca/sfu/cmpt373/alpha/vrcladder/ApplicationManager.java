package ca.sfu.cmpt373.alpha.vrcladder;

import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.MatchGroupManager;
import ca.sfu.cmpt373.alpha.vrcladder.persistence.SessionManager;
import ca.sfu.cmpt373.alpha.vrcladder.teams.TeamManager;
import ca.sfu.cmpt373.alpha.vrcladder.users.UserManager;

public class ApplicationManager {

    private SessionManager sessionManager;
    private UserManager userManager;
    private TeamManager teamManager;
    private MatchGroupManager matchGroupManager;

    public ApplicationManager() {
        sessionManager = new SessionManager();
        userManager = new UserManager(sessionManager);
        teamManager = new TeamManager(sessionManager);
        matchGroupManager = new MatchGroupManager(sessionManager);
    }

    public ApplicationManager(SessionManager sessionManager, UserManager userManager, TeamManager teamManager,
        MatchGroupManager matchGroupManager) {
        this.sessionManager = sessionManager;
        this.userManager = userManager;
        this.teamManager = teamManager;
        this.matchGroupManager = matchGroupManager;
    }

    public void shutDown() {
        sessionManager.shutDown();
    }

    public UserManager getUserManager() {
        return userManager;
    }

    public TeamManager getTeamManager() {
        return teamManager;
    }

    public MatchGroupManager getMatchGroupManager() {
        return matchGroupManager;
    }

}
