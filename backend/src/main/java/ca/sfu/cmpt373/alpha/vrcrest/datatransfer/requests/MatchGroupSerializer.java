package ca.sfu.cmpt373.alpha.vrcrest.datatransfer.requests;

import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.MatchGroup;
import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class MatchGroupSerializer implements JsonSerializer<MatchGroup> {

    private static final String JSON_PROPERTY_MATCHGROUP_ID = "matchGroupId";
    private static final String JSON_PROPERTY_TEAM_ID = "teamId";

    @Override
    public JsonElement serialize(MatchGroup src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonMatchGroup = new JsonObject();

        jsonMatchGroup.addProperty(JSON_PROPERTY_MATCHGROUP_ID, src.getId().getValue());

        for (int i = 0; i < src.getTeams().size(); i++) {
            Team team = src.getTeams().get(i);
            jsonMatchGroup.addProperty(JSON_PROPERTY_TEAM_ID + (i + 1), team.getId().getValue());
        }

        return jsonMatchGroup;
    }
}
