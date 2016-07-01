package ca.sfu.cmpt373.alpha.vrcrest.routes;

import com.google.gson.Gson;
import org.apache.shiro.authz.AuthorizationException;
import spark.Request;

public abstract class RestRouter {

    public static final String PARAM_ID = ":id";

    public static final String HEADER_AUTHORIZATION = "Authorization";
    public static final String HEADER_AUTHORIZATION_VALUE_BEARER = "Bearer";

    public static final String JSON_RESPONSE_TYPE = "application/json";
    public static final String JSON_PROPERTY_ERROR = "error";

    protected static final String ERROR_COULD_NOT_COMPLETE_REQUEST = "Request could not be completed.";
    protected static final String ERROR_MALFORMED_JSON = "The provided JSON in the request body is malformed.";

    private static final String ERROR_MISSING_AUTHORIZATION_TOKEN = "The request is missing the Authorization header.";
    private static final String ERROR_INVALID_AUTHORIZATION_FORMAT = "The Authorization header should be in the " +
        "format: Bearer <token>.";

    private static final int EXPECTED_AUTHORIZATION_HEADER_WORD_COUNT = 2;
    private static final int AUTHORIZATION_BEARER_INDEX = 0;
    private static final int AUTHORIZATION_TOKEN_INDEX = 1;

    private Gson gson;

    public RestRouter() {
        gson = buildGson();
    }

    protected Gson getGson() {
        return gson;
    }

    protected String extractAuthorizationToken(Request request) {
        String authorizationHeader = request.headers(HEADER_AUTHORIZATION);
        if (authorizationHeader == null) {
            throw new AuthorizationException(ERROR_MISSING_AUTHORIZATION_TOKEN);
        }

        String[] authorizationHeaderWords = authorizationHeader.split(" ");
        if (authorizationHeaderWords.length != EXPECTED_AUTHORIZATION_HEADER_WORD_COUNT) {
            throw new AuthorizationException(ERROR_INVALID_AUTHORIZATION_FORMAT);
        }

        if (!authorizationHeaderWords[AUTHORIZATION_BEARER_INDEX].equals(HEADER_AUTHORIZATION_VALUE_BEARER)) {
            throw new AuthorizationException(ERROR_INVALID_AUTHORIZATION_FORMAT);
        }

        return authorizationHeaderWords[AUTHORIZATION_TOKEN_INDEX];
    }

    public abstract void attachRoutes();
    protected abstract Gson buildGson();

}
