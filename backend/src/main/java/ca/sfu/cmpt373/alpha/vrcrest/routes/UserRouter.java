package ca.sfu.cmpt373.alpha.vrcrest.routes;

import ca.sfu.cmpt373.alpha.vrcladder.ApplicationManager;
import ca.sfu.cmpt373.alpha.vrcladder.exceptions.EntityNotFoundException;
import ca.sfu.cmpt373.alpha.vrcladder.users.User;
import ca.sfu.cmpt373.alpha.vrcladder.users.UserManager;
import ca.sfu.cmpt373.alpha.vrcladder.util.Log;
import ca.sfu.cmpt373.alpha.vrcrest.datatransfer.requests.NewUserPayload;
import ca.sfu.cmpt373.alpha.vrcrest.datatransfer.responses.UserGsonSerializer;
import com.google.gson.*;
import org.eclipse.jetty.http.HttpStatus;
import org.hibernate.exception.ConstraintViolationException;
import spark.Request;
import spark.Response;
import spark.Spark;

import java.util.List;

// TODO - Implement private route methods.
//      - Update buildGson method if necessary.
public class UserRouter extends RestRouter {

    public static final String ROUTE_USERS = "/users";
    public static final String ROUTE_USER_ID = "/user/:id";

    public static final String JSON_PROPERTY_USER = "user";
    public static final String JSON_PROPERTY_ALLUSERS = "users";
    public static final String JSON_PROPERTY_USERID = "userId";
    public static final String JSON_PROPERTY_USERROLE = "userRole";
    public static final String JSON_PROPERTY_FIRSTNAME = "firstName";
    public static final String JSON_PROPERTY_MIDDLENAME = "firstName";
    public static final String JSON_PROPERTY_LASTNAME = "lastName";
    public static final String JSON_PROPERTY_EMAILADDRESS = "emailAddress";
    public static final String JSON_PROPERTY_PHONENUMBER= "phoneNumber";

    private static final String ERROR_PLAYER_ID_NOT_FOUND = "The provided player ID cannot be found.";
    private static final String ERROR_IDENTICAL_PLAYER_ID = "Cannot create user as a user with this ID already exists.";
    private static final String ERROR_NONEXISTENT_USER = "This user does not exist.";
    private static final String ERROR_EXISTING_USER = "The provided pair of player's already form a team.";



    private UserManager userManager;
    private static ApplicationManager application = new ApplicationManager();


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
        Spark.put(ROUTE_USER_ID, (request, response) -> handleDeleteUserById(request, response));
    }

    @Override
    protected Gson buildGson() {
        GsonBuilder gson = new GsonBuilder()
                .registerTypeAdapter(User.class, new UserGsonSerializer())
                .registerTypeAdapter(NewUserPayload.class, new NewUserPayload.UserGsonDeserializer());
        return gson.create();
    }


    private String handleGetUsers(Request request, Response response) {
        Gson userGson = new Gson();
        List<User> users = userManager.getAll();
        JsonObject responseBody = new JsonObject();

        try{
            responseBody.add(JSON_PROPERTY_ALLUSERS, getGson().toJsonTree(users));
            response.type(JSON_RESPONSE_TYPE);
            response.status(HttpStatus.OK_200);
            return responseBody.toString();
        }catch (Exception e){
            e.printStackTrace();
            response.status(HttpStatus.INTERNAL_SERVER_ERROR_500);
            return userGson.toJson("Unable to pull users:\n" + e.getMessage());
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
        return null;
    }

    private String handleUpdateUserById(Request request, Response response) {
        return null;
    }

    private String handleDeleteUserById(Request request, Response response) {
        return null;
    }

}
