package ca.sfu.cmpt373.alpha.vrcrest.routes;


private Gson gson;

public RestRouter() {
        gson = buildGson();
        }

import com.google.gson.Gson;

public abstract class RestRouter {
    protected Gson getGson() {
        return gson;
    }

    public abstract void attachRoutes();
    protected abstract Gson buildGson();

}
