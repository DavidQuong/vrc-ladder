package ca.sfu.cmpt373.alpha.vrcrest.routes;

import com.google.gson.Gson;

public abstract class RestRouter {

    protected static final String ERROR_PROPERTY_MISSING_FORMAT = "%s property is missing";

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
