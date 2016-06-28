package ca.sfu.cmpt373.alpha.vrcrest.routes;

import ca.sfu.cmpt373.alpha.vrcladder.ladder.Ladder;
import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.MatchGroup;
import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.MatchGroupManager;
import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
import ca.sfu.cmpt373.alpha.vrcladder.teams.TeamManager;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.AttendanceStatus;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.PlayTime;
import ca.sfu.cmpt373.alpha.vrcladder.util.GeneratedId;
import ca.sfu.cmpt373.alpha.vrcrest.datatransfer.requests.NewTeamIdListPayload;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.eclipse.jetty.http.HttpStatus;
import spark.Request;
import spark.Response;
import spark.Spark;

import java.util.List;
import java.util.ArrayList;

/*

LadderRouter:
	A class to modify the Ladder and its storage in the database via Rest calls from the front end.
	Operations:
		/ladder/regenerate
			Automatically recompute the new ladder AFTER all teams have played/failed to attend. Throws error if any team does not have a ScoreCard filled out.
		/ladder/rearrange
			Changes the ladder rankings for manual changes

 */

public class LadderRouter extends RestRouter{

    private static final String ROUTE_LADDER = "/ladder";

    public static final String ROUTE_LADDER_REGENERATE = ROUTE_LADDER + "/regenerate";
	public static final String ROUTE_LADDER_REARRANGE = ROUTE_LADDER + "/rearrange";

    private static final String ERROR_SCORECARDS_NOT_FILLED = "Not all MatchGroups have reported their scores yet";

    private TeamManager teamManager;
    private MatchGroupManager matchGroupManager;

    public LadderRouter(TeamManager teamManager, MatchGroupManager matchGroupManager) {
        this.teamManager = teamManager;
        this.matchGroupManager = matchGroupManager;
    }

    @Override
    public void attachRoutes() {
        Spark.put(ROUTE_LADDER_REGENERATE, this::handleRegenerateLadder);
		Spark.put(ROUTE_LADDER_REARRANGE, this::handleRearrangeLadder);
    }

	private String handleRearrangeLadder(Request request, Response response) {
		JsonObject responseBody = new JsonObject();
		try {
			NewTeamIdListPayload newTeamPayload = getGson().fromJson(request.body(), NewTeamIdListPayload.class);
			List<Team> teams = new ArrayList<>();
			List<GeneratedId> teamIds = newTeamPayload.getTeamIds();
			List<MatchGroup> matchGroups = matchGroupManager.getAll();

			for(GeneratedId teamId : teamIds) {
				teams.add(teamManager.getById(teamId));
			}

			updateLadder(teams, matchGroups);
			response.status(HttpStatus.OK_200);
		} catch (IllegalStateException e) {
			response.status(HttpStatus.BAD_REQUEST_400);
			responseBody.addProperty(JSON_PROPERTY_ERROR, e.getMessage());
		}
		return responseBody.toString();
	}

    private String handleRegenerateLadder(Request request, Response response) {
        JsonObject responseBody = new JsonObject();
        try {
            List<Team> teams = teamManager.getAll();
            List<MatchGroup> matchGroups = matchGroupManager.getAll();

            checkAllScoresReported(matchGroups);
            updateLadder(teams, matchGroups);
            resetPlayerSettings(teams);
			response.status(HttpStatus.OK_200);
        } catch (IllegalStateException e) {
            response.status(HttpStatus.BAD_REQUEST_400);
            responseBody.addProperty(JSON_PROPERTY_ERROR, e.getMessage());
        }
        return responseBody.toString();
    }

    @Override
    protected Gson buildGson() {
        return null;
    }

    private void checkAllScoresReported(List<MatchGroup> matchGroups) {
        for (MatchGroup matchGroup : matchGroups) {
            if (!matchGroup.getScoreCard().isFilledOut()) {
                throw new IllegalStateException(ERROR_SCORECARDS_NOT_FILLED);
            }
        }
    }

    private void updateLadder(List<Team> teams, List<MatchGroup> matchGroups) {
        Ladder ladder = new Ladder(teams);
		ladder.updateLadder(matchGroups);
        teamManager.updateLadderPositions(ladder.getLadder());
    }

    private void resetPlayerSettings(List<Team> teams) {
        matchGroupManager.deleteAll();
        for (Team team : teams) {
            teamManager.updateAttendancePlaytime(team.getId(), PlayTime.NONE);
            teamManager.updateAttendanceStatus(team.getId(), AttendanceStatus.PRESENT);
        }
    }
}
