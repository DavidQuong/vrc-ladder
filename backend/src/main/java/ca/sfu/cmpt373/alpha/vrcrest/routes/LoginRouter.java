package ca.sfu.cmpt373.alpha.vrcrest.routes;

import ca.sfu.cmpt373.alpha.vrcladder.users.authentication.PasswordManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import spark.Request;
import spark.Response;
import spark.Spark;

public class LoginRouter extends RestRouter {

    public static final String ROUTE_LOGIN = "/login";

    private PasswordManager passwordManager;

    public LoginRouter(PasswordManager passwordManager) {
        this.passwordManager = passwordManager;
    }

    @Override
    public void attachRoutes() {
        Spark.post(ROUTE_LOGIN, ((request, response) -> handleLogin(request, response)));
    }

    @Override
    protected Gson buildGson() {
        return new GsonBuilder()
            .setPrettyPrinting()
            .create();
    }

    private String handleLogin(Request request, Response response) {
        return null;
    }

}
