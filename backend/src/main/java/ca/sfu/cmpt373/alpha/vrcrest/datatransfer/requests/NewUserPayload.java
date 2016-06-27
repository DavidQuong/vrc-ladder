package ca.sfu.cmpt373.alpha.vrcrest.datatransfer.requests;

import ca.sfu.cmpt373.alpha.vrcladder.users.authorization.UserRole;
import ca.sfu.cmpt373.alpha.vrcladder.users.personal.EmailAddress;
import ca.sfu.cmpt373.alpha.vrcladder.users.personal.PhoneNumber;
import ca.sfu.cmpt373.alpha.vrcladder.users.personal.UserId;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import org.apache.commons.lang3.EnumUtils;

import java.lang.reflect.Type;


public class NewUserPayload {

    public static final String JSON_PROPERTY_USER_ID = "userId";
    public static final String JSON_PROPERTY_USER_ROLE = "userRole";
    public static final String JSON_PROPERTY_FIRST_NAME = "firstName";
    public static final String JSON_PROPERTY_MIDDLE_NAME = "middleName";
    public static final String JSON_PROPERTY_LAST_NAME = "lastName";
    public static final String JSON_PROPERTY_EMAIL_ADDRESS = "emailAddress";
    public static final String JSON_PROPERTY_PHONE_NUMBER = "phoneNumber";
    public static final String JSON_PROPERTY_PASSWORD = "password";

    private static final String ERROR_INVALID_USER_ROLE = "Invalid userRole.";

    public static class GsonDeserializer extends BaseGsonDeserializer<NewUserPayload> {
        @Override
        public NewUserPayload deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            checkForMissingProperties(jsonObject);

            JsonElement jsonUserId = jsonObject.get(JSON_PROPERTY_USER_ID);
            UserId userId = new UserId(jsonUserId.getAsString());

            JsonElement jsonUserRole = jsonObject.get(JSON_PROPERTY_USER_ROLE);
            UserRole userRole = EnumUtils.getEnum(UserRole.class, jsonUserRole.getAsString());
            if (userRole == null) {
                throw new IllegalArgumentException(ERROR_INVALID_USER_ROLE);
            }

            String firstName = jsonObject.get(JSON_PROPERTY_FIRST_NAME).getAsString();
            String middleName = jsonObject.get(JSON_PROPERTY_MIDDLE_NAME).getAsString();
            String lastName = jsonObject.get(JSON_PROPERTY_LAST_NAME).getAsString();

            JsonElement jsonEmailAddress = jsonObject.get(JSON_PROPERTY_EMAIL_ADDRESS);
            EmailAddress emailAddress = new EmailAddress(jsonEmailAddress.getAsString());

            JsonElement jsonPhoneNumber = jsonObject.get(JSON_PROPERTY_PHONE_NUMBER);
            PhoneNumber phoneNumber = new PhoneNumber(jsonPhoneNumber.getAsString());

            JsonElement jsonPassword = jsonObject.get(JSON_PROPERTY_PASSWORD);
            String plaintextPassword = jsonPassword.getAsString();

            return new NewUserPayload(userId, userRole, firstName, middleName, lastName, emailAddress, phoneNumber,
                plaintextPassword);
        }

        protected void checkForMissingProperties(JsonObject jsonObject) {
            if (!jsonObject.has(JSON_PROPERTY_USER_ID)) {
                throwMissingPropertyException(JSON_PROPERTY_USER_ID);
            }

            if (!jsonObject.has(JSON_PROPERTY_USER_ROLE)) {
                throwMissingPropertyException(JSON_PROPERTY_USER_ROLE);
            }

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

            if (!jsonObject.has(JSON_PROPERTY_PASSWORD)) {
                throwMissingPropertyException(JSON_PROPERTY_PASSWORD);
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
