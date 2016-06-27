package ca.sfu.cmpt373.alpha.vrcrest.routes;

import ca.sfu.cmpt373.alpha.vrcladder.ladder.Ladder;
import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.MatchGroup;
import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.MatchGroupManager;
import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
import ca.sfu.cmpt373.alpha.vrcladder.teams.TeamManager;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.AttendanceStatus;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.PlayTime;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.eclipse.jetty.http.HttpStatus;
import spark.Request;
import spark.Response;
import spark.Spark;

import java.util.List;

public class LadderRouter extends RestRouter{

    private static final String ROUTE_LADDER = "/ladder";
    private static final String ERROR_SCORECARDS_NOT_FILLED = "Not all MatchGroups have reported their scores yet";

    private TeamManager teamManager;
    private MatchGroupManager matchGroupManager;

    public LadderRouter(TeamManager teamManager, MatchGroupManager matchGroupManager) {
        this.teamManager = teamManager;
        this.matchGroupManager = matchGroupManager;
    }

    @Override
    public void attachRoutes() {
        Spark.put(ROUTE_LADDER, this::handleUpdateLadder);
        //TODO: add manual team ladder position swapping
    }

    private String handleUpdateLadder(Request request, Response response) {
        JsonObject responseBody = new JsonObject();
        try {
            //TODO: make sure MatchGroups are sorted
            List<Team> teams = teamManager.getAll();
            List<MatchGroup> matchGroups = matchGroupManager.getAll();

            checkAllScoresReported(matchGroups);
            updateLadder(teams, matchGroups);
            resetPlayerSettings(teams);
        } catch (IllegalStateException e) {
            response.status(HttpStatus.BAD_REQUEST_400);
            responseBody.addProperty(JSON_PROPERTY_ERROR, e.getMessage());
        }
        return  responseBody.toString();
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
