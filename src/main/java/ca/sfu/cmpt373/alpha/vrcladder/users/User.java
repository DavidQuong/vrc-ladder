package ca.sfu.cmpt373.alpha.vrcladder.users;

import ca.sfu.cmpt373.alpha.vrcladder.users.authorization.UserAction;
import ca.sfu.cmpt373.alpha.vrcladder.users.authorization.UserRole;
import ca.sfu.cmpt373.alpha.vrcladder.users.personal.EmailAddress;
import ca.sfu.cmpt373.alpha.vrcladder.users.personal.PhoneNumber;
import ca.sfu.cmpt373.alpha.vrcladder.users.personal.UserId;
import ca.sfu.cmpt373.alpha.vrcladder.users.personal.UserName;

// TODO - Annotate fields for data persistence
public class User {

    private UserId userId;
    private UserRole userRole;
    private UserName userName;
    private EmailAddress emailAddress;
    private PhoneNumber phoneNumber;

    public User(UserId userId, UserRole userRole, UserName userName, EmailAddress emailAddress,
        PhoneNumber phoneNumber) {
        this.userId = userId;
        this.userRole = userRole;
        this.userName = userName;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
    }

    public UserId getUserId() {
        return userId;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public UserName getUserName() {
        return userName;
    }

    public EmailAddress getEmailAddress() {
        return emailAddress;
    }

    public PhoneNumber getPhoneNumber() {
        return phoneNumber;
    }

    public boolean isPermittedToPerform(UserAction action) {
        return userRole.hasAuthorizationToPerform(action);
    }

    @Override
    public String toString(){
        return  "userID = " + userId.getUserId() + ", "
                + userName.getDisplayName() + ", "
                + userRole + ", "
                + emailAddress.toString() + ", "
                + phoneNumber.toString();
    }

}
