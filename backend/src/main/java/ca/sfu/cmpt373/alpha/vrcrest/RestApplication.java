package ca.sfu.cmpt373.alpha.vrcrest;

import ca.sfu.cmpt373.alpha.vrcladder.ApplicationManager;
import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.MatchGroupManager;
import ca.sfu.cmpt373.alpha.vrcladder.persistence.SessionManager;
import ca.sfu.cmpt373.alpha.vrcladder.teams.TeamManager;
import ca.sfu.cmpt373.alpha.vrcladder.users.UserManager;
import ca.sfu.cmpt373.alpha.vrcladder.users.authentication.PasswordManager;
import ca.sfu.cmpt373.alpha.vrcrest.routes.RestRouter;
import ca.sfu.cmpt373.alpha.vrcrest.routes.TeamRouter;
import ca.sfu.cmpt373.alpha.vrcrest.routes.UserRouter;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.config.IniSecurityManagerFactory;
import org.apache.shiro.mgt.SecurityManager;
import org.apache.shiro.util.Factory;
import spark.servlet.SparkApplication;

import java.util.Arrays;
import java.util.List;


/**
 * A class for deploying Spark on Servers/Servlet containers such as TomCat or GlassFish
 * This is used primarily for Amazon Web Services Deployment
 */
public class RestApplication implements SparkApplication {

    private RestApi restApi;

    @Override
    public void init() {
        SessionManager sessionManager = new SessionManager();
        PasswordManager passwordManager = new PasswordManager();
        UserManager userManager = new UserManager(sessionManager);
        TeamManager teamManager = new TeamManager(sessionManager);
        MatchGroupManager matchGroupManager = new MatchGroupManager(sessionManager);
        ApplicationManager appManager = new ApplicationManager(sessionManager, passwordManager, userManager,
            teamManager, matchGroupManager);

        UserRouter userRouter = new UserRouter(appManager.getUserManager());
        TeamRouter teamRouter = new TeamRouter(appManager.getTeamManager());
        List<RestRouter> routers = Arrays.asList(userRouter, teamRouter);
        restApi = new RestApi(appManager, routers);
    }

    @Override
    public void destroy() {
        restApi.shutDown();
    }

}
