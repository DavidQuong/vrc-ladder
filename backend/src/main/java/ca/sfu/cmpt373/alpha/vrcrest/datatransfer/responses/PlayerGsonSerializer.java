package ca.sfu.cmpt373.alpha.vrcrest.datatransfer.responses;

import ca.sfu.cmpt373.alpha.vrcladder.users.User;
import ca.sfu.cmpt373.alpha.vrcrest.datatransfer.JsonProperties;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class PlayerGsonSerializer implements JsonSerializer<User> {

    @Override
    public JsonElement serialize(User user, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonUser = new JsonObject();

        jsonUser.addProperty(JsonProperties.JSON_PROPERTY_USER_ID, user.getUserId().getValue());
        jsonUser.addProperty(JsonProperties.JSON_PROPERTY_NAME, user.getDisplayName());

        return jsonUser;
    }
}
