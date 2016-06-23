package ca.sfu.cmpt373.alpha.vrcrest.routes;

import com.google.gson.Gson;

public abstract class RestRouter {

    public static final String RESPONSE_TYPE = "application/json";
    public static final String JSON_PROPERTY_ERROR = "error";

    private Gson gson;

    public RestRouter() {
        gson = buildGson();
    }

    protected Gson getGson() {
        return gson;
    }

    public abstract void attachRoutes();
    protected abstract Gson buildGson();

}
