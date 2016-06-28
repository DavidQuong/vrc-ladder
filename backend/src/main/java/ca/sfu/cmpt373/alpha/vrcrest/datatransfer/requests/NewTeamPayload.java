package ca.sfu.cmpt373.alpha.vrcrest.datatransfer.requests;

import ca.sfu.cmpt373.alpha.vrcladder.users.personal.UserId;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class NewTeamPayload {

    public static class GsonDeserializer extends BaseGsonDeserializer<NewTeamPayload> {

        public static final String JSON_PROPERTY_FIRST_PLAYER_ID = "firstPlayerId";
        public static final String JSON_PROPERTY_SECOND_PLAYER_ID = "secondPlayerId";

        @Override
        public NewTeamPayload deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            checkForMissingProperties(jsonObject);

            JsonElement jsonFirstPlayerId = jsonObject.get(JSON_PROPERTY_FIRST_PLAYER_ID);
            UserId firstPlayerId = new UserId(jsonFirstPlayerId.getAsString());

            JsonElement jsonSecondPlayerId = jsonObject.get(JSON_PROPERTY_SECOND_PLAYER_ID);
            UserId secondPlayerId = new UserId(jsonSecondPlayerId.getAsString());

            return new NewTeamPayload(firstPlayerId, secondPlayerId);
        }

        @Override
        protected void checkForMissingProperties(JsonObject jsonObject) {
            if (!jsonObject.has(JSON_PROPERTY_FIRST_PLAYER_ID)) {
                throwMissingPropertyException(JSON_PROPERTY_FIRST_PLAYER_ID);
            }

            if (!jsonObject.has(JSON_PROPERTY_SECOND_PLAYER_ID)) {
                throwMissingPropertyException(JSON_PROPERTY_SECOND_PLAYER_ID);
            }
        }

    }

    private UserId firstPlayerId;
    private UserId secondPlayerId;

    public NewTeamPayload(UserId firstPlayerId, UserId secondPlayerId) {
        this.firstPlayerId = firstPlayerId;
        this.secondPlayerId = secondPlayerId;
    }

    public UserId getFirstPlayerId() {
        return firstPlayerId;
    }

    public UserId getSecondPlayerId() {
        return secondPlayerId;
    }

}
