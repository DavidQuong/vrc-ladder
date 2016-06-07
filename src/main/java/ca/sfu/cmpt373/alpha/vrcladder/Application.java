package ca.sfu.cmpt373.alpha.vrcladder;

import ca.sfu.cmpt373.alpha.vrcladder.persistence.SessionManager;
import ca.sfu.cmpt373.alpha.vrcladder.teams.TeamManager;
import ca.sfu.cmpt373.alpha.vrcladder.users.UserManager;

public class Application {

    private SessionManager sessionManager;
    private UserManager userManager;
    private TeamManager teamManager;

    public Application() {
        sessionManager = new SessionManager();
        userManager = new UserManager(sessionManager);
        teamManager = new TeamManager(sessionManager);
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
}
