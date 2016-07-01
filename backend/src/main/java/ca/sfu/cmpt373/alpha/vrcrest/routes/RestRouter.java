package ca.sfu.cmpt373.alpha.vrcrest.routes;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.shiro.authz.AuthorizationException;
import org.eclipse.jetty.http.HttpStatus;
import spark.Request;
import spark.Response;
import spark.Spark;

public abstract class RestRouter {


    public static final String PARAM_ID = ":id";

    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String HEADER_AUTHORIZATION_VALUE_BEARER = "Bearer";

    public static final String JSON_RESPONSE_TYPE = "application/json";
    public static final String JSON_PROPERTY_ERROR = "error";

    protected static final String ERROR_INVALID_AUTHORIZATION = "Invalid authorization, could not perform request.";
    protected static final String ERROR_COULD_NOT_COMPLETE_REQUEST = "Request could not be completed.";
    protected static final String ERROR_MALFORMED_JSON = "The provided JSON in the request body is malformed.";

    private static final int EXPECTED_AUTHORIZATION_HEADER_WORD_COUNT = 2;
    private static final int AUTHORIZATION_BEARER_INDEX = 0;

    private Gson gson;

    public RestRouter() {
        gson = buildGson();
    }

    protected Gson getGson() {
        return gson;
    }

    protected void checkForAuthorizationToken(Request request, Response response) {
        String authorizationHeader = request.headers(HEADER_AUTHORIZATION);
        if (authorizationHeader == null) {
            haltUnauthorizedRequest(response);
        }

        try {
            checkAuthorizationHeaderFormat(authorizationHeader);
        } catch (AuthorizationException ex) {
            haltUnauthorizedRequest(response);
        }
    }

    private void checkAuthorizationHeaderFormat(String authorizationHeader) {
        String[] authorizationHeaderWords = authorizationHeader.split(" ");
        if (authorizationHeaderWords.length != EXPECTED_AUTHORIZATION_HEADER_WORD_COUNT) {
            throw new AuthorizationException();
        }

        if (!authorizationHeaderWords[AUTHORIZATION_BEARER_INDEX].equals(HEADER_AUTHORIZATION_VALUE_BEARER)) {
            throw new AuthorizationException();
        }
    }

    private void haltUnauthorizedRequest(Response response) {
        JsonObject responseBody = new JsonObject();
        responseBody.addProperty(JSON_PROPERTY_ERROR, ERROR_INVALID_AUTHORIZATION);

        response.type(JSON_RESPONSE_TYPE);
        Spark.halt(HttpStatus.UNAUTHORIZED_401, responseBody.toString());
    }

    public abstract void attachRoutes();
    protected abstract Gson buildGson();

}
