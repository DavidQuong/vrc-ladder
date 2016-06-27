package ca.sfu.cmpt373.alpha.vrcrest.datatransfer.responses;

import ca.sfu.cmpt373.alpha.vrcladder.users.User;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class UserGsonSerializer implements JsonSerializer<User> {

    public static final String JSON_PROPERTY_USER_ID = "userId";
    public static final String JSON_PROPERTY_USER_ROLE = "userRole";
    public static final String JSON_PROPERTY_FIRST_NAME = "firstName";
    public static final String JSON_PROPERTY_MIDDLE_NAME = "middleName";
    public static final String JSON_PROPERTY_LAST_NAME = "lastName";
    public static final String JSON_PROPERTY_EMAIL_ADDRESS = "emailAddress";
    public static final String JSON_PROPERTY_PHONE_NUMBER = "phoneNumber";

    @Override
    public JsonElement serialize(User user, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonUser = new JsonObject();

        jsonUser.addProperty(JSON_PROPERTY_USER_ID, user.getUserId().getValue());
        jsonUser.addProperty(JSON_PROPERTY_USER_ROLE, user.getUserRole().name());
        jsonUser.addProperty(JSON_PROPERTY_FIRST_NAME, user.getFirstName());
        jsonUser.addProperty(JSON_PROPERTY_MIDDLE_NAME, user.getMiddleName());
        jsonUser.addProperty(JSON_PROPERTY_LAST_NAME, user.getLastName());
        jsonUser.addProperty(JSON_PROPERTY_EMAIL_ADDRESS, user.getEmailAddress().getValue());
        jsonUser.addProperty(JSON_PROPERTY_PHONE_NUMBER, user.getPhoneNumber().getValue());

        return jsonUser;
    }
}
