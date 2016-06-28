package ca.sfu.cmpt373.alpha.vrcrest.datatransfer.requests;

import ca.sfu.cmpt373.alpha.vrcladder.users.personal.EmailAddress;
import ca.sfu.cmpt373.alpha.vrcladder.users.personal.PhoneNumber;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class UpdateUserPayload {

    public static final String JSON_PROPERTY_FIRST_NAME = "firstName";
    public static final String JSON_PROPERTY_MIDDLE_NAME = "middleName";
    public static final String JSON_PROPERTY_LAST_NAME = "lastName";
    public static final String JSON_PROPERTY_EMAIL_ADDRESS = "emailAddress";
    public static final String JSON_PROPERTY_PHONE_NUMBER = "phoneNumber";

    public static class GsonDeserialiezr extends BaseGsonDeserializer<UpdateUserPayload> {

        @Override
        public UpdateUserPayload deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            checkForMissingProperties(jsonObject);

            String firstName = jsonObject.get(JSON_PROPERTY_FIRST_NAME).getAsString();
            String middleName = jsonObject.get(JSON_PROPERTY_MIDDLE_NAME).getAsString();
            String lastName = jsonObject.get(JSON_PROPERTY_LAST_NAME).getAsString();

            JsonElement jsonEmailAddress = jsonObject.get(JSON_PROPERTY_EMAIL_ADDRESS);
            EmailAddress emailAddress = new EmailAddress(jsonEmailAddress.getAsString());

            JsonElement jsonPhoneNumber = jsonObject.get(JSON_PROPERTY_PHONE_NUMBER);
            PhoneNumber phoneNumber = new PhoneNumber(jsonPhoneNumber.getAsString());

            return new UpdateUserPayload(firstName, middleName, lastName, emailAddress, phoneNumber);
        }

        @Override
        protected void checkForMissingProperties(JsonObject jsonObject) {
            if (!jsonObject.has(JSON_PROPERTY_FIRST_NAME)) {
                throwMissingPropertyException(JSON_PROPERTY_FIRST_NAME);
            }

            if (!jsonObject.has(JSON_PROPERTY_MIDDLE_NAME)) {
                throwMissingPropertyException(JSON_PROPERTY_MIDDLE_NAME);
            }

            if (!jsonObject.has(JSON_PROPERTY_LAST_NAME)) {
                throwMissingPropertyException(JSON_PROPERTY_LAST_NAME);
            }

            if (!jsonObject.has(JSON_PROPERTY_EMAIL_ADDRESS)) {
                throwMissingPropertyException(JSON_PROPERTY_EMAIL_ADDRESS);
            }

            if (!jsonObject.has(JSON_PROPERTY_PHONE_NUMBER)) {
                throwMissingPropertyException(JSON_PROPERTY_PHONE_NUMBER);
            }
        }
    }

    private String firstName;
    private String middleName;
    private String lastName;
    private EmailAddress emailAddress;
    private PhoneNumber phoneNumber;

    public UpdateUserPayload(String firstName, String middleName, String lastName, EmailAddress emailAddress,
        PhoneNumber phoneNumber) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public EmailAddress getEmailAddress() {
        return emailAddress;
    }

    public PhoneNumber getPhoneNumber() {
        return phoneNumber;
    }

}
