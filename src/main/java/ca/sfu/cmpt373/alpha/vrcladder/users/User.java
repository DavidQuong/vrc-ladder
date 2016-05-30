package ca.sfu.cmpt373.alpha.vrcladder.users;

import ca.sfu.cmpt373.alpha.vrcladder.persistence.PersistenceConstants;
import ca.sfu.cmpt373.alpha.vrcladder.users.authorization.UserAction;
import ca.sfu.cmpt373.alpha.vrcladder.users.authorization.UserRole;
import ca.sfu.cmpt373.alpha.vrcladder.users.personal.EmailAddress;
import ca.sfu.cmpt373.alpha.vrcladder.users.personal.PhoneNumber;
import ca.sfu.cmpt373.alpha.vrcladder.users.personal.UserId;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;

// TODO - Add character limit to persistent columns of type String.
@Entity
@Table(name = PersistenceConstants.TABLE_USER)
public class User {

    private static final String DISPLAY_NAME_INITIAL_DOT = ". ";

    private UserId userId;
    private UserRole userRole;
    private String firstName;
    private String middleName;
    private String lastName;
    private EmailAddress emailAddress;
    private PhoneNumber phoneNumber;

    public User() {
        // Required by Hibernate
    }

    public User(UserId userId, UserRole userRole, String firstName, String middleName, String lastName,
        EmailAddress emailAddress, PhoneNumber phoneNumber) {
        this.userId = userId;
        this.userRole = userRole;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
    }

    public User(String userId, UserRole userRole, String firstName, String lastName, String emailAddress,
        String phoneNumber) {
        this.userId = new UserId(userId);
        this.userRole = userRole;
        this.firstName = firstName;
        this.middleName = null;
        this.lastName = lastName;
        this.emailAddress = new EmailAddress(emailAddress);
        this.phoneNumber = new PhoneNumber(phoneNumber);
    }

    public User(String userId, UserRole userRole, String firstName, String middleName, String lastName,
        String emailAddress, String phoneNumber) {
        this.userId = new UserId(userId);
        this.userRole = userRole;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.emailAddress = new EmailAddress(emailAddress);
        this.phoneNumber = new PhoneNumber(phoneNumber);
    }

    @Id
    @Column(name = PersistenceConstants.COLUMN_ID)
    public String getUserId() {
        return userId.getUserId();
    }

    public void setUserId(String newUserId) {
        userId = new UserId(newUserId);
    }

    @Enumerated(EnumType.STRING)
    @Column(name = PersistenceConstants.COLUMN_USER_ROLE, nullable = false)
    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole newUserRole) {
        userRole = newUserRole;
    }

    @Column(name = PersistenceConstants.COLUMN_FIRST_NAME, nullable = false)
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String name) {
        firstName = name;
    }

    public boolean hasMiddleName() {
        return (middleName != null);
    }

    @Column(name = PersistenceConstants.COLUMN_MIDDLE_NAME)
    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String name) {
        middleName = name;
    }

    @Column(name = PersistenceConstants.COLUMN_LAST_NAME, nullable = false)
    public String getLastName() {
        return lastName;
    }

    public void setLastName(String name) {
        lastName = name;
    }

    @Transient
    public String getDisplayName() {
        String constructedName = firstName + " ";
        if (middleName != null && !middleName.isEmpty()) {
            char middleInitial = middleName.charAt(0);
            constructedName = constructedName + middleInitial + DISPLAY_NAME_INITIAL_DOT;
        }
        constructedName = constructedName + lastName;

        return constructedName;
    }

    @Column(name = PersistenceConstants.COLUMN_EMAIL_ADDRESS, nullable = false)
    public String getEmailAddress() {
        return emailAddress.toString();
    }

    public void setEmailAddress(String email) {
        emailAddress = new EmailAddress(email);
    }

    @Column(name = PersistenceConstants.COLUMN_PHONE_NUMBER, nullable = false)
    public String getPhoneNumber() {
        return phoneNumber.toString();
    }

    public void setPhoneNumber(String phoneNum) {
        phoneNumber = new PhoneNumber(phoneNum);
    }

    public boolean isPermittedToPerform(UserAction action) {
        return userRole.hasAuthorizationToPerform(action);
    }

}
