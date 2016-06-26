package ca.sfu.cmpt373.alpha.vrcrest.routes.userrouting;

import ca.sfu.cmpt373.alpha.vrcladder.users.User;
import com.google.gson.*;
import com.sun.xml.internal.ws.encoding.soap.SerializationException;

import java.lang.reflect.Type;

/**
 * Created by Samus on 22/06/2016.
 */
public class UserGsonSerializer implements JsonSerializer{
    @Override
    public JsonElement serialize(Object src, Type typeOfSrc, JsonSerializationContext context) {
        try{
            User user = (User)src;
            JsonObject jsonObject = new JsonObject();

            jsonObject.addProperty("userId", user.getUserId().getValue());
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
