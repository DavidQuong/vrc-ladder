package ca.sfu.cmpt373.alpha.vrcrest.routes.userrouting;

import ca.sfu.cmpt373.alpha.vrcladder.users.User;
import ca.sfu.cmpt373.alpha.vrcladder.users.authorization.UserRole;
import ca.sfu.cmpt373.alpha.vrcladder.users.personal.EmailAddress;
import ca.sfu.cmpt373.alpha.vrcladder.users.personal.PhoneNumber;
import ca.sfu.cmpt373.alpha.vrcladder.users.personal.UserId;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

/**
 * Created by Samus on 22/06/2016.
 */
public class UserGsonDeserializer implements JsonDeserializationContext{
    @Override
    public User deserialize(JsonElement json, Type typeOfT) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        UserId userId = new UserId(jsonObject.get("userId").getAsString());

        String userRolseAsString = jsonObject.get("userRole").getAsString();
        UserRole userRole = UserRole.valueOf(userRolseAsString);

        String firstName = jsonObject.get("firstName").getAsString();
        String middleName = jsonObject.get("middleName").getAsString();
        String lastName = jsonObject.get("lastName").getAsString();

        String emailAddressAsString = jsonObject.get("emailAddress").getAsString();
        EmailAddress emailAddress = new EmailAddress(emailAddressAsString);

        String phoneNumberAsString = jsonObject.get("phoneNumber").getAsString();
        PhoneNumber phoneNumber = new PhoneNumber(phoneNumberAsString);

        User user = new User(userId, userRole, firstName, middleName, lastName, emailAddress, phoneNumber);
        return user;
    }
}
