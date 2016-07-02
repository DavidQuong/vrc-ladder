package ca.sfu.cmpt373.alpha.vrcrest.routes;

import ca.sfu.cmpt373.alpha.vrcladder.users.User;
import ca.sfu.cmpt373.alpha.vrcladder.users.UserManager;
import ca.sfu.cmpt373.alpha.vrcladder.users.authentication.SecurityManager;
import ca.sfu.cmpt373.alpha.vrcrest.datatransfer.requests.LoginPayload;
import ca.sfu.cmpt373.alpha.vrcrest.security.RouteSignature;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import org.apache.shiro.authc.AuthenticationException;
import org.eclipse.jetty.http.HttpStatus;
import spark.Request;
import spark.Response;
import spark.Spark;
import spark.route.HttpMethod;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class LoginRouter extends RestRouter {

    public static final String ROUTE_LOGIN = "/login";

    private static final String JSON_PROPERTY_AUTHORIZATION_TOKEN = "authorizationToken";

    private static final List<RouteSignature> PUBLIC_ROUTE_SIGNATURES = createRouteSignature();

    private SecurityManager securityManager;
    private UserManager userManager;

    public LoginRouter(SecurityManager securityManager, UserManager userManager) {
        this.securityManager = securityManager;
        this.userManager = userManager;
    }

    @Override
    public void attachRoutes() {
        Spark.post(ROUTE_LOGIN, ((request, response) -> handleLogin(request, response)));
    }

    @Override
    public List<RouteSignature> getPublicRouteSignatures() {
        return Collections.unmodifiableList(PUBLIC_ROUTE_SIGNATURES);
    }

    private static List<RouteSignature> createRouteSignature() {
        List<RouteSignature> routeSignatures = new ArrayList<>();

        RouteSignature createUserSignature = new RouteSignature(ROUTE_LOGIN, HttpMethod.post);
        routeSignatures.add(createUserSignature);

        return routeSignatures;
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

        try {
            LoginPayload loginPayload = getGson().fromJson(request.body(), LoginPayload.class);

            User user = userManager.getById(loginPayload.getUserId());
            String plaintextPassword = loginPayload.getPassword();

            String authorizationToken = securityManager.login(user, plaintextPassword);
            responseBody.addProperty(JSON_PROPERTY_AUTHORIZATION_TOKEN, authorizationToken);
            response.status(HttpStatus.OK_200);
        } catch (JsonSyntaxException ex) {
            responseBody.addProperty(JSON_PROPERTY_ERROR, ERROR_MALFORMED_JSON);
            response.status(HttpStatus.BAD_REQUEST_400);
        } catch (JsonParseException ex) {
            responseBody.addProperty(JSON_PROPERTY_ERROR, ex.getMessage());
            response.status(HttpStatus.BAD_REQUEST_400);
        } catch (AuthenticationException ex) {
            responseBody.addProperty(JSON_PROPERTY_ERROR, ex.getMessage());
            response.status(HttpStatus.UNAUTHORIZED_401);
        } catch (EntityNotFoundException ex) {
            responseBody.addProperty(JSON_PROPERTY_ERROR, SecurityManager.ERROR_INVALID_CREDENTIALS);
            response.status(HttpStatus.UNAUTHORIZED_401);
        } catch (RuntimeException ex) {
            responseBody.addProperty(JSON_PROPERTY_ERROR, ERROR_COULD_NOT_COMPLETE_REQUEST);
            response.status(HttpStatus.BAD_REQUEST_400);
        }

        response.type(JSON_RESPONSE_TYPE);
        return responseBody.toString();
    }

}
