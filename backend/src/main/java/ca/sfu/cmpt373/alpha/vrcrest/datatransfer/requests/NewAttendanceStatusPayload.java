package ca.sfu.cmpt373.alpha.vrcrest.datatransfer.requests;

import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.AttendanceStatus;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.AttendanceStatus;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import org.apache.commons.lang3.EnumUtils;

import java.lang.reflect.Type;

import static ca.sfu.cmpt373.alpha.vrcrest.datatransfer.JsonProperties.JSON_PROPERTY_ATTENDANCE_STATUS;

public class NewAttendanceStatusPayload {

    public static class GsonDeserializer extends BaseGsonDeserializer<NewAttendanceStatusPayload> {

        private static final String ERROR_INVALID_ATTENDANCE_STATUS = "Invalid AttendanceStatus.";

        @Override
        public NewAttendanceStatusPayload deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();

            checkForMissingProperties(jsonObject);
            JsonElement jsonAttendanceStatus = jsonObject.get(JSON_PROPERTY_ATTENDANCE_STATUS);
            AttendanceStatus AttendanceStatus = EnumUtils.getEnum(AttendanceStatus.class, jsonAttendanceStatus.getAsString());

            if (AttendanceStatus == null) {
                throw new IllegalArgumentException(ERROR_INVALID_ATTENDANCE_STATUS);
            }

            return new NewAttendanceStatusPayload(AttendanceStatus);
        }

        @Override
        protected void checkForMissingProperties(JsonObject jsonObject) {
            if (!jsonObject.has(JSON_PROPERTY_ATTENDANCE_STATUS)) {
                throwMissingPropertyException(JSON_PROPERTY_ATTENDANCE_STATUS);
            }
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