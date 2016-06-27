package ca.sfu.cmpt373.alpha.vrcladder;

import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.MatchGroupManager;
import ca.sfu.cmpt373.alpha.vrcladder.persistence.SessionManager;
import ca.sfu.cmpt373.alpha.vrcladder.teams.TeamManager;
import ca.sfu.cmpt373.alpha.vrcladder.users.UserManager;
import ca.sfu.cmpt373.alpha.vrcladder.users.authentication.Password;
import ca.sfu.cmpt373.alpha.vrcladder.users.authentication.PasswordManager;

public class ApplicationManager {

    private SessionManager sessionManager;
    private PasswordManager passwordManager;
    private UserManager userManager;
    private TeamManager teamManager;
    private MatchGroupManager matchGroupManager;

    public ApplicationManager(SessionManager sessionManager, PasswordManager passwordManager, UserManager userManager,
        TeamManager teamManager, MatchGroupManager matchGroupManager) {
        this.sessionManager = sessionManager;
        this.passwordManager = passwordManager;
        this.userManager = userManager;
        this.teamManager = teamManager;
        this.matchGroupManager = matchGroupManager;
    }

    public void shutDown() {
        sessionManager.shutDown();
    }

    public PasswordManager getPasswordManager() {
        return passwordManager;
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
