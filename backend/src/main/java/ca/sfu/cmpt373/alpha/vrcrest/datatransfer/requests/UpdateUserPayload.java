package ca.sfu.cmpt373.alpha.vrcrest.datatransfer.requests;

import ca.sfu.cmpt373.alpha.vrcladder.users.authentication.Password;
import ca.sfu.cmpt373.alpha.vrcladder.users.personal.EmailAddress;
import ca.sfu.cmpt373.alpha.vrcladder.users.personal.PhoneNumber;
import ca.sfu.cmpt373.alpha.vrcrest.datatransfer.JsonProperties;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;

public class UpdateUserPayload {

    public static class GsonDeserializer extends BaseGsonDeserializer<UpdateUserPayload> {

        @Override
        public UpdateUserPayload deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            checkForMissingProperties(jsonObject);

            String firstName = jsonObject.get(JsonProperties.JSON_PROPERTY_FIRST_NAME).getAsString();
            String middleName = jsonObject.get(JsonProperties.JSON_PROPERTY_MIDDLE_NAME).getAsString();
            String lastName = jsonObject.get(JsonProperties.JSON_PROPERTY_LAST_NAME).getAsString();

            JsonElement jsonEmailAddress = jsonObject.get(JsonProperties.JSON_PROPERTY_EMAIL_ADDRESS);
            EmailAddress emailAddress = new EmailAddress(jsonEmailAddress.getAsString());

            JsonElement jsonPhoneNumber = jsonObject.get(JsonProperties.JSON_PROPERTY_PHONE_NUMBER);
            PhoneNumber phoneNumber = new PhoneNumber(jsonPhoneNumber.getAsString());

            JsonElement jsonPassword = jsonObject.get(JsonProperties.JSON_PROPERTY_PASSWORD);
            Password password = new Password(jsonPassword.getAsString());

            return new UpdateUserPayload(firstName, middleName, lastName, emailAddress, phoneNumber, password);
        }

        @Override
        protected void checkForMissingProperties(JsonObject jsonObject) {
            if (!jsonObject.has(JsonProperties.JSON_PROPERTY_FIRST_NAME)) {
                throwMissingPropertyException(JsonProperties.JSON_PROPERTY_FIRST_NAME);
            }

            if (!jsonObject.has(JsonProperties.JSON_PROPERTY_MIDDLE_NAME)) {
                throwMissingPropertyException(JsonProperties.JSON_PROPERTY_MIDDLE_NAME);
            }

            if (!jsonObject.has(JsonProperties.JSON_PROPERTY_LAST_NAME)) {
                throwMissingPropertyException(JsonProperties.JSON_PROPERTY_LAST_NAME);
            }

            if (!jsonObject.has(JsonProperties.JSON_PROPERTY_EMAIL_ADDRESS)) {
                throwMissingPropertyException(JsonProperties.JSON_PROPERTY_EMAIL_ADDRESS);
            }

            if (!jsonObject.has(JsonProperties.JSON_PROPERTY_PHONE_NUMBER)) {
                throwMissingPropertyException(JsonProperties.JSON_PROPERTY_PHONE_NUMBER);
            }
            if (!jsonObject.has(JsonProperties.JSON_PROPERTY_PASSWORD)) {
                throwMissingPropertyException(JsonProperties.JSON_PROPERTY_PASSWORD);
            }
        }
    }

    private String firstName;
    private String middleName;
    private String lastName;
    private EmailAddress emailAddress;
    private PhoneNumber phoneNumber;
    private Password password;

    public UpdateUserPayload(String firstName, String middleName, String lastName, EmailAddress emailAddress,
        PhoneNumber phoneNumber, Password newPassword) {
        if(firstName != "") {
            this.firstName = firstName;
        }
        if(middleName != "") {
            this.middleName = middleName;
        }
        if(lastName != "") {
            this.lastName = lastName;
        }

        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;

        if(newPassword.toString() != ""){
            this.password = newPassword;
        }
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

    public Password getPassword(){
        return  password;
    }

    public PhoneNumber getPhoneNumber() {
        return phoneNumber;
    }

}
