package ca.sfu.cmpt373.alpha.vrcrest.routes;


    public static final String ROUTE_ID = ":id";

    public static final String JSON_RESPONSE_TYPE = "application/json";
    public static final String JSON_PROPERTY_ERROR = "error";
    public static final String ERROR_COULD_NOT_COMPLETE_REQUEST = "Request could not be completed.";
    public static final String ERROR_MALFORMED_JSON = "The provided JSON in the request body is malformed.";


    private Gson gson;
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
