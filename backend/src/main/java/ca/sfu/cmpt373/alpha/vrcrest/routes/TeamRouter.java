package ca.sfu.cmpt373.alpha.vrcrest.routes;

import ca.sfu.cmpt373.alpha.vrcladder.exceptions.DuplicateTeamMemberException;
import ca.sfu.cmpt373.alpha.vrcladder.exceptions.EntityNotFoundException;
import ca.sfu.cmpt373.alpha.vrcladder.exceptions.ExistingTeamException;
import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
import ca.sfu.cmpt373.alpha.vrcladder.teams.TeamManager;
import ca.sfu.cmpt373.alpha.vrcrest.datatransfer.JsonConstants;
import ca.sfu.cmpt373.alpha.vrcrest.datatransfer.TeamSerializer;
import ca.sfu.cmpt373.alpha.vrcrest.payloads.NewTeamPayload;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import org.eclipse.jetty.http.HttpStatus;
import org.hibernate.exception.ConstraintViolationException;
import spark.Request;
import spark.Response;
import spark.Spark;

import java.util.List;

// TODO - Implement private route methods.
public class TeamRouter extends RestRouter {

    public static final String ROUTE_TEAMS = "/teams";
    public static final String ROUTE_TEAM_ID = "/team/:id";
    public static final String ROUTE_TEAM_ID_ATTENDANCE = "/team/:id/attendance";

    private static final String ERROR_PLAYER_ID_NOT_FOUND = "One of the provided player ID's cannot be found.";
    private static final String ERROR_EXISTING_TEAM = "The provided pair of player's already form a team.";
    private static final String ERROR_IDENTICAL_PLAYER_ID = "Cannot create a team consisting of the two same players.";

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
        return new GsonBuilder()
            .registerTypeAdapter(Team.class, new TeamSerializer())
            .registerTypeAdapter(NewTeamPayload.class, new NewTeamPayload.GsonDeserializer())
            .setPrettyPrinting()
            .create();
    }

    // TODO - Refactor
    private String handleGetAllTeams(Request request, Response response) {
        JsonObject responseBody = new JsonObject();
        JsonArray jsonTeams = new JsonArray();

        List<Team> teams = teamManager.getAll();
        for (Team team : teams) {
            jsonTeams.add(getGson().toJsonTree(team));
        }
        responseBody.add(JsonConstants.JSON_PROPERTY_TEAMS, jsonTeams);

        response.status(HttpStatus.OK_200);
        response.type(JsonConstants.RESPONSE_TYPE);
        return responseBody.toString();
    }

    private String handleCreateTeam(Request request, Response response) {
        Team newTeam;
        JsonObject responseBody = new JsonObject();

        try {
            newTeam = createTeam(request.body());
            JsonElement jsonTeam = getGson().toJsonTree(newTeam);

            responseBody.add(JsonConstants.JSON_PROPERTY_TEAM, jsonTeam);
            response.status(HttpStatus.CREATED_201);
        } catch (RuntimeException ex) {
            responseBody.addProperty(JsonConstants.JSON_PROPERTY_ERROR, ex.getMessage());
            response.status(HttpStatus.BAD_REQUEST_400);
        }

        response.type(JsonConstants.RESPONSE_TYPE);
        return responseBody.toString();
    }

    private Team createTeam(String requestBody) {
        NewTeamPayload newTeamPayload = getGson().fromJson(requestBody, NewTeamPayload.class);

        Team createdTeam;
        try {
            createdTeam = teamManager.create(newTeamPayload.getFirstPlayerId(), newTeamPayload.getSecondPlayerId());
        } catch(EntityNotFoundException ex) {
            throw new RuntimeException(ERROR_PLAYER_ID_NOT_FOUND);
        } catch(ExistingTeamException | ConstraintViolationException ex) {
            throw new RuntimeException(ERROR_EXISTING_TEAM);
        } catch(DuplicateTeamMemberException ex) {
            throw new RuntimeException(ERROR_IDENTICAL_PLAYER_ID);
        }

        return createdTeam;
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
