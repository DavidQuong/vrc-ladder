package ca.sfu.cmpt373.alpha.vrcrest.routes;

import ca.sfu.cmpt373.alpha.vrcladder.teams.TeamManager;
import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Spark;

// TODO - Implement private route methods.
//      - Update buildGson method if necessary.
public class TeamRouter extends RestRouter {

    public static final String ROUTE_TEAMS = "/teams";
    public static final String ROUTE_TEAM_ID = "/team/:id";
    public static final String ROUTE_TEAM_ID_ATTENDANCE = "/team/:id/attendance";

    private TeamManager teamManager;

    public TeamRouter(TeamManager teamManager) {
        super();
        this.teamManager = teamManager;
    }

    @Override
    public void attachRoutes() {
        Spark.get(ROUTE_TEAMS, (request, response) -> handleGetAllTeams(request, response));
        Spark.post(ROUTE_TEAMS, (request, response) -> handleCreateTeam(request, response));
        Spark.get(ROUTE_TEAM_ID, (request, response) -> handleGetTeamById(request, response));
        Spark.get(ROUTE_TEAM_ID, (request, response) -> handleUpdateTeamById(request, response));
        Spark.delete(ROUTE_TEAM_ID, (request, response) -> handleDeleteTeamById(request, response));
        Spark.get(ROUTE_TEAM_ID_ATTENDANCE, (request, response) -> handleGetTeamAttendance(request, response));
        Spark.put(ROUTE_TEAM_ID_ATTENDANCE, (request, response) -> handleUpdateGetAttendance(request, response));
    }

    @Override
    protected Gson buildGson() {
        return new Gson();
    }

    private String handleGetAllTeams(Request request, Response response) {
        return null;
    }

    private String handleCreateTeam(Request request, Response response) {
        return null;
    }

    private String handleGetTeamById(Request request, Response response) {
        return null;
    }

    private String handleUpdateTeamById(Request request, Response response) {
        return null;
    }

    private String handleDeleteTeamById(Request request, Response response) {
        return null;
    }

    private String handleGetTeamAttendance(Request request, Response response) {
        return null;
    }

    private String handleUpdateGetAttendance(Request request, Response response) {
        return null;
    }

}
