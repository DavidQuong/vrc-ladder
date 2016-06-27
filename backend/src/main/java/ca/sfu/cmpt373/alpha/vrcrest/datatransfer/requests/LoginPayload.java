package ca.sfu.cmpt373.alpha.vrcrest.datatransfer.requests;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class LoginPayload {

    public static class LoginGsonDeserializer extends BaseGsonDeserializer<LoginPayload> {

        @Override
        public LoginPayload deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
            return null;
        }

    }

}
