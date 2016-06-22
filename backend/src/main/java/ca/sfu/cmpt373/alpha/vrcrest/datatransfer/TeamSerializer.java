package ca.sfu.cmpt373.alpha.vrcrest.datatransfer;

import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.PlayTime;
import ca.sfu.cmpt373.alpha.vrcladder.users.User;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class TeamSerializer implements JsonSerializer<Team> {

    @Override
    public JsonElement serialize(Team team, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonTeam = new JsonObject();

        jsonTeam.addProperty("teamId", team.getId().getValue());

        jsonTeam.addProperty("ladderPosition", team.getLadderPosition().getValue());

        PlayTime playTime = team.getAttendanceCard().getPreferredPlayTime();
        jsonTeam.addProperty("playTime", playTime.getDisplayTime());

        User firstPlayer = team.getFirstPlayer();
        JsonObject jsonFirstPlayer = new JsonObject();
        jsonFirstPlayer.addProperty("userId", firstPlayer.getUserId().getValue());
        jsonFirstPlayer.addProperty("name", firstPlayer.getDisplayName());
        jsonTeam.add("firstPlayer", jsonFirstPlayer);

        User secondPlayer = team.getSecondPlayer();
        JsonObject jsonSecondPlayer = new JsonObject();
        jsonSecondPlayer.addProperty("userId", secondPlayer.getUserId().getValue());
        jsonSecondPlayer.addProperty("name", secondPlayer.getDisplayName());
        jsonTeam.add("secondPlayer", jsonSecondPlayer);

        return jsonTeam;
    }

}
