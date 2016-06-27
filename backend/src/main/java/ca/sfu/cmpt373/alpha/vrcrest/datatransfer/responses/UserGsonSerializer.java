package ca.sfu.cmpt373.alpha.vrcrest.datatransfer.responses;

import ca.sfu.cmpt373.alpha.vrcladder.users.User;
import ca.sfu.cmpt373.alpha.vrcladder.users.personal.UserId;
import com.google.gson.*;
import com.sun.xml.internal.ws.encoding.soap.SerializationException;

import java.lang.reflect.Type;

/**
 * Created by Samus on 22/06/2016.
 */
public class UserGsonSerializer implements JsonSerializer<User>{

    @Override
    public JsonElement serialize(User user, Type typeOfSrc, JsonSerializationContext context) {
        try{
            JsonObject jsonObject = new JsonObject();

            jsonObject.addProperty("userId", user.getUserId().getValue().toString());
            jsonObject.addProperty("userRole", user.getUserRole().name());
            jsonObject.addProperty("firstName", user.getFirstName());
            jsonObject.addProperty("middleName", user.getMiddleName());
            jsonObject.addProperty("lastName", user.getLastName());
            jsonObject.addProperty("emailAddress", user.getUserId().getValue());
            jsonObject.addProperty("phoneNumber", user.getPhoneNumber().getValue());

            return jsonObject;
        }catch (ClassCastException e){
            e.printStackTrace();
            throw new SerializationException("Unable to serialize object.");
        }
    }




}
