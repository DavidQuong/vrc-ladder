package ca.sfu.cmpt373.alpha.vrcrest.routes;

import ca.sfu.cmpt373.alpha.vrcrest.security.RouteSignature;
import com.google.gson.Gson;

import java.util.List;

public abstract class RestRouter {

    public static final String PARAM_ID = ":id";

    public static final String JSON_PROPERTY_ERROR = "error";

    protected static final String ERROR_COULD_NOT_COMPLETE_REQUEST = "Request could not be completed.";
    protected static final String ERROR_MALFORMED_JSON = "The provided JSON in the request body is malformed.";
    protected static final String ERROR_INVALID_RESOURCE_ID = "The provided resource identifier is invalid.";


    private Gson gson;

    public RestRouter() {
        gson = buildGson();
    }

    protected Gson getGson() {
        return gson;
    }

    public abstract void attachRoutes();
    public abstract List<RouteSignature> getPublicRouteSignatures();
    protected abstract Gson buildGson();

}
