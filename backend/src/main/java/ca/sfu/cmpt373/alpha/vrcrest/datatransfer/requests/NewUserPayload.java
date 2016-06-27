package ca.sfu.cmpt373.alpha.vrcrest.datatransfer.requests;

import ca.sfu.cmpt373.alpha.vrcladder.users.authorization.UserRole;
import ca.sfu.cmpt373.alpha.vrcladder.users.personal.EmailAddress;
import ca.sfu.cmpt373.alpha.vrcladder.users.personal.PhoneNumber;
import ca.sfu.cmpt373.alpha.vrcladder.users.personal.UserId;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

import java.lang.reflect.Type;


public class NewUserPayload {

    public static final String JSON_PROPERTY_USERID = "userId";
    public static final String JSON_PROPERTY_USERROLE = "userRole";
    public static final String JSON_PROPERTY_FIRSTNAME = "firstName";
    public static final String JSON_PROPERTY_MIDDLENAME = "middleName";
    public static final String JSON_PROPERTY_LASTNAME = "lastName";
    public static final String JSON_PROPERTY_EMAILADDRESS = "emailAddress";
    public static final String JSON_PROPERTY_PHONENUMBER = "phoneNumber";

    public static class UserGsonDeserializer extends BaseGsonDeserializer<NewUserPayload> {
        @Override
        public NewUserPayload deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            checkForMissingProperties(jsonObject);

            UserId userId = new UserId(jsonObject.get(JSON_PROPERTY_USERID).getAsString());

            String userRolseAsString = jsonObject.get(JSON_PROPERTY_USERROLE).getAsString();
            UserRole userRole = UserRole.valueOf(userRolseAsString);

            String firstName = jsonObject.get(JSON_PROPERTY_FIRSTNAME).getAsString();
            String middleName = jsonObject.get(JSON_PROPERTY_MIDDLENAME).getAsString();
            String lastName = jsonObject.get(JSON_PROPERTY_LASTNAME).getAsString();

            String emailAddressAsString = jsonObject.get(JSON_PROPERTY_EMAILADDRESS).getAsString();
            EmailAddress emailAddress = new EmailAddress(emailAddressAsString);

            String phoneNumberAsString = jsonObject.get(JSON_PROPERTY_PHONENUMBER).getAsString();
            PhoneNumber phoneNumber = new PhoneNumber(phoneNumberAsString);

            return new NewUserPayload(userId, userRole, firstName,
                    middleName, lastName, emailAddress, phoneNumber);
        }

        private void checkForMissingProperties(JsonObject jsonObject) {
            if (!jsonObject.has(JSON_PROPERTY_USERID)) {
                throwMissingPropertyException(JSON_PROPERTY_USERID);
            }

            if (!jsonObject.has(JSON_PROPERTY_USERROLE)) {
                throwMissingPropertyException(JSON_PROPERTY_USERROLE);
            }

            if (!jsonObject.has(JSON_PROPERTY_FIRSTNAME)) {
                throwMissingPropertyException(JSON_PROPERTY_FIRSTNAME);
            }

            if (!jsonObject.has(JSON_PROPERTY_MIDDLENAME)) {
                throwMissingPropertyException(JSON_PROPERTY_MIDDLENAME);
            }

            if (!jsonObject.has(JSON_PROPERTY_LASTNAME)) {
                throwMissingPropertyException(JSON_PROPERTY_LASTNAME);
            }
            if (!jsonObject.has(JSON_PROPERTY_EMAILADDRESS)) {
                throwMissingPropertyException(JSON_PROPERTY_EMAILADDRESS);
            }
            if (!jsonObject.has(JSON_PROPERTY_PHONENUMBER)) {
                throwMissingPropertyException(JSON_PROPERTY_PHONENUMBER);
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

    public NewUserPayload(UserId userId,
                          UserRole userRole,
                          String firstName,
                          String middleName,
                          String lastName,
                          EmailAddress emailAddress,
                          PhoneNumber phoneNumber) {
        this.userId = userId;
        this.userRole = userRole;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
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
}
