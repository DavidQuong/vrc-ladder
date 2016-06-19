package ca.sfu.cmpt373.alpha.vrcrest;

import ca.sfu.cmpt373.alpha.vrcladder.ApplicationManager;
import ca.sfu.cmpt373.alpha.vrcrest.routes.TeamRouter;
import ca.sfu.cmpt373.alpha.vrcrest.routes.UserRouter;
import spark.Spark;

public class Restapi {

    private ApplicationManager appManager;
    private UserRouter userRouter;
    private TeamRouter teamRouter;

    public Restapi(ApplicationManager appManager, UserRouter userRouter, TeamRouter teamRouter) {
        this.appManager = appManager;
        this.userRouter = userRouter;
        this.teamRouter = teamRouter;
    }

    public void startUp() {
        configure();
        attachRouters();
    }

    public void shutDown() {
        Spark.stop();
        appManager.shutDown();
    }

    private void configure() {
        // TODO - Add specific configurations for Spark.
    }

    // TODO - Add more routers
    private void attachRouters() {
        userRouter.attachRoutes();
        teamRouter.attachRoutes();
    }

}
