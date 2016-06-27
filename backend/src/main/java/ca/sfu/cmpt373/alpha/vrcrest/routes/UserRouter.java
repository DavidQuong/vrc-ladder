package ca.sfu.cmpt373.alpha.vrcrest.routes;

import ca.sfu.cmpt373.alpha.vrcladder.exceptions.EntityNotFoundException;
import ca.sfu.cmpt373.alpha.vrcladder.users.User;
import ca.sfu.cmpt373.alpha.vrcladder.users.UserManager;
import ca.sfu.cmpt373.alpha.vrcladder.users.personal.UserId;
import ca.sfu.cmpt373.alpha.vrcladder.util.Log;
import ca.sfu.cmpt373.alpha.vrcrest.datatransfer.requests.NewUserPayload;
import ca.sfu.cmpt373.alpha.vrcrest.datatransfer.responses.UserGsonSerializer;
import com.google.gson.*;
import org.eclipse.jetty.http.HttpStatus;
import spark.Request;
import spark.Response;
import spark.Spark;

import java.util.List;

public class UserRouter extends RestRouter {

    public static final String ROUTE_USERS = "/users";
    public static final String ROUTE_USER_ID = "/user/:id";

    public static final String JSON_PROPERTY_USER = "user";
    public static final String JSON_PROPERTY_ALLUSERS = "users";

    private static final String ERROR_PLAYER_ID_NOT_FOUND = "The provided player ID cannot be found.";
    private static final String ERROR_NONEXISTENT_USER = "This user does not exist.";
    private static final String ERROR_EXISTING_USER = "Cannot create user as a user with this ID already exists.";
    private static final String ERROR_GET_USERS_FAILURE = "Unable to pull all users";
    private final UserManager userManager;

    public UserRouter(UserManager userManager) {
        super();
        this.userManager = userManager;
    }

    @Override
    public void attachRoutes() {
        Spark.get(ROUTE_USERS, (request, response) -> handleGetUsers(request, response));
        Spark.post(ROUTE_USERS, (request, response) -> handleCreateUser(request, response));
        Spark.get(ROUTE_USER_ID, (request, response) -> handleGetUserById(request, response));
        Spark.put(ROUTE_USER_ID, (request, response) -> handleUpdateUserById(request, response));
        Spark.delete(ROUTE_USER_ID, (request, response) -> handleDeleteUserById(request, response));
    }

    @Override
    protected Gson buildGson() {
        GsonBuilder gson = new GsonBuilder()
                .registerTypeAdapter(User.class, new UserGsonSerializer())
                .registerTypeAdapter(NewUserPayload.class, new NewUserPayload.UserGsonDeserializer())
                .setPrettyPrinting();
        return gson.create();
    }


    private String handleGetUsers(Request request, Response response) {
        List<User> users = userManager.getAll();
        JsonObject responseBody = new JsonObject();

        try {
            responseBody.add(JSON_PROPERTY_ALLUSERS, getGson().toJsonTree(users));
            response.type(JSON_RESPONSE_TYPE);
            response.status(HttpStatus.OK_200);
            return responseBody.toString();
        } catch (Exception e) {
            e.printStackTrace();
            responseBody.add(JSON_PROPERTY_ALLUSERS, getGson().toJsonTree(ERROR_GET_USERS_FAILURE));
            response.status(HttpStatus.INTERNAL_SERVER_ERROR_500);
            return responseBody.toString();
        }
    }

    private String handleCreateUser(Request request, Response response) {
        User newUser;
        JsonObject responseBody = new JsonObject();

        try {
            NewUserPayload newUserPayload = getGson().fromJson(request.body(), NewUserPayload.class);
            newUser = userManager.create(
                    newUserPayload.getUserId(),
                    newUserPayload.getUserRole(),
                    newUserPayload.getFirstName(),
                    newUserPayload.getMiddleName(),
                    newUserPayload.getLastName(),
                    newUserPayload.getEmailAddress(),
                    newUserPayload.getPhoneNumber());

            JsonElement jsonUser = getGson().toJsonTree(newUser);

            responseBody.add(JSON_PROPERTY_USER, jsonUser);
            response.status(HttpStatus.CREATED_201);
        } catch (JsonSyntaxException ex) {
            responseBody.addProperty(JSON_PROPERTY_ERROR, ERROR_MALFORMED_JSON);
            response.status(HttpStatus.BAD_REQUEST_400);
        } catch (JsonParseException ex) {
            responseBody.addProperty(JSON_PROPERTY_ERROR, ex.getMessage());
            response.status(HttpStatus.BAD_REQUEST_400);
        } catch (EntityNotFoundException ex) {
            responseBody.addProperty(JSON_PROPERTY_ERROR, ERROR_PLAYER_ID_NOT_FOUND);
            response.status(HttpStatus.NOT_FOUND_404);
        } catch (RuntimeException ex) {
            responseBody.addProperty(JSON_PROPERTY_ERROR, ERROR_COULD_NOT_COMPLETE_REQUEST);
            response.status(HttpStatus.BAD_REQUEST_400);
        }

        response.type(JSON_RESPONSE_TYPE);
        return responseBody.toString();
    }

    private String handleGetUserById(Request request, Response response) {
        JsonObject responseBody = new JsonObject();
        String requestedId = request.params(ROUTE_ID);
        UserId userId = new UserId(requestedId);

        try {
            User existingUser = userManager.getById(userId);

            responseBody.add(JSON_PROPERTY_USER, getGson().toJsonTree(existingUser));
            response.status(HttpStatus.OK_200);
        } catch (JsonSyntaxException ex) {
            Log.info("Unable to get user");
            responseBody.addProperty(JSON_PROPERTY_ERROR, ERROR_MALFORMED_JSON);
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
            NewUserPayload userPayload = getGson().fromJson(request.body(), NewUserPayload.class);
            User existingUser = userManager.update(
                    userPayload.getUserId(),
                    userPayload.getUserRole(),
                    userPayload.getFirstName(),
                    userPayload.getMiddleName(),
                    userPayload.getLastName(),
                    userPayload.getEmailAddress(),
                    userPayload.getPhoneNumber());

            responseBody.add(JSON_PROPERTY_USER, getGson().toJsonTree(existingUser));
            response.status(HttpStatus.OK_200);
        } catch (JsonSyntaxException ex) {
            responseBody.addProperty(JSON_PROPERTY_ERROR, ERROR_MALFORMED_JSON);
            response.status(HttpStatus.BAD_REQUEST_400);
        }  catch (JsonParseException | IllegalArgumentException ex) {
            responseBody.addProperty(JSON_PROPERTY_ERROR, ex.getMessage());
            response.status(HttpStatus.BAD_REQUEST_400);
        } catch (EntityNotFoundException ex) {
            responseBody.addProperty(JSON_PROPERTY_ERROR, ERROR_EXISTING_USER);
            response.status(HttpStatus.NOT_FOUND_404);
        } catch (RuntimeException ex) {
            responseBody.addProperty(JSON_PROPERTY_ERROR, ERROR_COULD_NOT_COMPLETE_REQUEST);
            response.status(HttpStatus.BAD_REQUEST_400);
        }

        return responseBody.toString();
    }

    private String handleDeleteUserById(Request request, Response response) {
        JsonObject responseBody = new JsonObject();
        String requestedId = request.params(ROUTE_ID);

        UserId userId = new UserId(requestedId);
        try {
            User deletedUser = userManager.deleteById(userId);
            responseBody.add(JSON_PROPERTY_USER, getGson().toJsonTree(deletedUser));
            response.status(HttpStatus.OK_200);
        }  catch (EntityNotFoundException ex) {
            responseBody.addProperty(JSON_PROPERTY_ERROR, ERROR_NONEXISTENT_USER);
            response.status(HttpStatus.NOT_FOUND_404);
        } catch (RuntimeException ex) {
            responseBody.addProperty(JSON_PROPERTY_ERROR, ERROR_COULD_NOT_COMPLETE_REQUEST);
            response.status(HttpStatus.BAD_REQUEST_400);
        }

        return responseBody.toString();
    }

}
