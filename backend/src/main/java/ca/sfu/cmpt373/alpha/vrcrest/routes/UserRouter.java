package ca.sfu.cmpt373.alpha.vrcrest.routes;

import ca.sfu.cmpt373.alpha.vrcladder.ApplicationManager;
import ca.sfu.cmpt373.alpha.vrcladder.exceptions.ValidationException;
import ca.sfu.cmpt373.alpha.vrcladder.notifications.NotificationManager;
import ca.sfu.cmpt373.alpha.vrcladder.notifications.logic.NotificationType;
import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
import ca.sfu.cmpt373.alpha.vrcladder.teams.TeamManager;
import ca.sfu.cmpt373.alpha.vrcladder.users.User;
import ca.sfu.cmpt373.alpha.vrcladder.users.UserManager;
import ca.sfu.cmpt373.alpha.vrcladder.users.authentication.Password;
import ca.sfu.cmpt373.alpha.vrcladder.users.authentication.SecurityManager;
import ca.sfu.cmpt373.alpha.vrcladder.users.personal.UserId;
import ca.sfu.cmpt373.alpha.vrcrest.datatransfer.requests.NewUserPayload;
import ca.sfu.cmpt373.alpha.vrcrest.datatransfer.requests.UpdateUserPayload;
import ca.sfu.cmpt373.alpha.vrcrest.datatransfer.responses.PlayerGsonSerializer;
import ca.sfu.cmpt373.alpha.vrcrest.datatransfer.responses.TeamGsonSerializer;
import ca.sfu.cmpt373.alpha.vrcrest.datatransfer.responses.UserGsonSerializer;
import ca.sfu.cmpt373.alpha.vrcrest.security.RouteSignature;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import org.eclipse.jetty.http.HttpStatus;
import org.hibernate.exception.ConstraintViolationException;
import spark.Request;
import spark.Response;
import spark.Spark;
import spark.route.HttpMethod;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static ca.sfu.cmpt373.alpha.vrcrest.routes.TeamRouter.JSON_PROPERTY_TEAMS;

public class UserRouter extends RestRouter {

    public static final String ROUTE_USERS = "/users";
    public static final String ROUTE_PLAYERS = "/players";

    public static final String ROUTE_USERS_SELF = ROUTE_USERS + "/self";
    public static final String ROUTE_USER_ID = "/user/" + PARAM_ID;
    private static final String ROUTE_USER_ID_TEAMS = ROUTE_USER_ID + "/teams";

    public static final String JSON_PROPERTY_USERS = "users";
    public static final String JSON_PROPERTY_USER = "user";
    public static final String JSON_PROPERTY_PLAYERS = "players";


    private static final String ERROR_NONEXISTENT_USER = "This user does not exist.";
    private static final String ERROR_EXISTING_USER_DETAILS = "A user with this ID or email address already exists.";
    private static final String ERROR_UNAUTHORIZED_OTHER_USERS = "This user is not authorized to access or modify " +
        "other user's data.";

    private static final List<RouteSignature> PUBLIC_ROUTE_SIGNATURES = createPublicRouteSignatures();

    private final NotificationManager notify;
    private SecurityManager securityManager;
    private UserManager userManager;
    private TeamManager teamManager;
    private Gson playerGson;

    public UserRouter(ApplicationManager applicationManager) {
        super(applicationManager);
        securityManager = applicationManager.getSecurityManager();
        userManager = applicationManager.getUserManager();
        teamManager = applicationManager.getTeamManager();
        playerGson = buildGsonForPlayer();
        notify = new NotificationManager();
    }

    @Override
    public void attachRoutes() {
        Spark.get(ROUTE_USERS, this::handleGetUsers);
        Spark.post(ROUTE_USERS, this::handleCreateUser);
        Spark.get(ROUTE_PLAYERS, this::handleGetPlayers);
        Spark.get(ROUTE_USERS_SELF, this::handleGetActiveUser);
        Spark.get(ROUTE_USER_ID, this::handleGetUserById);
        Spark.put(ROUTE_USER_ID, this::handleUpdateUserById);
        Spark.delete(ROUTE_USER_ID, this::handleDeleteUserById);
        Spark.get(ROUTE_USER_ID_TEAMS, this::handleGetAllTeamsForUser);
    }

    @Override
    public List<RouteSignature> getPublicRouteSignatures() {
        return PUBLIC_ROUTE_SIGNATURES;
    }

    private static List<RouteSignature> createPublicRouteSignatures() {
        List<RouteSignature> routeSignatures = new ArrayList<>();

        RouteSignature createUserSignature = new RouteSignature(ROUTE_USERS, HttpMethod.post);
        routeSignatures.add(createUserSignature);

        return Collections.unmodifiableList(routeSignatures);
    }

    @Override
    protected Gson buildGson() {
        return new GsonBuilder()
            .registerTypeAdapter(Team.class, new TeamGsonSerializer())
            .registerTypeAdapter(User.class, new UserGsonSerializer())
            .registerTypeAdapter(NewUserPayload.class, new NewUserPayload.GsonDeserializer())
            .registerTypeAdapter(UpdateUserPayload.class, new UpdateUserPayload.GsonDeserializer())
            .setPrettyPrinting()
            .create();
    }

    private Gson buildGsonForPlayer() {
        return new GsonBuilder()
            .registerTypeAdapter(User.class, new PlayerGsonSerializer())
            .setPrettyPrinting()
            .create();
    }

    private String handleGetUsers(Request request, Response response) {
        JsonObject responseBody = new JsonObject();
        try {
            List<User> users = userManager.getAll();
            responseBody.add(JSON_PROPERTY_USERS, getGson().toJsonTree(users));
            response.status(HttpStatus.OK_200);
        } catch (RuntimeException ex) {
            responseBody.addProperty(JSON_PROPERTY_ERROR, ERROR_COULD_NOT_COMPLETE_REQUEST);
            response.status(HttpStatus.BAD_REQUEST_400);
        }

        return responseBody.toString();
    }

    private String handleGetPlayers(Request request, Response response) {
        JsonObject responseBody = new JsonObject();
        try {
            List<User> players = userManager.getAllPlayers();
            responseBody.add(JSON_PROPERTY_PLAYERS, playerGson.toJsonTree(players));
            response.status(HttpStatus.OK_200);
        } catch (RuntimeException ex) {
            responseBody.addProperty(JSON_PROPERTY_ERROR, ERROR_COULD_NOT_COMPLETE_REQUEST);
            response.status(HttpStatus.BAD_REQUEST_400);
        }

        return responseBody.toString();
    }

    private String handleCreateUser(Request request, Response response) {
        JsonObject responseBody = new JsonObject();

        try {
            NewUserPayload newUserPayload = getGson().fromJson(request.body(), NewUserPayload.class);
            Password hashedPassword = securityManager.hashPassword(newUserPayload.getPassword());

            User newUser = userManager.create(
                newUserPayload.getUserId(),
                newUserPayload.getUserRole(),
                newUserPayload.getFirstName(),
                newUserPayload.getMiddleName(),
                newUserPayload.getLastName(),
                newUserPayload.getEmailAddress(),
                newUserPayload.getPhoneNumber(),
                hashedPassword);

            notify.notifyUser(newUser, NotificationType.ACCOUNT_ACTIVATED);
            JsonElement jsonUser = getGson().toJsonTree(newUser);
            responseBody.add(JSON_PROPERTY_USER, jsonUser);
            response.status(HttpStatus.CREATED_201);
        } catch (JsonSyntaxException ex) {
            responseBody.addProperty(JSON_PROPERTY_ERROR, ERROR_MALFORMED_JSON);
            response.status(HttpStatus.BAD_REQUEST_400);
        } catch (JsonParseException ex) {
            responseBody.addProperty(JSON_PROPERTY_ERROR, ex.getMessage());
            response.status(HttpStatus.BAD_REQUEST_400);
        } catch (ConstraintViolationException ex) {
            responseBody.addProperty(JSON_PROPERTY_ERROR, ERROR_EXISTING_USER_DETAILS);
            response.status(HttpStatus.CONFLICT_409);
        } catch (RuntimeException ex) {
            responseBody.addProperty(JSON_PROPERTY_ERROR, ERROR_COULD_NOT_COMPLETE_REQUEST + ": " + ex.getMessage());
            response.status(HttpStatus.BAD_REQUEST_400);
        }

        return responseBody.toString();
    }

    private String handleGetActiveUser(Request request, Response response) {
        JsonObject responseBody = new JsonObject();

        try {
            User activeUser = extractUserFromRequest(request);
            responseBody.add(JSON_PROPERTY_USER, getGson().toJsonTree(activeUser));
            response.status(HttpStatus.OK_200);
        } catch (ValidationException ex) {
            responseBody.addProperty(JSON_PROPERTY_ERROR, ERROR_INVALID_RESOURCE_ID);
            response.status(HttpStatus.BAD_REQUEST_400);
        } catch (EntityNotFoundException ex) {
            responseBody.addProperty(JSON_PROPERTY_ERROR, ERROR_NONEXISTENT_USER);
            response.status(HttpStatus.NOT_FOUND_404);
        } catch (RuntimeException ex) {
            responseBody.addProperty(JSON_PROPERTY_ERROR, ERROR_COULD_NOT_COMPLETE_REQUEST);
            response.status(HttpStatus.BAD_REQUEST_400);
        }

        return responseBody.toString();
    }

    private String handleGetUserById(Request request, Response response) {
        JsonObject responseBody = new JsonObject();

        try {
            String requestedId = request.params(PARAM_ID);
            UserId userId = new UserId(requestedId);

            User existingUser = userManager.getById(userId);
            responseBody.add(JSON_PROPERTY_USER, getGson().toJsonTree(existingUser));
            response.status(HttpStatus.OK_200);
        } catch (ValidationException ex) {
            responseBody.addProperty(JSON_PROPERTY_ERROR, ERROR_INVALID_RESOURCE_ID);
            response.status(HttpStatus.BAD_REQUEST_400);
        } catch (EntityNotFoundException ex) {
            responseBody.addProperty(JSON_PROPERTY_ERROR, ERROR_NONEXISTENT_USER);
            response.status(HttpStatus.NOT_FOUND_404);
        } catch (RuntimeException ex) {
            responseBody.addProperty(JSON_PROPERTY_ERROR, ERROR_COULD_NOT_COMPLETE_REQUEST);
            response.status(HttpStatus.BAD_REQUEST_400);
        }

        return responseBody.toString();
    }

    private String handleUpdateUserById(Request request, Response response) {
        JsonObject responseBody = new JsonObject();

        try {
            String requestedId = request.params(PARAM_ID);
            UserId userId = new UserId(requestedId);

            UpdateUserPayload updateUserPayload = getGson().fromJson(request.body(), UpdateUserPayload.class);
            User existingUser = userManager.update(
                userId,
                updateUserPayload.getFirstName(),
                updateUserPayload.getMiddleName(),
                updateUserPayload.getLastName(),
                updateUserPayload.getEmailAddress(),
                updateUserPayload.getPhoneNumber());

            responseBody.add(JSON_PROPERTY_USER, getGson().toJsonTree(existingUser));
            response.status(HttpStatus.OK_200);
        } catch (ValidationException ex) {
            responseBody.addProperty(JSON_PROPERTY_ERROR, ex.getMessage());
            response.status(HttpStatus.BAD_REQUEST_400);
        } catch (JsonSyntaxException ex) {
            responseBody.addProperty(JSON_PROPERTY_ERROR, ERROR_MALFORMED_JSON);
            response.status(HttpStatus.BAD_REQUEST_400);
        } catch (JsonParseException | IllegalArgumentException ex) {
            responseBody.addProperty(JSON_PROPERTY_ERROR, ex.getMessage());
            response.status(HttpStatus.BAD_REQUEST_400);
        } catch (EntityNotFoundException ex) {
            responseBody.addProperty(JSON_PROPERTY_ERROR, ERROR_NONEXISTENT_USER);
            response.status(HttpStatus.NOT_FOUND_404);
        } catch (ConstraintViolationException ex) {
            responseBody.addProperty(JSON_PROPERTY_ERROR, ERROR_COULD_NOT_COMPLETE_REQUEST + ": " +
                ex.getConstraintName());
            response.status(HttpStatus.CONFLICT_409);
        } catch (RuntimeException ex) {
            responseBody.addProperty(JSON_PROPERTY_ERROR, ERROR_COULD_NOT_COMPLETE_REQUEST + ": " + ex.getMessage());
            response.status(HttpStatus.BAD_REQUEST_400);
        }

        return responseBody.toString();
    }

    private String handleDeleteUserById(Request request, Response response) {
        JsonObject responseBody = new JsonObject();

        try {
            String requestedId = request.params(PARAM_ID);
            UserId userId = new UserId(requestedId);

            User deletedUser = userManager.deleteById(userId);
            responseBody.add(JSON_PROPERTY_USER, getGson().toJsonTree(deletedUser));
            response.status(HttpStatus.OK_200);
        } catch (ValidationException ex) {
            responseBody.addProperty(JSON_PROPERTY_ERROR, ERROR_MALFORMED_JSON);
            response.status(HttpStatus.BAD_REQUEST_400);
        } catch (EntityNotFoundException ex) {
            responseBody.addProperty(JSON_PROPERTY_ERROR, ERROR_NONEXISTENT_USER);
            response.status(HttpStatus.NOT_FOUND_404);
        } catch (RuntimeException ex) {
            responseBody.addProperty(JSON_PROPERTY_ERROR, ERROR_COULD_NOT_COMPLETE_REQUEST + ": " + ex.getMessage());
            response.status(HttpStatus.BAD_REQUEST_400);
        }

        return responseBody.toString();
    }

    private String handleGetAllTeamsForUser(Request request, Response response) {
        JsonObject responseBody = new JsonObject();

        String userIdParam = request.params(PARAM_ID);
        User user = userManager.getById(new UserId(userIdParam));

        List<Team> teamsForUser = teamManager.getTeamsForUser(user);
        responseBody.add(JSON_PROPERTY_TEAMS, getGson().toJsonTree(teamsForUser));
        return responseBody.toString();
    }

}
