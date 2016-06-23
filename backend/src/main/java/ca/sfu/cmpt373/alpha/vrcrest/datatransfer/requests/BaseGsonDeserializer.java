package ca.sfu.cmpt373.alpha.vrcrest.datatransfer.requests;

import com.google.gson.JsonDeserializer;
import com.google.gson.JsonParseException;

public abstract class BaseGsonDeserializer<T> implements JsonDeserializer<T> {

    private static final String ERROR_PROPERTY_MISSING_FORMAT = "The %s property is missing";

    protected void throwMissingPropertyException(String propertyName) {
        String errorMsg = String.format(ERROR_PROPERTY_MISSING_FORMAT, propertyName);
        throw new JsonParseException(errorMsg);
    }

}
