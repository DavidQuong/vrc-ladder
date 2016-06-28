package ca.sfu.cmpt373.alpha.vrcrest.datatransfer.requests;

import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.MatchGroup;
import ca.sfu.cmpt373.alpha.vrcladder.util.GeneratedId;
import ca.sfu.cmpt373.alpha.vrcladder.util.IdType;
import ca.sfu.cmpt373.alpha.vrcrest.datatransfer.JsonProperties;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

public class ScoreCardPayload {

    public static class GsonDeserializer extends BaseGsonDeserializer<ScoreCardPayload> {

        private static final String ERROR_TOO_MANY_TEAMS = "There are too many teams in this ScoreCard";

        @Override
        public ScoreCardPayload deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = (JsonObject) json;

            checkForMissingProperties(jsonObject);

            List<IdType> teamIds = new ArrayList<>();
            for (int i = 0; i < MatchGroup.MAX_NUM_TEAMS; i++) {
                int teamNumber = i + 1;
                //we've already checked there's a valid amount of teams in the JSON object,
                //so when a property doesn't exist, just stop scanning.
                if (!jsonObject.has(JsonProperties.JSON_PROPERTY_TEAM_ID + teamNumber)) {
                    break;
                }
                JsonElement jsonTeamId = jsonObject.get(JsonProperties.JSON_PROPERTY_TEAM_ID + teamNumber);
                IdType teamId = new GeneratedId(jsonTeamId.getAsString());
                teamIds.add(teamId);
            }

            return new ScoreCardPayload(teamIds);
        }

        @Override
        protected void checkForMissingProperties(JsonObject jsonObject) {
            for (int i = 0; i < MatchGroup.MAX_NUM_TEAMS; i++) {
                int teamNumber = i + 1;
                if (!jsonObject.has(JsonProperties.JSON_PROPERTY_TEAM_ID + teamNumber)) {
                    boolean isValidTeamCount =
                            MatchGroup.MIN_NUM_TEAMS <= teamNumber && teamNumber <= MatchGroup.MAX_NUM_TEAMS;
                    if (isValidTeamCount) {
                        break;
                    } else if (teamNumber < MatchGroup.MIN_NUM_TEAMS) {
                        throwMissingPropertyException(JsonProperties.JSON_PROPERTY_TEAM_ID + teamNumber);
                    } else if (teamNumber > MatchGroup.MAX_NUM_TEAMS) {
                        throw new JsonParseException(ERROR_TOO_MANY_TEAMS);
                    }
                }
            }
        }
    }

    private List<IdType> teamIds;

    public ScoreCardPayload(List<IdType> teamIds) {
        this.teamIds = teamIds;
    }

    public List<IdType> getTeamIds() {
        return teamIds;
    }
}
