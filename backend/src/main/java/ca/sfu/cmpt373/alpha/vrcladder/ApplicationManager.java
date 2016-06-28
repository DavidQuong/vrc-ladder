package ca.sfu.cmpt373.alpha.vrcladder;

import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.CourtManager;
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
    private CourtManager courtManager;

    public ApplicationManager(SessionManager sessionManager, PasswordManager passwordManager, UserManager userManager,
        TeamManager teamManager, MatchGroupManager matchGroupManager, CourtManager courtManager) {
        this.sessionManager = sessionManager;
        this.passwordManager = passwordManager;
        this.userManager = userManager;
        this.teamManager = teamManager;
        this.matchGroupManager = matchGroupManager;
        this.courtManager = courtManager;
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

    public CourtManager getCourtManager() {
        return courtManager;
    }

}
