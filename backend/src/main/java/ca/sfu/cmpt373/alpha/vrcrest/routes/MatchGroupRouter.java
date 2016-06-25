package ca.sfu.cmpt373.alpha.vrcrest.routes;

import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.MatchGroup;
import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.MatchGroupManager;
import ca.sfu.cmpt373.alpha.vrcladder.util.GeneratedId;
import ca.sfu.cmpt373.alpha.vrcrest.datatransfer.requests.MatchGroupSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import org.eclipse.jetty.http.HttpStatus;
import spark.Request;
import spark.Response;
import spark.Spark;

import javax.persistence.EntityNotFoundException;
import java.util.List;

public class MatchGroupRouter extends RestRouter {

    private MatchGroupManager matchGroupManager;

    public MatchGroupRouter(MatchGroupManager matchGroupManager) {
        this.matchGroupManager = matchGroupManager;
    }

    private static final String PARAM_MATCHGROUP_1 = PARAM_ID + "matchGroup1";
    private static final String PARAM_MATCHGROUP_2 = PARAM_ID + "matchGroup2";
    private static final String PARAM_TEAM_1 = PARAM_ID + "team1";
    private static final String PARAM_TEAM_2 = PARAM_ID + "team2";

    private static final String ROUTE_MATCHGROUP = "/matchGroup";

    public static final String ROUTE_MATCHGROUPS = "/matchGroups";
    public static final String ROUTE_MATCHGROUP_GENERATION = ROUTE_MATCHGROUPS + "/generate";
    public static final String ROUTE_MATCHGROUP_ID = ROUTE_MATCHGROUP + "/" + PARAM_ID;
    public static final String ROUTE_MATCHGROUP_SCORES = ROUTE_MATCHGROUP + "/" + PARAM_ID + "/scores";
    public static final String ROUTE_MATCHGROUPS_SWAP_TEAMS = ROUTE_MATCHGROUPS +
            "/" + PARAM_MATCHGROUP_1 +
            "/" + PARAM_MATCHGROUP_2 +
            "/" + PARAM_TEAM_1 +
            "/" + PARAM_TEAM_2;

    private static final String JSON_PROPERTY_MATCHGROUPS = "matchGroups";
    private static final String JSON_PROPERTY_MATCHGROUP = "matchGroup";

    @Override
    public void attachRoutes() {
        Spark.get(ROUTE_MATCHGROUPS, this::handleGetAllMatchGroups);
        Spark.get(ROUTE_MATCHGROUP_ID, this::handleGetMatchGroupById);
        Spark.put(ROUTE_MATCHGROUP_SCORES, this::handleUpdateMatchGroupScores);
        Spark.delete(ROUTE_MATCHGROUP_ID, this::handleDeleteMatchGroup);
        Spark.post(ROUTE_MATCHGROUP_GENERATION, this::handleGenerateMatchGroups);
        Spark.post(ROUTE_MATCHGROUPS_SWAP_TEAMS, this::handleSwapMatchGroupTeams);
    }

    @Override
    protected Gson buildGson() {
        return new GsonBuilder()
                .registerTypeAdapter(MatchGroup.class, new MatchGroupSerializer())
                .setPrettyPrinting()
                .create();
    }

    private String handleGetAllMatchGroups(Request request, Response response) {
        JsonObject responseBody = new JsonObject();
        List<MatchGroup> matchGroups = matchGroupManager.getAll();
        responseBody.add(JSON_PROPERTY_MATCHGROUPS, getGson().toJsonTree(matchGroups));
        response.type(JSON_RESPONSE_TYPE);
        return responseBody.toString();
    }

    private String handleGetMatchGroupById(Request request, Response response) {
        JsonObject responseBody = new JsonObject();
        try {
            String matchGroupParam = request.params(PARAM_ID);

            //TODO: make sure that EntityNotFoundException is thrown in this after rebasing
            MatchGroup matchGroup = matchGroupManager.getById(new GeneratedId(matchGroupParam));
            responseBody.add(JSON_PROPERTY_MATCHGROUP, getGson().toJsonTree(matchGroup));

            response.type(JSON_RESPONSE_TYPE);
        } catch (JsonSyntaxException e) {
            responseBody.addProperty(JSON_PROPERTY_ERROR, ERROR_MALFORMED_JSON);
        } catch (EntityNotFoundException e) {
            response.status(HttpStatus.NOT_FOUND_404);
        }
        return responseBody.toString();
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
