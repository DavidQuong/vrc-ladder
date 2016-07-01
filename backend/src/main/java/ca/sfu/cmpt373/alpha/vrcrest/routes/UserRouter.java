package ca.sfu.cmpt373.alpha.vrcrest.routes;

import ca.sfu.cmpt373.alpha.vrcladder.exceptions.ValidationException;
import ca.sfu.cmpt373.alpha.vrcladder.users.User;
import ca.sfu.cmpt373.alpha.vrcladder.users.UserManager;
import ca.sfu.cmpt373.alpha.vrcladder.users.authentication.Password;
import ca.sfu.cmpt373.alpha.vrcladder.users.authentication.SecurityManager;
import ca.sfu.cmpt373.alpha.vrcladder.users.personal.UserId;
import ca.sfu.cmpt373.alpha.vrcladder.util.Log;
import ca.sfu.cmpt373.alpha.vrcrest.datatransfer.requests.NewUserPayload;
import ca.sfu.cmpt373.alpha.vrcrest.datatransfer.requests.UpdateUserPayload;
import ca.sfu.cmpt373.alpha.vrcrest.datatransfer.responses.UserGsonSerializer;
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

import javax.persistence.EntityNotFoundException;
import java.util.List;

public class UserRouter extends RestRouter {

    public static final String ROUTE_USERS = "/users";
    public static final String ROUTE_USER_ID = "/user/" + PARAM_ID;

    public static final String JSON_PROPERTY_USERS = "users";
    public static final String JSON_PROPERTY_USER = "user";

    private static final String ERROR_NONEXISTENT_USER = "This user does not exist.";
    private static final String ERROR_GET_USERS_FAILURE = "Unable to get all users.";
    private static final String ERROR_EXISTNG_CONSTRAINT = "Email or user already exists.";

    private SecurityManager securityManager;
    private UserManager userManager;

    public UserRouter(SecurityManager securityManager, UserManager userManager) {
        super();
        this.securityManager = securityManager;
        this.userManager = userManager;
    }

    @Override
    public void attachRoutes() {
        Spark.get(ROUTE_USERS, this::handleGetUsers);
        Spark.post(ROUTE_USERS, this::handleCreateUser);
        Spark.get(ROUTE_USER_ID, this::handleGetUserById);
        Spark.put(ROUTE_USER_ID, this::handleUpdateUserById);
        Spark.delete(ROUTE_USER_ID, this::handleDeleteUserById);
    }

    @Override
    protected Gson buildGson() {
        GsonBuilder gson = new GsonBuilder()
            .registerTypeAdapter(User.class, new UserGsonSerializer())
            .registerTypeAdapter(NewUserPayload.class, new NewUserPayload.GsonDeserializer())
            .registerTypeAdapter(UpdateUserPayload.class, new UpdateUserPayload.GsonDeserializer())
            .setPrettyPrinting();
        return gson.create();
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

        response.type(JSON_RESPONSE_TYPE);
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

            JsonElement jsonUser = getGson().toJsonTree(newUser);

            responseBody.add(JSON_PROPERTY_USER, jsonUser);
            response.status(HttpStatus.CREATED_201);
        } catch (JsonSyntaxException ex) {
            responseBody.addProperty(JSON_PROPERTY_ERROR, ERROR_MALFORMED_JSON + ": " + ex.getMessage());
            response.status(HttpStatus.BAD_REQUEST_400);
        } catch (JsonParseException ex) {
            responseBody.addProperty(JSON_PROPERTY_ERROR, ex.getMessage());
            response.status(HttpStatus.BAD_REQUEST_400);
        } catch (ConstraintViolationException ex) {
            responseBody.addProperty(JSON_PROPERTY_ERROR, ERROR_EXISTNG_CONSTRAINT);
            response.status(HttpStatus.CONFLICT_409);
        } catch (RuntimeException ex) {
            responseBody.addProperty(JSON_PROPERTY_ERROR, ERROR_COULD_NOT_COMPLETE_REQUEST + ": " + ex.getMessage());
            response.status(HttpStatus.BAD_REQUEST_400);
        }

        response.type(JSON_RESPONSE_TYPE);
        return responseBody.toString();
    }

    private String handleGetUserById(Request request, Response response) {
        JsonObject responseBody = new JsonObject();

        String requestedId = request.params(PARAM_ID);
        UserId userId = new UserId(requestedId);

        try {
            User existingUser = userManager.getById(userId);

            responseBody.add(JSON_PROPERTY_USER, getGson().toJsonTree(existingUser));
            response.status(HttpStatus.OK_200);
        } catch (EntityNotFoundException ex) {
            responseBody.addProperty(JSON_PROPERTY_ERROR, ERROR_NONEXISTENT_USER);
            response.status(HttpStatus.NOT_FOUND_404);
        } catch (RuntimeException ex) {
            responseBody.addProperty(JSON_PROPERTY_ERROR, ERROR_COULD_NOT_COMPLETE_REQUEST);
            response.status(HttpStatus.BAD_REQUEST_400);
        }

        response.type(JSON_RESPONSE_TYPE);
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
            responseBody.addProperty(JSON_PROPERTY_ERROR, ERROR_MALFORMED_JSON  + ": " + ex.getMessage());
            response.status(HttpStatus.BAD_REQUEST_400);
        } catch (JsonSyntaxException ex) {
            responseBody.addProperty(JSON_PROPERTY_ERROR, ERROR_MALFORMED_JSON + ": " + ex.getMessage());
            response.status(HttpStatus.BAD_REQUEST_400);
        } catch (JsonParseException | IllegalArgumentException ex) {
            responseBody.addProperty(JSON_PROPERTY_ERROR, ex.getMessage());
            response.status(HttpStatus.BAD_REQUEST_400);
        } catch (EntityNotFoundException ex) {
            Log.info(ex.getMessage());
            responseBody.addProperty(JSON_PROPERTY_ERROR, ERROR_NONEXISTENT_USER);
            response.status(HttpStatus.NOT_FOUND_404);
        } catch (ConstraintViolationException ex) {
            responseBody.addProperty(JSON_PROPERTY_ERROR, ERROR_COULD_NOT_COMPLETE_REQUEST
                    + " : " + ex.getConstraintName());
            response.status(HttpStatus.CONFLICT_409);
        } catch (RuntimeException ex) {
            Log.info(ex.getCause().toString() + " : " + ex.getMessage());
            responseBody.addProperty(JSON_PROPERTY_ERROR, ERROR_COULD_NOT_COMPLETE_REQUEST + ": " + ex.getMessage());
            response.status(HttpStatus.BAD_REQUEST_400);
        }

        response.type(JSON_RESPONSE_TYPE);
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
            responseBody.addProperty(JSON_PROPERTY_ERROR, ERROR_MALFORMED_JSON  + ": " + ex.getMessage());
            response.status(HttpStatus.BAD_REQUEST_400);
        }  catch (EntityNotFoundException ex) {
            responseBody.addProperty(JSON_PROPERTY_ERROR, ERROR_NONEXISTENT_USER);
            response.status(HttpStatus.NOT_FOUND_404);
        } catch (RuntimeException ex) {
            responseBody.addProperty(JSON_PROPERTY_ERROR, ERROR_COULD_NOT_COMPLETE_REQUEST  + ": " + ex.getMessage());
            response.status(HttpStatus.BAD_REQUEST_400);
        }

        response.type(JSON_RESPONSE_TYPE);
        return responseBody.toString();
    }

}
