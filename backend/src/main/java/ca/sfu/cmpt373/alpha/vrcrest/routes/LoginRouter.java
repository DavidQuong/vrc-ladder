package ca.sfu.cmpt373.alpha.vrcrest.routes;

import ca.sfu.cmpt373.alpha.vrcladder.users.authentication.PasswordManager;
import ca.sfu.cmpt373.alpha.vrcrest.datatransfer.requests.LoginPayload;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
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
            .registerTypeAdapter(LoginPayload.class, new LoginPayload.GsonDeserializer())
            .setPrettyPrinting()
            .create();
    }

    private String handleLogin(Request request, Response response) {
        JsonObject responseBody = new JsonObject();

        response.type(JSON_RESPONSE_TYPE);
        return responseBody.toString();
    }

}
