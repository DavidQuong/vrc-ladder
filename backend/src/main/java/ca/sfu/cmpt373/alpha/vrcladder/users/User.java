package ca.sfu.cmpt373.alpha.vrcladder.users;

import ca.sfu.cmpt373.alpha.vrcladder.persistence.PersistenceConstants;
import ca.sfu.cmpt373.alpha.vrcladder.users.authorization.UserAction;
import ca.sfu.cmpt373.alpha.vrcladder.users.authorization.UserRole;
import ca.sfu.cmpt373.alpha.vrcladder.users.personal.EmailAddress;
import ca.sfu.cmpt373.alpha.vrcladder.users.personal.PhoneNumber;
import ca.sfu.cmpt373.alpha.vrcladder.users.personal.UserId;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
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
        // Required by Hibernate.
    }

    public User(UserId userId, UserRole userRole, String firstName, String middleName, String lastName, EmailAddress emailAddress, PhoneNumber phoneNumber) {
        this.userId = userId;
        this.userRole = userRole;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.emailAddress = emailAddress;
        this.phoneNumber = phoneNumber;
    }

    @EmbeddedId
    public UserId getUserId() {
        return userId;
    }

    private void setUserId(UserId newUserId) {
        userId = newUserId;
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
        return (middleName.isEmpty());
    }

    @Column(name = PersistenceConstants.COLUMN_MIDDLE_NAME, nullable = false)
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
    @JsonIgnore
    public String getDisplayName() {
        String constructedName = firstName + " ";
        if (middleName != null && !middleName.isEmpty()) {
            char middleInitial = middleName.charAt(0);
            constructedName = constructedName + middleInitial + DISPLAY_NAME_INITIAL_DOT;
        }
        constructedName = constructedName + lastName;

        return constructedName;
    }

    @Embedded
    @Column(name = PersistenceConstants.COLUMN_EMAIL_ADDRESS, nullable = false)
    public EmailAddress getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(EmailAddress emailAddress) {
        this.emailAddress = emailAddress;
    }

    @Column(name = PersistenceConstants.COLUMN_PHONE_NUMBER, nullable = false)
    public PhoneNumber getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(PhoneNumber phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean isPermittedToPerform(UserAction action) {
        return userRole.hasAuthorizationToPerform(action);
    }

    @Override
    public boolean equals(Object otherObj) {
        if (this == otherObj) {
            return true;
        }

        if (otherObj == null || getClass() != otherObj.getClass()) {
            return false;
        }

        User otherUser = (User) otherObj;

        return userId.equals(otherUser.userId);
    }

    @Override
    public int hashCode() {
        return userId.hashCode();
    }

}