package ca.sfu.cmpt373.alpha.vrcrest.routes;

import com.google.gson.Gson;
import spark.Request;
import spark.Response;
import spark.Spark;

public class MatchGroupRouter extends RestRouter {

    private static final String PARAM_MATCHGROUP_1 = RestRouter.PARAM_ID + "matchGroup1";
    private static final String PARAM_MATCHGROUP_2 = RestRouter.PARAM_ID + "matchGroup2";
    private static final String PARAM_TEAM_1 = RestRouter.PARAM_ID + "team1";
    private static final String PARAM_TEAM_2 = RestRouter.PARAM_ID + "team2";

    private static final String ROUTE_MATCHGROUP = "/matchGroup";

    public static final String ROUTE_MATCHGROUPS = "/matchGroups";
    public static final String ROUTE_MATCHGROUP_GENERATION = ROUTE_MATCHGROUPS + "/generate";
    public static final String ROUTE_MATCHGROUP_ID = ROUTE_MATCHGROUP + RestRouter.PARAM_ID;
    public static final String ROUTE_MATCHGROUP_SCORES = ROUTE_MATCHGROUP + RestRouter.PARAM_ID + "/scores";
    public static final String ROUTE_MATCHGROUPS_SWAP_TEAMS = ROUTE_MATCHGROUPS +
            PARAM_MATCHGROUP_1 +
            PARAM_MATCHGROUP_2 +
            PARAM_TEAM_1 +
            PARAM_TEAM_2;

    @Override
    public void attachRoutes() {
        Spark.get(ROUTE_MATCHGROUPS, this::handleGetAllMatchGroups);
        Spark.get(ROUTE_MATCHGROUP_ID, this::handleGetMatchGroupById);
        Spark.get(ROUTE_MATCHGROUP_GENERATION, this::handleGenerateMatchGroups);
        Spark.put(ROUTE_MATCHGROUP_SCORES, this::handleUpdateMatchGroupScores);
        Spark.delete(ROUTE_MATCHGROUP_ID, this::handleDeleteMatchGroup);
        Spark.post(ROUTE_MATCHGROUPS_SWAP_TEAMS, this::handleSwapMatchGroupTeams);
    }

    @Override
    protected Gson buildGson() {
        return null;
    }

    private String handleGetAllMatchGroups(Request request, Response response) {
        return null;
    }

    private String handleGetMatchGroupById(Request request, Response response) {
        return null;
    }

    private String handleGenerateMatchGroups(Request request, Response response) {
        return null;
    }


    private String handleUpdateMatchGroupScores(Request request, Response response) {
        return null;
    }

    private String handleDeleteMatchGroup(Request request, Response response) {
        return null;
    }

    private String handleSwapMatchGroupTeams(Request request, Response response) {
        return null;
    }
}
