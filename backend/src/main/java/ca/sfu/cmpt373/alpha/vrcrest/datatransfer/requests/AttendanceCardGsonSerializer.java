package ca.sfu.cmpt373.alpha.vrcrest.datatransfer.requests;

import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.AttendanceCard;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.AttendanceStatus;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.PlayTime;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class AttendanceCardGsonSerializer implements JsonSerializer<AttendanceCard> {

    public static final String JSON_PROPERTY_PLAY_TIME = "playTime";
    public static final String JSON_PROPERTY_ATTENDANCE_STATUS = "attendanceStatus";

    @Override
    public JsonElement serialize(AttendanceCard attendanceCard, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonAttendanceCard = new JsonObject();

        PlayTime preferredPlayTime = attendanceCard.getPreferredPlayTime();
        jsonAttendanceCard.addProperty(JSON_PROPERTY_PLAY_TIME, preferredPlayTime.toString());
        AttendanceStatus attendanceStatus = attendanceCard.getAttendanceStatus();
        jsonAttendanceCard.addProperty(JSON_PROPERTY_ATTENDANCE_STATUS, attendanceStatus.toString());

        return jsonAttendanceCard;
    }
}
