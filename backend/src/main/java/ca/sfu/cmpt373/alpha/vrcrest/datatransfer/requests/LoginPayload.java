package ca.sfu.cmpt373.alpha.vrcrest.datatransfer.requests;

import ca.sfu.cmpt373.alpha.vrcladder.users.personal.UserId;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class LoginPayload {

    public static class GsonDeserializer extends BaseGsonDeserializer<LoginPayload> {

        public static final String JSON_PROPERTY_USER_ID = "userId";
        public static final String JSON_PROPERTY_PASSWORD = "password";

        @Override
        public LoginPayload deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();

            if (!jsonObject.has(JSON_PROPERTY_USER_ID)) {
                throwMissingPropertyException(JSON_PROPERTY_USER_ID);
            }
            JsonElement jsonUserId = jsonObject.get(JSON_PROPERTY_USER_ID);
            UserId userId = new UserId(jsonUserId.getAsString());

            if (!jsonObject.has(JSON_PROPERTY_PASSWORD)) {
                throwMissingPropertyException(JSON_PROPERTY_PASSWORD);
            }
            JsonElement jsonPassword = jsonObject.get(JSON_PROPERTY_PASSWORD);
            String password = jsonPassword.getAsString();

            return new LoginPayload(userId, password);
        }

    }

    private UserId userId;
    private String password;

    public LoginPayload(UserId userId, String password) {
        this.userId = userId;
        this.password = password;
    }

    public UserId getUserId() {
        return userId;
    }

    public String getPassword() {
        return password;
    }

}
