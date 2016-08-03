package ca.sfu.cmpt373.alpha.vrcrest.routes;

import ca.sfu.cmpt373.alpha.vrcladder.ApplicationManager;
import ca.sfu.cmpt373.alpha.vrcladder.exceptions.PdfCouldNotBeCreatedException;
import ca.sfu.cmpt373.alpha.vrcladder.exceptions.TemplateNotFoundException;
import ca.sfu.cmpt373.alpha.vrcladder.file.PdfManager;
import ca.sfu.cmpt373.alpha.vrcladder.ladder.Ladder;
import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.CourtManager;
import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.MatchGroup;
import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.MatchGroupManager;
import ca.sfu.cmpt373.alpha.vrcladder.persistence.PersistenceConstants;
import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
import ca.sfu.cmpt373.alpha.vrcladder.teams.TeamManager;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.AttendanceStatus;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.PlayTime;
import ca.sfu.cmpt373.alpha.vrcladder.users.User;
import ca.sfu.cmpt373.alpha.vrcladder.util.GeneratedId;
import ca.sfu.cmpt373.alpha.vrcrest.datatransfer.requests.NewTeamIdListPayload;
import ca.sfu.cmpt373.alpha.vrcrest.security.RouteSignature;
import com.google.gson.*;
import org.eclipse.jetty.http.HttpStatus;
import spark.Request;
import spark.Response;
import spark.Spark;

import java.util.Collections;
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
    public static final String ROUTE_LADDER_PDF = ROUTE_LADDER + "/pdf";

    private static final String ERROR_SCORECARDS_NOT_FILLED = "Not all MatchGroups have reported their scores yet";

    private TeamManager teamManager;
    private MatchGroupManager matchGroupManager;
    private CourtManager courtManager;

    public LadderRouter(ApplicationManager applicationManager) {
        super(applicationManager);
        teamManager = applicationManager.getTeamManager();
        matchGroupManager = applicationManager.getMatchGroupManager();
        courtManager = applicationManager.getCourtManager();
    }

    @Override
    protected Gson buildGson() {
        return new GsonBuilder()
                .registerTypeAdapter(NewTeamIdListPayload.class, new NewTeamIdListPayload.GsonDeserializer())
                .setPrettyPrinting()
                .create();
    }

    @Override
    public void attachRoutes() {
        Spark.put(ROUTE_LADDER_REGENERATE, this::handleRegenerateLadder);
        Spark.put(ROUTE_LADDER_REARRANGE, this::handleRearrangeLadder);
        Spark.post(ROUTE_LADDER_PDF, this::handleRequestPDF);
    }

    @Override
    public List<RouteSignature> getPublicRouteSignatures() {
        return Collections.emptyList();
    }

    private String handleRearrangeLadder(Request request, Response response) {
        checkForVolunteerRole(request);

        JsonObject responseBody = new JsonObject();
        try {
            NewTeamIdListPayload newTeamPayload = getGson().fromJson(request.body(), NewTeamIdListPayload.class);

            List<Team> teams = new ArrayList<>();
            List<GeneratedId> teamIds = newTeamPayload.getTeamIds();

            for(GeneratedId teamId : teamIds) {
                teams.add(teamManager.getById(teamId));
            }

            teamManager.updateLadderPositions(teams);
            response.status(HttpStatus.OK_200);
        } catch (IllegalStateException e) {
            response.status(HttpStatus.BAD_REQUEST_400);
            responseBody.addProperty(JSON_PROPERTY_ERROR, e.getMessage());
        } catch (JsonSyntaxException ex) {
            responseBody.addProperty(JSON_PROPERTY_ERROR, ERROR_MALFORMED_JSON);
            response.status(HttpStatus.BAD_REQUEST_400);
        } catch (JsonParseException ex) {
            responseBody.addProperty(JSON_PROPERTY_ERROR, ex.getMessage());
            response.status(HttpStatus.BAD_REQUEST_400);
        }
        return responseBody.toString();
    }

    private String handleRegenerateLadder(Request request, Response response) {
        checkForVolunteerRole(request);

        JsonObject responseBody = new JsonObject();
        try {
            List<Team> teams = teamManager.getAll();
            List<MatchGroup> matchGroups = matchGroupManager.getAll();

            checkAllScoresReported(matchGroups);
            regenerateLadder(teams, matchGroups);
            resetPlayerSettings(teams);
            response.status(HttpStatus.OK_200);
        } catch (IllegalStateException e) {
            response.status(HttpStatus.BAD_REQUEST_400);
            responseBody.addProperty(JSON_PROPERTY_ERROR, e.getMessage());
        }
        return responseBody.toString();
    }

    private String handleRequestPDF(Request request, Response response) {
        checkForVolunteerRole(request);
        User adminRequestor = extractUserFromRequest(request);

        JsonObject responseBody = new JsonObject();
        try{
            Ladder ladderToExport = new Ladder(teamManager.getAll());
            PdfManager pdfManager = new PdfManager(ladderToExport);
            pdfManager.exportLadder(adminRequestor);
            response.status(HttpStatus.CREATED_201);
        } catch (TemplateNotFoundException | PdfCouldNotBeCreatedException ex){
            ex.printStackTrace();
            responseBody.addProperty(PersistenceConstants.PDF_GENERATED, false);
            responseBody.addProperty(ex.getMessage(), true);
            response.status(HttpStatus.SERVICE_UNAVAILABLE_503);
        }

        return responseBody.toString();
    }

    private void checkAllScoresReported(List<MatchGroup> matchGroups) {
        for (MatchGroup matchGroup : matchGroups) {
            if (!matchGroup.getScoreCard().isFilledOut()) {
                throw new IllegalStateException(ERROR_SCORECARDS_NOT_FILLED);
            }
        }
    }

    private void regenerateLadder(List<Team> teams, List<MatchGroup> matchGroups) {
        Ladder ladder = new Ladder(teams);
        ladder.updateLadder(matchGroups);
        teamManager.updateLadderPositions(ladder.getLadder());
    }

    private void resetPlayerSettings(List<Team> teams) {
        courtManager.deleteAll();
        matchGroupManager.deleteAll();
        for (Team team : teams) {
            teamManager.updateAttendancePlaytime(team.getId(), PlayTime.NONE);
            teamManager.updateAttendanceStatus(team.getId(), AttendanceStatus.PRESENT);
        }
    }
}
