package ca.sfu.cmpt373.alpha.vrcrest.routes;

import ca.sfu.cmpt373.alpha.vrcladder.exceptions.DuplicateTeamMemberException;
import ca.sfu.cmpt373.alpha.vrcladder.exceptions.EntityNotFoundException;
import ca.sfu.cmpt373.alpha.vrcladder.exceptions.ExistingTeamException;
import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
import ca.sfu.cmpt373.alpha.vrcladder.teams.TeamManager;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.AttendanceCard;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.AttendanceStatus;
import ca.sfu.cmpt373.alpha.vrcladder.util.GeneratedId;
import ca.sfu.cmpt373.alpha.vrcrest.datatransfer.requests.NewPlayTimePayload;
import ca.sfu.cmpt373.alpha.vrcrest.datatransfer.requests.NewTeamPayload;
import ca.sfu.cmpt373.alpha.vrcrest.datatransfer.responses.AttendanceCardGsonSerializer;
import ca.sfu.cmpt373.alpha.vrcrest.datatransfer.responses.TeamGsonSerializer;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSyntaxException;
import org.eclipse.jetty.http.HttpStatus;
import org.hibernate.exception.ConstraintViolationException;
import spark.Request;
import spark.Response;
import spark.Spark;

import java.util.List;

public class TeamRouter extends RestRouter {

    public static final String ROUTE_TEAMS = "/teams";
    public static final String ROUTE_TEAM_ID = "/team/" + ROUTE_ID;
    public static final String ROUTE_TEAM_ID_ATTENDANCE = "/team/" + ROUTE_ID + "/attendance";
    public static final String ROUTE_TEAM_ID_ATTENDANCE_PLAYTIME = ROUTE_TEAM_ID_ATTENDANCE + "/playtime";
    public static final String ROUTE_TEAM_ID_ATTENDANCE_STATUS = ROUTE_TEAM_ID_ATTENDANCE + "/status";

    public static final String JSON_PROPERTY_TEAMS = "teams";
    public static final String JSON_PROPERTY_TEAM = "team";
    public static final String JSON_PROPERTY_ATTENDANCE = "attendance";

    private static final String ERROR_PLAYER_ID_NOT_FOUND = "One of the provided player ID's cannot be found.";
    private static final String ERROR_EXISTING_TEAM = "The provided pair of player's already form a team.";
    private static final String ERROR_IDENTICAL_PLAYER_ID = "Cannot create a team consisting of the two same players.";
    private static final String ERROR_NONEXISTENT_TEAM = "This team does not exist.";

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
        Spark.delete(ROUTE_TEAM_ID, (request, response) -> handleDeleteTeamById(request, response));
        Spark.get(ROUTE_TEAM_ID_ATTENDANCE, (request, response) -> handleGetTeamAttendance(request, response));
        Spark.put(ROUTE_TEAM_ID_ATTENDANCE_PLAYTIME, (request, response) -> handleUpdatePlayTime(request, response));
        Spark.put(ROUTE_TEAM_ID_ATTENDANCE_STATUS, (request, response) -> handleUpdateAttendanceStatus(request,
            response));

    }

    @Override
    protected Gson buildGson() {
        return new GsonBuilder()
            .registerTypeAdapter(Team.class, new TeamGsonSerializer())
            .registerTypeAdapter(AttendanceCard.class, new AttendanceCardGsonSerializer())
            .registerTypeAdapter(NewTeamPayload.class, new NewTeamPayload.GsonDeserializer())
            .registerTypeAdapter(NewPlayTimePayload.class, new NewPlayTimePayload.GsonDeserializer())
            .setPrettyPrinting()
            .create();
    }

    private String handleGetAllTeams(Request request, Response response) {
        JsonObject responseBody = new JsonObject();

        List<Team> teams = teamManager.getAll();
        responseBody.add(JSON_PROPERTY_TEAMS, getGson().toJsonTree(teams));

        response.status(HttpStatus.OK_200);
        response.type(JSON_RESPONSE_TYPE);
        return responseBody.toString();
    }

    private String handleCreateTeam(Request request, Response response) {
        Team newTeam;
        JsonObject responseBody = new JsonObject();

        try {
            NewTeamPayload newTeamPayload = getGson().fromJson(request.body(), NewTeamPayload.class);
            newTeam = teamManager.create(newTeamPayload.getFirstPlayerId(), newTeamPayload.getSecondPlayerId());

            JsonElement jsonTeam = getGson().toJsonTree(newTeam);
            responseBody.add(JSON_PROPERTY_TEAM, jsonTeam);
            response.status(HttpStatus.CREATED_201);
        } catch (JsonSyntaxException ex) {
            responseBody.addProperty(JSON_PROPERTY_ERROR, ERROR_MALFORMED_JSON);
            response.status(HttpStatus.BAD_REQUEST_400);
        } catch (JsonParseException ex) {
            responseBody.addProperty(JSON_PROPERTY_ERROR, ex.getMessage());
            response.status(HttpStatus.BAD_REQUEST_400);
        } catch (EntityNotFoundException ex) {
            responseBody.addProperty(JSON_PROPERTY_ERROR, ERROR_PLAYER_ID_NOT_FOUND);
            response.status(HttpStatus.NOT_FOUND_404);
        } catch(ExistingTeamException | ConstraintViolationException ex) {
            responseBody.addProperty(JSON_PROPERTY_ERROR, ERROR_EXISTING_TEAM);
            response.status(HttpStatus.CONFLICT_409);
        } catch(DuplicateTeamMemberException ex)  {
            responseBody.addProperty(JSON_PROPERTY_ERROR, ERROR_IDENTICAL_PLAYER_ID);
            response.status(HttpStatus.BAD_REQUEST_400);
        } catch (RuntimeException ex) {
            responseBody.addProperty(JSON_PROPERTY_ERROR, ERROR_COULD_NOT_COMPLETE_REQUEST);
            response.status(HttpStatus.BAD_REQUEST_400);
        }

        response.type(JSON_RESPONSE_TYPE);
        return responseBody.toString();
    }

    private String handleGetTeamById(Request request, Response response) {
        JsonObject responseBody = new JsonObject();

        String paramId = request.params(ROUTE_ID);
        GeneratedId generatedId = new GeneratedId(paramId);

        try {
            Team existingTeam = teamManager.getById(generatedId);

            responseBody.add(JSON_PROPERTY_TEAM, getGson().toJsonTree(existingTeam));
            response.status(HttpStatus.OK_200);
        } catch (JsonSyntaxException ex) {
            responseBody.addProperty(JSON_PROPERTY_ERROR, ERROR_MALFORMED_JSON);
            response.status(HttpStatus.BAD_REQUEST_400);
        } catch (EntityNotFoundException ex) {
            responseBody.addProperty(JSON_PROPERTY_ERROR, ERROR_NONEXISTENT_TEAM);
            response.status(HttpStatus.NOT_FOUND_404);
        } catch (RuntimeException ex) {
            responseBody.addProperty(JSON_PROPERTY_ERROR, ERROR_COULD_NOT_COMPLETE_REQUEST);
            response.status(HttpStatus.BAD_REQUEST_400);
        }

        response.type(JSON_RESPONSE_TYPE);
        return responseBody.toString();
    }

    private String handleDeleteTeamById(Request request, Response response) {
        JsonObject responseBody = new JsonObject();

        String paramId = request.params(ROUTE_ID);
        GeneratedId generatedId = new GeneratedId(paramId);

        try {
            Team deletedTeam = teamManager.deleteById(generatedId);

            responseBody.add(JSON_PROPERTY_TEAM, getGson().toJsonTree(deletedTeam));
            response.status(HttpStatus.OK_200);
        } catch (EntityNotFoundException ex) {
            responseBody.addProperty(JSON_PROPERTY_ERROR, ERROR_NONEXISTENT_TEAM);
            response.status(HttpStatus.NOT_FOUND_404);
        } catch (RuntimeException ex) {
            responseBody.addProperty(JSON_PROPERTY_ERROR, ERROR_COULD_NOT_COMPLETE_REQUEST);
            response.status(HttpStatus.BAD_REQUEST_400);
        }

        response.type(JSON_RESPONSE_TYPE);
        return responseBody.toString();
    }

    private String handleGetTeamAttendance(Request request, Response response) {
        JsonObject responseBody = new JsonObject();

        String paramId = request.params(ROUTE_ID);
        GeneratedId generatedId = new GeneratedId(paramId);

        try {
            Team existingTeam = teamManager.getById(generatedId);
            AttendanceCard attendanceCard = existingTeam.getAttendanceCard();

            responseBody.add(JSON_PROPERTY_ATTENDANCE, getGson().toJsonTree(attendanceCard));
            response.status(HttpStatus.OK_200);
        } catch (EntityNotFoundException ex) {
            responseBody.addProperty(JSON_PROPERTY_ERROR, ERROR_NONEXISTENT_TEAM);
            response.status(HttpStatus.NOT_FOUND_404);
        } catch (RuntimeException ex) {
            responseBody.addProperty(JSON_PROPERTY_ERROR, ERROR_COULD_NOT_COMPLETE_REQUEST);
            response.status(HttpStatus.BAD_REQUEST_400);
        }

        response.type(JSON_RESPONSE_TYPE);
        return responseBody.toString();
    }

    private String handleUpdatePlayTime(Request request, Response response) {
        JsonObject responseBody = new JsonObject();

        String paramId = request.params(ROUTE_ID);
        GeneratedId generatedId = new GeneratedId(paramId);

        try {
            NewPlayTimePayload playTimePayload = getGson().fromJson(request.body(), NewPlayTimePayload.class);
            Team existingTeam = teamManager.updateAttendancePlaytime(generatedId, playTimePayload.getPlayTime());
            AttendanceCard attendanceCard = existingTeam.getAttendanceCard();

            responseBody.add(JSON_PROPERTY_ATTENDANCE, getGson().toJsonTree(attendanceCard));
            response.status(HttpStatus.OK_200);
        } catch (JsonSyntaxException ex) {
            responseBody.addProperty(JSON_PROPERTY_ERROR, ERROR_MALFORMED_JSON);
            response.status(HttpStatus.BAD_REQUEST_400);
        } catch (JsonParseException | IllegalArgumentException ex) {
            responseBody.addProperty(JSON_PROPERTY_ERROR, ex.getMessage());
            response.status(HttpStatus.BAD_REQUEST_400);
        } catch (EntityNotFoundException ex) {
            responseBody.addProperty(JSON_PROPERTY_ERROR, ERROR_NONEXISTENT_TEAM);
            response.status(HttpStatus.NOT_FOUND_404);
        } catch (RuntimeException ex) {
            responseBody.addProperty(JSON_PROPERTY_ERROR, ERROR_COULD_NOT_COMPLETE_REQUEST);
            response.status(HttpStatus.BAD_REQUEST_400);
        }

        response.type(JSON_RESPONSE_TYPE);
        return responseBody.toString();
    }

    // TODO - Enable updating a Team's AttendanceStatus.
    private String handleUpdateAttendanceStatus(Request request, Response response) {
      return null;
    }

}
