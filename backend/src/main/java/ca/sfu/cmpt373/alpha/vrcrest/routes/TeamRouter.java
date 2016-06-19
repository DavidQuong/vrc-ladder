package ca.sfu.cmpt373.alpha.vrcrest.routes;

import ca.sfu.cmpt373.alpha.vrcladder.teams.TeamManager;
import ca.sfu.cmpt373.alpha.vrcrest.interfaces.RestRouter;
import spark.Route;
import spark.Spark;

// TODO - Implement private route methods.
public class TeamRouter implements RestRouter {

    public static final String ROUTE_TEAMS = "/teams";
    public static final String ROUTE_TEAM_ID = "/team/:id";
    public static final String ROUTE_TEAM_ID_ATTENDANCE = "/team/:id/attendance";

    private TeamManager teamManager;

    public TeamRouter(TeamManager teamManager) {
        this.teamManager = teamManager;
    }

    @Override
    public void attachRoutes() {
        Spark.get(ROUTE_TEAMS, getTeamsRoute());
        Spark.post(ROUTE_TEAMS, postTeamsRoute());
        Spark.get(ROUTE_TEAM_ID, getTeamByIdRoute());
        Spark.get(ROUTE_TEAM_ID, putTeamByIdRoute());
        Spark.delete(ROUTE_TEAM_ID, deleteTeamByIdRoute());
        Spark.get(ROUTE_TEAM_ID_ATTENDANCE, getTeamAttendanceRoute());
        Spark.put(ROUTE_TEAM_ID_ATTENDANCE, putTeamAttendanceRoute());
    }

    private Route getTeamsRoute() {
        return null;
    }

    private Route postTeamsRoute() {
        return null;
    }

    private Route getTeamByIdRoute() {
        return null;
    }

    private Route putTeamByIdRoute() {
        return null;
    }

    private Route deleteTeamByIdRoute() {
        return null;
    }

    private Route getTeamAttendanceRoute() {
        return null;
    }

    private Route putTeamAttendanceRoute() {
        return null;
    }

}
