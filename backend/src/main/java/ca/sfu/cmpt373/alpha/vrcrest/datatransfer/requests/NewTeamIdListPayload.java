package ca.sfu.cmpt373.alpha.vrcrest.datatransfer.requests;

import ca.sfu.cmpt373.alpha.vrcladder.util.GeneratedId;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.util.Collections;
import java.util.List;
import java.util.ArrayList;

import java.lang.reflect.Type;

public class NewTeamIdListPayload {

    public static class GsonDeserializer extends BaseGsonDeserializer<NewTeamIdListPayload> {

        public static final String JSON_PROPERTY_TEAM_ID_LIST = "teamIds";

        @Override
        public NewTeamIdListPayload deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            checkForMissingProperties(jsonObject);

            JsonElement jsonTeamIds = jsonObject.get(JSON_PROPERTY_TEAM_ID_LIST);
            JsonArray jsonArrayTeamIds = jsonTeamIds.getAsJsonArray();
            List<GeneratedId> teamIds = new ArrayList<>();

            for (int i = 0;i < jsonArrayTeamIds.size();i++) {
                String idAsString = jsonArrayTeamIds.get(i).getAsString();
                teamIds.add(new GeneratedId(idAsString));
            }

            return new NewTeamIdListPayload(Collections.unmodifiableList(teamIds));
        }

        @Override
        protected void checkForMissingProperties(JsonObject jsonObject) {
            if (!jsonObject.has(JSON_PROPERTY_TEAM_ID_LIST)) {
                throwMissingPropertyException(JSON_PROPERTY_TEAM_ID_LIST);
            }
        }

    }

    private List<GeneratedId> teamIds;

    public NewTeamIdListPayload(List<GeneratedId> teamIds) {
        this.teamIds = teamIds;
    }

    public List<GeneratedId> getTeamIds() {
        return Collections.unmodifiableList(this.teamIds);
    }

}
