package ca.sfu.cmpt373.alpha.vrcrest.datatransfer.requests;

import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.PlayTime;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import org.apache.commons.lang3.EnumUtils;

import java.lang.reflect.Type;

public class NewPlayTimePayload {

    public static class GsonDeserializer extends BaseGsonDeserializer<NewPlayTimePayload> {

        public static final String JSON_PROPERTY_PLAY_TIME = "playTime";

        private static final String ERROR_INVALID_PLAY_TIME = "Invalid playTime.";

        @Override
        public NewPlayTimePayload deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            checkForMissingProperties(jsonObject);

            JsonElement jsonPlayTime = jsonObject.get(JSON_PROPERTY_PLAY_TIME);
            PlayTime playTime = EnumUtils.getEnum(PlayTime.class, jsonPlayTime.getAsString());

            if (playTime == null) {
                throw new IllegalArgumentException(ERROR_INVALID_PLAY_TIME);
            }

            return new NewPlayTimePayload(playTime);
        }

        @Override
        protected void checkForMissingProperties(JsonObject jsonObject) {
            if (!jsonObject.has(JSON_PROPERTY_PLAY_TIME)) {
                throwMissingPropertyException(JSON_PROPERTY_PLAY_TIME);
            }
        }

    }

    private PlayTime playTime;

    public NewPlayTimePayload(PlayTime playTime) {
        this.playTime = playTime;
    }

    public PlayTime getPlayTime() {
        return playTime;
    }

}
