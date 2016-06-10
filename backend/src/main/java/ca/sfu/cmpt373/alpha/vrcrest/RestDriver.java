package ca.sfu.cmpt373.alpha.vrcrest;

import ca.sfu.cmpt373.alpha.vrcladder.Application;
import ca.sfu.cmpt373.alpha.vrcladder.exceptions.PersistenceException;
import ca.sfu.cmpt373.alpha.vrcladder.exceptions.ValidationException;
import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
import ca.sfu.cmpt373.alpha.vrcladder.teams.attendance.PlayTime;
import ca.sfu.cmpt373.alpha.vrcladder.users.User;
import ca.sfu.cmpt373.alpha.vrcrest.payloads.NewTeamPayload;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.hibernate.PropertyValueException;

import java.io.IOException;
import java.io.StringWriter;

import static com.fasterxml.jackson.annotation.PropertyAccessor.FIELD;
import static spark.Spark.get;
import static spark.Spark.post;

public class RestDriver {
    private static final String TYPE_JSON = "application/json";
    private static final int HTTP_STATUS_OK = 200;
    private static final int HTTP_STATUS_ERROR = 400;
    private static final String ERROR_MESSAGE_JSON_PARSE = "There was an error parsing your JSON request";
    private static final String ERROR_MESSAGE_INVALID_JSON_OBJECT = "The JSON object provided was not well-formed";
    private static final String ERROR_MESSAGE_NONEXISTENT_USER_ID = "user ID does not exist";

    private static Application application = new Application();

    public static void main(String[] args) {
        //goto http://localhost:4567/teams to see the result of this
        get("/teams", (request, response) -> {
            response.type(TYPE_JSON);
            return dataToJson(application.getTeamManager().getAll());
        });

        get("/users", (request, response) -> {
            response.type(TYPE_JSON);
            return dataToJson(application.getUserManager().getAll());
        });

//        sample input JSON:
//        {
//            "userId" : "123",
//            "userRole" : "PLAYER",
//            "firstName" : "Trevor",
//            "middleName" : "McCabe",
//            "lastName" : "Clelland",
//            "emailAddress" : "tclellan@sfu.ca",
//            "phoneNumber" : "1111111111"
//        }
        post("/users/new", (request, response) -> {
            try {
                //TODO: handle errors where JSON object fields are missing.
                // ex: missing phone number field in json object results in a null phone number which crashes the program
                ObjectMapper objectMapper = new ObjectMapper();
                User newUser = objectMapper.readValue(request.body(), User.class);
                application.getUserManager().create(newUser);
                //usually, would return the id of a newly created object,
                //but users are required to provide a unique id when signing up
                return "";
            } catch (ValidationException | JsonMappingException e) {
                response.status(HTTP_STATUS_ERROR);
                return e.getMessage();
            } catch (JsonParseException e) {
                response.status(HTTP_STATUS_ERROR);
                return ERROR_MESSAGE_JSON_PARSE + "\n" + e.getMessage();
            }
        });

//        sample input JSON:
//        {
//            "userId1" : "86432",
//            "userId2" : "59152"
//        }
        post("/teams/new", (request, response) -> {
            try {
                ObjectMapper objectMapper = new ObjectMapper();
                NewTeamPayload newTeam = objectMapper.readValue(request.body(), NewTeamPayload.class);
                if (!newTeam.isValid()) {
                    response.status(HTTP_STATUS_ERROR);
                    return ERROR_MESSAGE_INVALID_JSON_OBJECT;
                }
                Team team = application.getTeamManager().create(newTeam.getUserId1().getUserId(), newTeam.getUserId2().getUserId());
                return team.getId();
            } catch (PropertyValueException e) {
                //this exception happens if userIds are provided that don't exist in the database
                response.status(HTTP_STATUS_ERROR);
                return ERROR_MESSAGE_NONEXISTENT_USER_ID;
            } catch (JsonParseException e) {
                response.status(HTTP_STATUS_ERROR);
                return ERROR_MESSAGE_JSON_PARSE + "\n" + e.getMessage();
            }
        });

//        sample input:
//        POST: /teams/attendance/TEAM_ID_IN_DATABASE
//        "TIME_SLOT_A"
        String paramTeamId = ":teamId";
        post("/teams/attendance/" + paramTeamId, (request, response) -> {
            try {
                String teamId = request.params(paramTeamId);
                ObjectMapper objectMapper = new ObjectMapper();
                PlayTime preferredPlayTime = objectMapper.readValue(request.body(), PlayTime.class);
                application.getTeamManager().updateAttendance(teamId, preferredPlayTime);
                return "";
            } catch (JsonMappingException e) {
                response.status(HTTP_STATUS_ERROR);
                return ERROR_MESSAGE_INVALID_JSON_OBJECT;
            } catch (PersistenceException e) {
                response.status(HTTP_STATUS_ERROR);
                return e.getMessage();
            }
        });
    }

    private static String dataToJson(Object data) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            mapper.enable(SerializationFeature.INDENT_OUTPUT);

            //allow json generation on private fields
            mapper.setVisibility(FIELD, JsonAutoDetect.Visibility.ANY);

            StringWriter sw = new StringWriter();
            mapper.writeValue(sw, data);
            return sw.toString();
        } catch (IOException e){
            System.out.println(e.getMessage());
            throw new RuntimeException("IOException from a StringWriter?");
        }
    }
}
