package ca.sfu.cmpt373.alpha.vrcrest;

import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
import ca.sfu.cmpt373.alpha.vrcladder.users.User;
import ca.sfu.cmpt373.alpha.vrcladder.users.UserBuilder;
import ca.sfu.cmpt373.alpha.vrcladder.users.authorization.UserRole;
import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;

import java.io.IOException;
import java.io.StringWriter;

import static com.fasterxml.jackson.annotation.PropertyAccessor.FIELD;
import static spark.Spark.get;

public class RestDriver {
    public static void main(String[] args) {
        //goto http://localhost:4567/teams to see the result of this
        get("/teams", (request, response) -> {
            User testUser = new UserBuilder()
                    .setUserId("" + 1)
                    .setEmailAddress("test@test.com")
                    .setFirstName("TestUser ")
                    .setMiddleName("MiddleName")
                    .setLastName("LastName")
                    .setPhoneNumber("778-235-4841")
                    .setUserRole(UserRole.PLAYER)
                    .buildUser();
            response.status(200);
            response.type("application/json");
            return dataToJson(new Team(testUser, testUser));
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
