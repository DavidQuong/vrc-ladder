package ca.sfu.cmpt373.alpha.vrcrest.routes;

import ca.sfu.cmpt373.alpha.vrcladder.users.UserManager;
import ca.sfu.cmpt373.alpha.vrcrest.interfaces.RestRouter;
import spark.Route;
import spark.Spark;

// TODO - Implement private route methods.
public class UserRouter implements RestRouter {

    public static final String ROUTE_USERS = "/users";
    public static final String ROUTE_USER_ID = "/user/:id";

    private UserManager userManager;

    public UserRouter(UserManager userManager) {
        this.userManager = userManager;
    }

    @Override
    public void attachRoutes() {
        Spark.get(ROUTE_USERS, getUsersRoute());
        Spark.post(ROUTE_USERS, postUsersRoute());
        Spark.get(ROUTE_USER_ID, getUserByIdRoute());
        Spark.put(ROUTE_USER_ID, putUserByIdRoute());
        Spark.put(ROUTE_USER_ID, deleteUserByIdRoute());
    }

    private Route getUsersRoute() {
        return null;
    }

    private Route postUsersRoute() {
        return null;
    }

    private Route getUserByIdRoute() {
        return null;
    }

    private Route putUserByIdRoute() {
        return null;
    }

    private Route deleteUserByIdRoute() {
        return null;
    }

}
