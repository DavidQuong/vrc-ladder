package ca.sfu.cmpt373.alpha.vrcrest.datatransfer.responses;

import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.PlayTime;
import ca.sfu.cmpt373.alpha.vrcladder.users.User;
import ca.sfu.cmpt373.alpha.vrcrest.datatransfer.JsonProperties;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class TeamGsonSerializer implements JsonSerializer<Team> {

    public static final String JSON_PROPERTY_LADDER_POSITION = "ladderPosition";
    public static final String JSON_PROPERTY_PLAY_TIME = "playTime";
    public static final String JSON_PROPERTY_USER_ID = "userId";
    public static final String JSON_PROPERTY_NAME = "name";
    public static final String JSON_PROPERTY_FIRST_PLAYER = "firstPlayer";
    public static final String JSON_PROPERTY_SECOND_PLAYER = "secondPlayer";

    @Override
    public JsonElement serialize(Team team, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonTeam = new JsonObject();

        jsonTeam.addProperty(JsonProperties.JSON_PROPERTY_TEAM_ID, team.getId().getValue());

        jsonTeam.addProperty(JSON_PROPERTY_LADDER_POSITION, team.getLadderPosition().getValue());

        PlayTime playTime = team.getAttendanceCard().getPreferredPlayTime();
        jsonTeam.addProperty(JSON_PROPERTY_PLAY_TIME, playTime.toString());

        User firstPlayer = team.getFirstPlayer();
        JsonObject jsonFirstPlayer = new JsonObject();
        jsonFirstPlayer.addProperty(JSON_PROPERTY_USER_ID, firstPlayer.getUserId().getValue());
        jsonFirstPlayer.addProperty(JSON_PROPERTY_NAME, firstPlayer.getDisplayName());
        jsonTeam.add(JSON_PROPERTY_FIRST_PLAYER, jsonFirstPlayer);

        User secondPlayer = team.getSecondPlayer();
        JsonObject jsonSecondPlayer = new JsonObject();
        jsonSecondPlayer.addProperty(JSON_PROPERTY_USER_ID, secondPlayer.getUserId().getValue());
        jsonSecondPlayer.addProperty(JSON_PROPERTY_NAME, secondPlayer.getDisplayName());
        jsonTeam.add(JSON_PROPERTY_SECOND_PLAYER, jsonSecondPlayer);

        return jsonTeam;
    }

}
