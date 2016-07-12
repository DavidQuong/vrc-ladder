package ca.sfu.cmpt373.alpha.vrcrest;

import ca.sfu.cmpt373.alpha.vrcladder.ApplicationManager;
import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.CourtManager;
import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.MatchGroupManager;
import ca.sfu.cmpt373.alpha.vrcladder.persistence.SessionManager;
import ca.sfu.cmpt373.alpha.vrcladder.teams.TeamManager;
import ca.sfu.cmpt373.alpha.vrcladder.users.UserManager;
import ca.sfu.cmpt373.alpha.vrcladder.users.authentication.SecurityManager;
import ca.sfu.cmpt373.alpha.vrcladder.util.ConfigurationManager;
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
 * This is used for Amazon Web Services Deployment using TomCat, as well as local deployment for dev/testing.
 * It contains all the initialization logic for the REST API.
 * There is also a main() method, which when called starts Spark's embedded Jetty server locally for dev/testing
 */
public class RestApplication implements SparkApplication {

    private RestApi restApi;

    @Override
    public void init() {
        ConfigurationManager configurationManager = new ConfigurationManager();
        SessionManager sessionManager = new SessionManager(configurationManager);

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
        UserRouter userRouter = new UserRouter(
                appManager.getSecurityManager(),
                appManager.getUserManager(),
                appManager.getTeamManager());
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
