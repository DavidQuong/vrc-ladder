package ca.sfu.cmpt373.alpha.vrcrest.datatransfer.responses;

import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.Court;
import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.MatchGroup;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.PlayTime;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.Map;

public class CourtSerializer implements JsonSerializer<Court> {

    private static final String JSON_PROPERTY_COURT_ID = "courtId";

    @Override
    public JsonElement serialize(Court src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonCourt = new JsonObject();

        jsonCourt.addProperty(JSON_PROPERTY_COURT_ID, src.getId().getValue());

        Map<PlayTime, MatchGroup> matches = src.getScheduledMatches();
        for (PlayTime playTime : matches.keySet()) {
            MatchGroup matchGroup = matches.get(playTime);
            jsonCourt.addProperty(playTime.toString(), context.serialize(matchGroup).toString());
        }

        return jsonCourt;
    }
}
