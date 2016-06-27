package ca.sfu.cmpt373.alpha.vrcrest.datatransfer.requests;

import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.AttendanceStatus;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.AttendanceStatus;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import org.apache.commons.lang3.EnumUtils;

import java.lang.reflect.Type;

public class NewAttendanceStatusPayload {

    public static class GsonDeserializer extends BaseGsonDeserializer<NewAttendanceStatusPayload> {

        public static final String JSON_PROPERTY_PLAY_TIME = "AttendanceStatus";

        private static final String ERROR_INVALID_PLAY_TIME = "Invalid AttendanceStatus.";

        @Override
        public NewAttendanceStatusPayload deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            JsonObject jsonObject = (JsonObject) json;

            if (!jsonObject.has(JSON_PROPERTY_PLAY_TIME)) {
                throwMissingPropertyException(JSON_PROPERTY_PLAY_TIME);
            }
            JsonElement jsonAttendanceStatus = jsonObject.get(JSON_PROPERTY_PLAY_TIME);
            AttendanceStatus AttendanceStatus = EnumUtils.getEnum(AttendanceStatus.class, jsonAttendanceStatus.getAsString());

            if (AttendanceStatus == null) {
                throw new IllegalArgumentException(ERROR_INVALID_PLAY_TIME);
            }

            return new NewAttendanceStatusPayload(AttendanceStatus);
        }

    }

    private AttendanceStatus AttendanceStatus;

    public NewAttendanceStatusPayload(AttendanceStatus AttendanceStatus) {
        this.AttendanceStatus = AttendanceStatus;
    }

    public AttendanceStatus getAttendanceStatus() {
        return AttendanceStatus;
    }

}