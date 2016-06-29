package ca.sfu.cmpt373.alpha.vrcladder;

import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.CourtManager;
import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.MatchGroupManager;
import ca.sfu.cmpt373.alpha.vrcladder.persistence.SessionManager;
import ca.sfu.cmpt373.alpha.vrcladder.teams.TeamManager;
import ca.sfu.cmpt373.alpha.vrcladder.users.UserManager;
import ca.sfu.cmpt373.alpha.vrcladder.users.authentication.SecurityManager;

public class ApplicationManager {

    private SessionManager sessionManager;
    private SecurityManager securityManager;
    private UserManager userManager;
    private TeamManager teamManager;
    private MatchGroupManager matchGroupManager;
    private CourtManager courtManager;

    public ApplicationManager(SessionManager sessionManager, SecurityManager securityManager, UserManager userManager,
        TeamManager teamManager, MatchGroupManager matchGroupManager, CourtManager courtManager) {
        this.sessionManager = sessionManager;
        this.securityManager = securityManager;
        this.userManager = userManager;
        this.teamManager = teamManager;
        this.matchGroupManager = matchGroupManager;
        this.courtManager = courtManager;
    }

    public void shutDown() {
        sessionManager.shutDown();
    }

    public SecurityManager getSecurityManager() {
        return securityManager;
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
