package ca.sfu.cmpt373.alpha.vrcrest.datatransfer.responses;

import ca.sfu.cmpt373.alpha.vrcladder.users.User;
import com.google.gson.*;

import java.lang.reflect.Type;

/**
 * Created by Samus on 22/06/2016.
 */
public class UserGsonSerializer implements JsonSerializer<User>{

    @Override
    public JsonElement serialize(User user, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonUser = new JsonObject();

        jsonUser.addProperty("userId", user.getUserId().getValue().toString());
        jsonUser.addProperty("userRole", user.getUserRole().name());
        jsonUser.addProperty("firstName", user.getFirstName());
        jsonUser.addProperty("middleName", user.getMiddleName());
        jsonUser.addProperty("lastName", user.getLastName());
        jsonUser.addProperty("emailAddress", user.getEmailAddress().getValue());
        jsonUser.addProperty("phoneNumber", user.getPhoneNumber().getValue());

        return jsonUser;
    }




}
