package ca.sfu.cmpt373.alpha.vrcrest.datatransfer.requests;

import ca.sfu.cmpt373.alpha.vrcladder.users.authorization.UserRole;
import ca.sfu.cmpt373.alpha.vrcladder.users.personal.EmailAddress;
import ca.sfu.cmpt373.alpha.vrcladder.users.personal.PhoneNumber;
import ca.sfu.cmpt373.alpha.vrcladder.users.personal.UserId;
import ca.sfu.cmpt373.alpha.vrcrest.datatransfer.JsonProperties;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import org.apache.commons.lang3.EnumUtils;

import java.lang.reflect.Type;


public class NewUserPayload {

    private static final String ERROR_INVALID_USER_ROLE = "Invalid userRole.";

    public static class GsonDeserializer extends BaseGsonDeserializer<NewUserPayload> {
        @Override
        public NewUserPayload deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
            throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            checkForMissingProperties(jsonObject);

            JsonElement jsonUserId = jsonObject.get(JsonProperties.JSON_PROPERTY_USER_ID);
            UserId userId = new UserId(jsonUserId.getAsString());

            JsonElement jsonUserRole = jsonObject.get(JsonProperties.JSON_PROPERTY_USER_ROLE);
            UserRole userRole = EnumUtils.getEnum(UserRole.class, jsonUserRole.getAsString());
            if (userRole == null) {
                throw new IllegalArgumentException(ERROR_INVALID_USER_ROLE);
            }

            String firstName = jsonObject.get(JsonProperties.JSON_PROPERTY_FIRST_NAME).getAsString();
            String middleName = jsonObject.get(JsonProperties.JSON_PROPERTY_MIDDLE_NAME).getAsString();
            String lastName = jsonObject.get(JsonProperties.JSON_PROPERTY_LAST_NAME).getAsString();

            JsonElement jsonEmailAddress = jsonObject.get(JsonProperties.JSON_PROPERTY_EMAIL_ADDRESS);
            EmailAddress emailAddress = new EmailAddress(jsonEmailAddress.getAsString());

            JsonElement jsonPhoneNumber = jsonObject.get(JsonProperties.JSON_PROPERTY_PHONE_NUMBER);
            PhoneNumber phoneNumber = new PhoneNumber(jsonPhoneNumber.getAsString());

            JsonElement jsonPassword = jsonObject.get(JsonProperties.JSON_PROPERTY_PASSWORD);
            String plaintextPassword = jsonPassword.getAsString();

            return new NewUserPayload(userId, userRole, firstName, middleName, lastName, emailAddress, phoneNumber,
                plaintextPassword);
        }

        protected void checkForMissingProperties(JsonObject jsonObject) {
            if (!jsonObject.has(JsonProperties.JSON_PROPERTY_USER_ID)) {
                throwMissingPropertyException(JsonProperties.JSON_PROPERTY_USER_ID);
            }

            if (!jsonObject.has(JsonProperties.JSON_PROPERTY_USER_ROLE)) {
                throwMissingPropertyException(JsonProperties.JSON_PROPERTY_USER_ROLE);
            }

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

    private UserId userId;
    private UserRole userRole;
    private String firstName;
    private String middleName;
    private String lastName;
    private EmailAddress emailAddress;
    private PhoneNumber phoneNumber;
    private String password;

    public NewUserPayload(UserId userId, UserRole userRole, String firstName, String middleName, String lastName,
                          EmailAddress emailAddress, PhoneNumber phoneNumber, String password) {
        this.userId = userId;
        this.userRole = userRole;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
        this.password = password;
    }

    public UserId getUserId() {
        return userId;
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getFirstName() {
        return firstName;
    }

    public UserRole getUserRole() {
        return userRole;
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

    public String getPassword() {
        return password;
    }

}
