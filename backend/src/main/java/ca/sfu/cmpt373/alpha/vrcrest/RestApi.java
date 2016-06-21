package ca.sfu.cmpt373.alpha.vrcrest;

import ca.sfu.cmpt373.alpha.vrcladder.ApplicationManager;
import ca.sfu.cmpt373.alpha.vrcrest.interfaces.RestRouter;
import spark.Spark;

import java.util.List;

public class RestApi {

    private ApplicationManager appManager;
    private List<RestRouter> routers;

    public RestApi(ApplicationManager appManager, List<RestRouter> routers) {
        this.appManager = appManager;
        this.routers = routers;
        initialize();
    }

    public void shutDown() {
        Spark.stop();
        appManager.shutDown();
    }

    private void initialize() {
        configure();
        attachRouters();
    }

    private void configure() {
        // TODO - Add specific configurations for Spark.
    }

    private void attachRouters() {
        routers.forEach(RestRouter::attachRoutes);
    }

}
