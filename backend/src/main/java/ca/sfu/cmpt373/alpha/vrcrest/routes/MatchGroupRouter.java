package ca.sfu.cmpt373.alpha.vrcrest.routes;

import ca.sfu.cmpt373.alpha.vrcladder.exceptions.MatchMakingException;
import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.Court;
import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.MatchGroup;
import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.MatchGroupManager;
import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.logic.MatchGroupGenerator;
import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.logic.MatchScheduler;
import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
import ca.sfu.cmpt373.alpha.vrcladder.teams.TeamManager;
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
    private TeamManager teamManager;

    public MatchGroupRouter(MatchGroupManager matchGroupManager, TeamManager teamManager) {
        this.matchGroupManager = matchGroupManager;
        this.teamManager = teamManager;
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
    public static final String ROUTE_MATCHGROUP_ADD_TEAM =
            ROUTE_MATCHGROUP +
            "/" + PARAM_MATCHGROUP_1 +
            "/add" +
            "/" + PARAM_TEAM_1;
    public static final String ROUTE_MATCHGROUP_REMOVE_TEAM =
            ROUTE_MATCHGROUP +
            "/" + PARAM_MATCHGROUP_1 +
            "/remove" +
            "/" + PARAM_TEAM_1;

    private static final String JSON_PROPERTY_MATCHGROUPS = "matchGroups";
    private static final String JSON_PROPERTY_MATCHGROUP = "matchGroup";

    private static final String ERROR_MATCHGROUP_ID_NOT_FOUND = "No MatchGroup was found for the given ID.";

    @Override
    public void attachRoutes() {
        Spark.get(ROUTE_MATCHGROUPS, this::handleGetAllMatchGroups);
        Spark.get(ROUTE_MATCHGROUP_ID, this::handleGetMatchGroupById);
        Spark.put(ROUTE_MATCHGROUP_SCORES, this::handleUpdateMatchGroupScores);
        Spark.delete(ROUTE_MATCHGROUP_ID, this::handleDeleteMatchGroup);
        Spark.post(ROUTE_MATCHGROUP_GENERATION, this::handleGenerateMatchGroups);
        Spark.put(ROUTE_MATCHGROUPS_SWAP_TEAMS, this::handleSwapMatchGroupTeams);
        Spark.put(ROUTE_MATCHGROUP_ADD_TEAM, this::handleAddMatchGroupTeam);
        Spark.put(ROUTE_MATCHGROUP_REMOVE_TEAM, this::handleRemoveMatchGroupTeam);
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
            String matchGroupIdParam = request.params(PARAM_ID);

            //TODO: make sure that EntityNotFoundException is thrown in this after rebasing
            MatchGroup matchGroup = matchGroupManager.getById(new GeneratedId(matchGroupIdParam));
            responseBody.add(JSON_PROPERTY_MATCHGROUP, getGson().toJsonTree(matchGroup));

            response.type(JSON_RESPONSE_TYPE);
        } catch (JsonSyntaxException e) {
            response.status(HttpStatus.BAD_REQUEST_400);
            responseBody.addProperty(JSON_PROPERTY_ERROR, ERROR_MALFORMED_JSON);
            return responseBody.toString();
        } catch (EntityNotFoundException e) {
            response.status(HttpStatus.NOT_FOUND_404);
            responseBody.addProperty(JSON_PROPERTY_ERROR, ERROR_MATCHGROUP_ID_NOT_FOUND);
            return responseBody.toString();
        }
        return responseBody.toString();
    }

    private String handleGenerateMatchGroups(Request request, Response response) {
        JsonObject responseBody = new JsonObject();
        try {
            //TODO: delete all MatchGroups and Courts
            matchGroupManager.deleteAll();
            List<Team> teams = teamManager.getAll();
            List<Court> courts = MatchScheduler.scheduleMatches(
                    MatchScheduler.DEFAULT_NUM_COURTS,
                    MatchGroupGenerator.generateMatchGroupings(teams));
            for (Court court : courts) {
                for (MatchGroup matchGroup : court.getScheduledMatches().values()) {
                    //TODO: catch any exceptions that occur as a result of this creation
                    //ex: matchGroup already exists for these teams
                    matchGroupManager.create(matchGroup);
                }
                //TODO: save courts in database
            }
        } catch (MatchMakingException e) {
            response.status(HttpStatus.BAD_REQUEST_400);
            responseBody.addProperty(JSON_PROPERTY_ERROR, e.getMessage());
            return responseBody.toString();
        }
        // if everything goes as planned, the matchGroups and courts will be generated, but the
        //response will be empty
        return responseBody.toString();
    }

    private String handleUpdateMatchGroupScores(Request request, Response response) {
        return null;
    }

    private String handleDeleteMatchGroup(Request request, Response response) {
        JsonObject responseBody = new JsonObject();
        try {
            String matchGroupIdParam = request.params(PARAM_ID);
            matchGroupManager.deleteById(new GeneratedId(matchGroupIdParam));
        } catch (EntityNotFoundException e) {
            response.status(HttpStatus.NOT_FOUND_404);
            responseBody.addProperty(JSON_PROPERTY_ERROR, ERROR_MATCHGROUP_ID_NOT_FOUND);
            return responseBody.toString();
        }
        return responseBody.toString();
    }

    private String handleSwapMatchGroupTeams(Request request, Response response) {
        return null;
    }

    private String handleAddMatchGroupTeam(Request request, Response response) {
        return null;
    }

    private String handleRemoveMatchGroupTeam(Request request, Response response) {
        return null;
    }
}
