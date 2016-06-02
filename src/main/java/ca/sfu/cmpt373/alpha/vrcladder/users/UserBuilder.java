package ca.sfu.cmpt373.alpha.vrcladder.users;

import ca.sfu.cmpt373.alpha.vrcladder.users.authorization.UserRole;
import ca.sfu.cmpt373.alpha.vrcladder.users.personal.EmailAddress;
import ca.sfu.cmpt373.alpha.vrcladder.users.personal.PhoneNumber;
import ca.sfu.cmpt373.alpha.vrcladder.users.personal.UserId;

public class UserBuilder {

    private static final String ERROR_MSG_NULL_USER_ID = "User ID cannot be null.";
    private static final String ERROR_MSG_NULL_USER_ROLE = "User role cannot be null.";
    private static final String ERROR_MSG_NULL_FIRST_NAME = "First name cannot be null.";
    private static final String ERROR_MSG_NULL_MIDDLE_NAME = "Middle name cannot be null.";
    private static final String ERROR_MSG_NULL_LAST_NAME = "Last name cannot be null.";
    private static final String ERROR_MSG_NULL_EMAIL_ADDRESS = "Email address cannot be null.";
    private static final String ERROR_MSG_NULL_USER_PHONE_NUMBER = "Phone number cannot be null.";

    private UserId userId;
    private UserRole userRole;
    private String firstName;
    private String middleName;
    private String lastName;
    private EmailAddress emailAddress;
    private PhoneNumber phoneNumber;

    public UserBuilder() {
        userId = null;
        userRole = null;
        firstName = null;
        middleName = null;
        lastName = null;
        emailAddress = null;
        phoneNumber = null;
    }

    public UserBuilder setUserId(UserId userId) {
        this.userId = userId;

        return this;
    }

    public UserBuilder setUserId(String userId) {
        this.userId = new UserId(userId);

        return this;
    }

    public UserBuilder setUserRole(UserRole userRole) {
        this.userRole = userRole;

        return this;
    }

    public UserBuilder setFirstName(String firstName) {
        this.firstName = firstName;

        return this;
    }

    public UserBuilder setMiddleName(String middleName) {
        this.middleName = middleName;

        return this;
    }

    public UserBuilder setLastName(String lastName) {
        this.lastName = lastName;

        return this;
    }

    public UserBuilder setEmailAddress(EmailAddress emailAddress) {
        this.emailAddress = emailAddress;

        return this;
    }

    public UserBuilder setEmailAddress(String emailAddress) {
        this.emailAddress = new EmailAddress(emailAddress);

        return this;
    }

    public UserBuilder setPhoneNumber(PhoneNumber phoneNumber) {
        this.phoneNumber = phoneNumber;

        return this;
    }

    public UserBuilder setPhoneNumber(String phoneNumber) {
        this.phoneNumber = new PhoneNumber(phoneNumber);

        return this;
    }

    public User buildUser() {
        if (userId == null) {
            throw new IllegalStateException(ERROR_MSG_NULL_USER_ID);
        }

        if (userRole == null) {
            throw new IllegalStateException(ERROR_MSG_NULL_USER_ROLE);
        }

        if (firstName == null) {
            throw new IllegalStateException(ERROR_MSG_NULL_FIRST_NAME);
        }

        if (middleName == null) {
            throw new IllegalStateException(ERROR_MSG_NULL_MIDDLE_NAME);
        }

        if (lastName == null) {
            throw new IllegalStateException(ERROR_MSG_NULL_LAST_NAME);
        }

        if (emailAddress == null) {
            throw new IllegalStateException(ERROR_MSG_NULL_EMAIL_ADDRESS);
        }

        if (phoneNumber == null) {
            throw new IllegalStateException(ERROR_MSG_NULL_USER_PHONE_NUMBER);
        }

        User newUser = new User();
        newUser.setUserId(userId.getUserId());
        newUser.setUserRole(userRole);
        newUser.setFirstName(firstName);
        newUser.setMiddleName(middleName);
        newUser.setLastName(lastName);
        newUser.setEmailAddress(emailAddress.getEmailAddress());
        newUser.setPhoneNumber(phoneNumber.getPhoneNumber());

        return newUser;
    }

}
