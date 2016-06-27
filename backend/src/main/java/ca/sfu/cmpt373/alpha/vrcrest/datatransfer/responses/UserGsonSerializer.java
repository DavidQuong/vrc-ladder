package ca.sfu.cmpt373.alpha.vrcrest.datatransfer.responses;

import ca.sfu.cmpt373.alpha.vrcladder.users.User;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;

public class UserGsonSerializer implements JsonSerializer<User> {

    public static final String JSON_PROPERTY_USERID = "userId";
    public static final String JSON_PROPERTY_USERROLE = "userRole";
    public static final String JSON_PROPERTY_FIRSTNAME = "firstName";
    public static final String JSON_PROPERTY_MIDDLENAME = "middleName";
    public static final String JSON_PROPERTY_LASTNAME = "lastName";
    public static final String JSON_PROPERTY_EMAILADDRESS = "emailAddress";
    public static final String JSON_PROPERTY_PHONENUMBER= "phoneNumber";

    @Override
    public JsonElement serialize(User user, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonUser = new JsonObject();

        jsonUser.addProperty(JSON_PROPERTY_USERID, user.getUserId().getValue().toString());
        jsonUser.addProperty(JSON_PROPERTY_USERROLE, user.getUserRole().name());
        jsonUser.addProperty(JSON_PROPERTY_FIRSTNAME, user.getFirstName());
        jsonUser.addProperty(JSON_PROPERTY_MIDDLENAME, user.getMiddleName());
        jsonUser.addProperty(JSON_PROPERTY_LASTNAME, user.getLastName());
        jsonUser.addProperty(JSON_PROPERTY_EMAILADDRESS, user.getEmailAddress().getValue());
        jsonUser.addProperty(JSON_PROPERTY_PHONENUMBER, user.getPhoneNumber().getValue());

        return jsonUser;
    }
}
