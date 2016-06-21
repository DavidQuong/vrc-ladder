package ca.sfu.cmpt373.alpha.vrcladder.users;

import ca.sfu.cmpt373.alpha.vrcladder.persistence.PersistenceConstants;
import ca.sfu.cmpt373.alpha.vrcladder.users.authorization.UserAction;
import ca.sfu.cmpt373.alpha.vrcladder.users.authorization.UserRole;
import ca.sfu.cmpt373.alpha.vrcladder.users.personal.EmailAddress;
import ca.sfu.cmpt373.alpha.vrcladder.users.personal.PhoneNumber;
import ca.sfu.cmpt373.alpha.vrcladder.users.personal.UserId;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Table;

@Entity
@Table(name = PersistenceConstants.TABLE_USER)
public class User {

    private static final String DISPLAY_NAME_INITIAL_DOT = ". ";

    @EmbeddedId
    private UserId userId;

    @Enumerated(EnumType.STRING)
    @Column(name = PersistenceConstants.COLUMN_USER_ROLE, nullable = false)
    private UserRole userRole;

    @Column(name = PersistenceConstants.COLUMN_FIRST_NAME, nullable = false)
    private String firstName;

    @Column(name = PersistenceConstants.COLUMN_MIDDLE_NAME, nullable = false)
    private String middleName;

    @Column(name = PersistenceConstants.COLUMN_LAST_NAME, nullable = false)
    private String lastName;

    @Embedded
    @Column(name = PersistenceConstants.COLUMN_EMAIL_ADDRESS, nullable = false)
    private EmailAddress emailAddress;

    @Column(name = PersistenceConstants.COLUMN_PHONE_NUMBER, nullable = false)
    private PhoneNumber phoneNumber;

    private User() {
        // Required by Hibernate.
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

    public UserId getUserId() {
        return userId;
    }

    public UserRole getUserRole() {
        return userRole;
    }

    public void setUserRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public boolean hasMiddleName() {
        return (!middleName.isEmpty());
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getDisplayName() {
        String constructedName = firstName + " ";
        if (hasMiddleName()) {
            char middleInitial = middleName.charAt(0);
            constructedName = constructedName + middleInitial + DISPLAY_NAME_INITIAL_DOT;
        }
        constructedName = constructedName + lastName;

        return constructedName;
    }

    public EmailAddress getEmailAddress() {
        return emailAddress;
    }

    public void setEmailAddress(EmailAddress emailAddress) {
        this.emailAddress = emailAddress;
    }

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