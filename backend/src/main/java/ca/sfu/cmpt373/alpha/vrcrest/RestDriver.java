package ca.sfu.cmpt373.alpha.vrcrest;

import ca.sfu.cmpt373.alpha.vrcladder.ApplicationManager;
import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.MatchGroupManager;
import ca.sfu.cmpt373.alpha.vrcladder.persistence.SessionManager;
import ca.sfu.cmpt373.alpha.vrcladder.teams.TeamManager;
import ca.sfu.cmpt373.alpha.vrcladder.users.UserManager;
import ca.sfu.cmpt373.alpha.vrcrest.interfaces.RestRouter;
import ca.sfu.cmpt373.alpha.vrcrest.routes.TeamRouter;
import ca.sfu.cmpt373.alpha.vrcrest.routes.UserRouter;

import java.util.Arrays;
import java.util.List;

public class RestDriver {

    public static void main(String[] args) {
        SessionManager sessionManager = new SessionManager();
        UserManager userManager = new UserManager(sessionManager);
        TeamManager teamManager = new TeamManager(sessionManager);
        MatchGroupManager matchGroupManager = new MatchGroupManager(sessionManager);
        ApplicationManager appManager = new ApplicationManager(sessionManager, userManager, teamManager,
            matchGroupManager);

        UserRouter userRouter = new UserRouter(appManager.getUserManager());
        TeamRouter teamRouter = new TeamRouter(appManager.getTeamManager());
        List<RestRouter> routers = Arrays.asList(userRouter, teamRouter);
        Restapi restapi = new Restapi(appManager, routers);
    }

}
