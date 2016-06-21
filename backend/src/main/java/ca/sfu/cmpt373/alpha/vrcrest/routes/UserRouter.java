package ca.sfu.cmpt373.alpha.vrcrest.routes;

import ca.sfu.cmpt373.alpha.vrcladder.users.UserManager;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Spark;

// TODO - Implement private route methods.
//      - Update buildGson method if necessary.
public class UserRouter extends RestRouter {

    public static final String ROUTE_USERS = "/users";
    public static final String ROUTE_USER_ID = "/user/:id";

    private UserManager userManager;

    public UserRouter(UserManager userManager) {
        super();
        this.userManager = userManager;
    }

    @Override
    public void attachRoutes() {
        Spark.get(ROUTE_USERS, (request, response) -> handleGetUsers(request, response));
        Spark.post(ROUTE_USERS, (request, response) -> handleCreateUser(request, response));
        Spark.get(ROUTE_USER_ID, (request, response) -> handleGetUserById(request, response));
        Spark.put(ROUTE_USER_ID, (request, response) -> handleUpdateUserById(request, response));
        Spark.put(ROUTE_USER_ID, (request, response) -> handleDeleteUserById(request, response));
    }

    @Override
    protected Gson buildGson() {
        return new Gson();
    }

    private String handleGetUsers(Request request, Response response) {
        return null;
    }

    private String handleCreateUser(Request request, Response response) {
        return null;
    }

    private String handleGetUserById(Request request, Response response) {
        return null;
    }

    private String handleUpdateUserById(Request request, Response response) {
        return null;
    }

    private String handleDeleteUserById(Request request, Response response) {
        return null;
    }

}
