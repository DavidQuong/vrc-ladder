package ca.sfu.cmpt373.alpha.vrcrest.datatransfer.requests;

import ca.sfu.cmpt373.alpha.vrcladder.util.GeneratedId;
import com.google.gson.*;

import java.lang.reflect.Type;

public class NewTradingMatchGroupsPayload {

    public static class GsonDeserializer extends BaseGsonDeserializer<NewTradingMatchGroupsPayload> {

        public static final String JSON_PROPERTY_MATCHGROUP_ID_FIRST = "firstMatchGroup";
        public static final String JSON_PROPERTY_MATCHGROUP_ID_SECOND = "firstTeam";
        public static final String JSON_PROPERTY_TEAM_ID_FIRST = "secondMatchGroup";
        public static final String JSON_PROPERTY_TEAM_ID_SECOND = "secondTeam";

        @Override
        public NewTradingMatchGroupsPayload deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            checkForMissingProperties(jsonObject);

            JsonElement jsonMatchGroup1 = jsonObject.get(JSON_PROPERTY_MATCHGROUP_ID_FIRST);
            GeneratedId firstMatchGroupId = new GeneratedId(jsonMatchGroup1.getAsString());

            JsonElement jsonMatchGroup2 = jsonObject.get(JSON_PROPERTY_MATCHGROUP_ID_FIRST);
            GeneratedId secondMatchGroupId = new GeneratedId(jsonMatchGroup2.getAsString());

            JsonElement jsonTeam1 = jsonObject.get(JSON_PROPERTY_MATCHGROUP_ID_FIRST);
            GeneratedId firstTeamId = new GeneratedId(jsonTeam1.getAsString());

            JsonElement jsonTeam2 = jsonObject.get(JSON_PROPERTY_MATCHGROUP_ID_FIRST);
            GeneratedId secondTeamId = new GeneratedId(jsonTeam2.getAsString());

            return new NewTradingMatchGroupsPayload(firstMatchGroupId, secondMatchGroupId, firstTeamId, secondTeamId);
        }

        @Override
        protected void checkForMissingProperties(JsonObject jsonObject) {
            if (!jsonObject.has(JSON_PROPERTY_MATCHGROUP_ID_FIRST)) {
                throwMissingPropertyException(JSON_PROPERTY_MATCHGROUP_ID_FIRST);
            }
            if (!jsonObject.has(JSON_PROPERTY_MATCHGROUP_ID_SECOND)) {
                throwMissingPropertyException(JSON_PROPERTY_MATCHGROUP_ID_FIRST);
            }
            if (!jsonObject.has(JSON_PROPERTY_TEAM_ID_FIRST)) {
                throwMissingPropertyException(JSON_PROPERTY_MATCHGROUP_ID_FIRST);
            }
            if (!jsonObject.has(JSON_PROPERTY_TEAM_ID_SECOND)) {
                throwMissingPropertyException(JSON_PROPERTY_MATCHGROUP_ID_FIRST);
            }
        }

    }

    private GeneratedId firstMatchGroupId;
    private GeneratedId secondMatchGroupId;
    private GeneratedId firstTeamId;
    private GeneratedId secondTeamId;

    public NewTradingMatchGroupsPayload(GeneratedId firstMatchGroupId, GeneratedId secondMatchGroupId, GeneratedId firstTeamId, GeneratedId secondTeamId) {
        this.firstMatchGroupId = firstMatchGroupId;
        this.secondMatchGroupId = secondMatchGroupId;
        this.firstTeamId = firstTeamId;
        this.secondTeamId = secondTeamId;
    }

    public GeneratedId getFirstMatchGroupId() {
        return this.firstMatchGroupId;
    }

    public GeneratedId getSecondMatchGroupId() {
        return this.secondMatchGroupId;
    }

    public GeneratedId getFirstTeamId() {
        return this.firstTeamId;
    }

    public GeneratedId getSecondTeamId() {
        return this.secondTeamId;
    }

}
