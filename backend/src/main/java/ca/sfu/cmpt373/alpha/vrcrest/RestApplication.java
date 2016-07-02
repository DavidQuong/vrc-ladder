package ca.sfu.cmpt373.alpha.vrcrest;

import ca.sfu.cmpt373.alpha.vrcladder.ApplicationManager;
import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.CourtManager;
import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.MatchGroupManager;
import ca.sfu.cmpt373.alpha.vrcladder.persistence.SessionManager;
import ca.sfu.cmpt373.alpha.vrcladder.teams.TeamManager;
import ca.sfu.cmpt373.alpha.vrcladder.users.UserManager;
import ca.sfu.cmpt373.alpha.vrcladder.users.authentication.SecurityManager;
import ca.sfu.cmpt373.alpha.vrcrest.routes.LadderRouter;
import ca.sfu.cmpt373.alpha.vrcrest.routes.LoginRouter;
import ca.sfu.cmpt373.alpha.vrcrest.routes.MatchGroupRouter;
import ca.sfu.cmpt373.alpha.vrcrest.routes.RestRouter;
import ca.sfu.cmpt373.alpha.vrcrest.routes.TeamRouter;
import ca.sfu.cmpt373.alpha.vrcrest.routes.UserRouter;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.impl.crypto.MacProvider;
import spark.servlet.SparkApplication;

import java.security.Key;
import java.util.Arrays;
import java.util.List;


/**
 * A class for deploying Spark on Servers/Servlet containers such as TomCat or GlassFish
 * This is used primarily for Amazon Web Services Deployment.
 * This class also contains all the initialization logic for the REST API. As such,
 * it is called in RestDriver to initialize the API even when using Spark's embedded Jetty server
 */
public class RestApplication implements SparkApplication {

    private RestApi restApi;

    @Override
    public void init() {
        SessionManager sessionManager = new SessionManager();

        // TODO - Receive these from configuration file instead.
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
        Key signatureKey = MacProvider.generateKey();

        SecurityManager securityManager = new SecurityManager(signatureAlgorithm, signatureKey);
        UserManager userManager = new UserManager(sessionManager);
        TeamManager teamManager = new TeamManager(sessionManager);
        MatchGroupManager matchGroupManager = new MatchGroupManager(sessionManager);
        CourtManager courtManager = new CourtManager(sessionManager);
        ApplicationManager appManager = new ApplicationManager(
                sessionManager,
                securityManager,
                userManager,
                teamManager,
                matchGroupManager,
                courtManager);

        LoginRouter loginRouter = new LoginRouter(appManager.getSecurityManager(), appManager.getUserManager());
        UserRouter userRouter = new UserRouter(appManager.getSecurityManager(), appManager.getUserManager());
        TeamRouter teamRouter = new TeamRouter(appManager.getTeamManager());
        MatchGroupRouter matchGroupRouter = new MatchGroupRouter(
                appManager.getMatchGroupManager(),
                appManager.getTeamManager(),
                appManager.getCourtManager());
        LadderRouter ladderRouter = new LadderRouter(
                appManager.getTeamManager(),
                appManager.getMatchGroupManager(),
                appManager.getCourtManager());
        List<RestRouter> routers = Arrays.asList(loginRouter, userRouter, teamRouter, matchGroupRouter, ladderRouter);
        restApi = new RestApi(appManager, routers);
    }

    @Override
    public void destroy() {
        restApi.shutDown();
    }

    public static void main(String[] args) {
        RestApplication restApplication = new RestApplication();
        restApplication.init();
    }

}
