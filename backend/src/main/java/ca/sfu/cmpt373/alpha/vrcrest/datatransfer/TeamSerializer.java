package ca.sfu.cmpt373.alpha.vrcrest.datatransfer;

import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.PlayTime;
import ca.sfu.cmpt373.alpha.vrcladder.users.User;
import ca.sfu.cmpt373.alpha.vrcrest.datatransfer.JsonConstants;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class TeamSerializer implements JsonSerializer<Team> {

    @Override
    public JsonElement serialize(Team team, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonTeam = new JsonObject();

        jsonTeam.addProperty(JsonConstants.JSON_PROPERTY_TEAM_ID, team.getId().getValue());

        jsonTeam.addProperty(JsonConstants.JSON_PROPERTY_LADDER_POSITION, team.getLadderPosition().getValue());

        PlayTime playTime = team.getAttendanceCard().getPreferredPlayTime();
        jsonTeam.addProperty(JsonConstants.JSON_PROPERTY_PLAY_TIME, playTime.getDisplayTime());

        User firstPlayer = team.getFirstPlayer();
        JsonObject jsonFirstPlayer = new JsonObject();
        jsonFirstPlayer.addProperty(JsonConstants.JSON_PROPERTY_USER_ID, firstPlayer.getUserId().getValue());
        jsonFirstPlayer.addProperty(JsonConstants.JSON_PROPERTY_NAME, firstPlayer.getDisplayName());
        jsonTeam.add(JsonConstants.JSON_PROPERTY_FIRST_PLAYER, jsonFirstPlayer);

        User secondPlayer = team.getSecondPlayer();
        JsonObject jsonSecondPlayer = new JsonObject();
        jsonSecondPlayer.addProperty(JsonConstants.JSON_PROPERTY_USER_ID, secondPlayer.getUserId().getValue());
        jsonSecondPlayer.addProperty(JsonConstants.JSON_PROPERTY_NAME, secondPlayer.getDisplayName());
        jsonTeam.add(JsonConstants.JSON_PROPERTY_SECOND_PLAYER, jsonSecondPlayer);

        return jsonTeam;
    }

}
