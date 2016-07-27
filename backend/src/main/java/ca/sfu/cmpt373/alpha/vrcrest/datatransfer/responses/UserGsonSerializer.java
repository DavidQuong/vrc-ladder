package ca.sfu.cmpt373.alpha.vrcrest.datatransfer.responses;

import ca.sfu.cmpt373.alpha.vrcladder.users.User;
import ca.sfu.cmpt373.alpha.vrcrest.datatransfer.JsonProperties;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class UserGsonSerializer implements JsonSerializer<User> {

    @Override
    public JsonElement serialize(User user, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonUser = new JsonObject();

        jsonUser.addProperty(JsonProperties.JSON_PROPERTY_USER_ID, user.getUserId().getValue());
        jsonUser.addProperty(JsonProperties.JSON_PROPERTY_USER_ROLE, user.getUserRole().name());
        jsonUser.addProperty(JsonProperties.JSON_PROPERTY_NAME, user.getDisplayName());
        jsonUser.addProperty(JsonProperties.JSON_PROPERTY_EMAIL_ADDRESS, user.getEmailAddress().getValue());
        jsonUser.addProperty(JsonProperties.JSON_PROPERTY_PHONE_NUMBER, user.getPhoneNumber().getValue());

        return jsonUser;
    }
}
