package ca.sfu.cmpt373.alpha.vrcladder.users;

import ca.sfu.cmpt373.alpha.vrcladder.exceptions.ValidationException;
import ca.sfu.cmpt373.alpha.vrcladder.persistence.PersistenceConstants;
import ca.sfu.cmpt373.alpha.vrcladder.users.authentication.Password;
import ca.sfu.cmpt373.alpha.vrcladder.users.authorization.UserAction;
import ca.sfu.cmpt373.alpha.vrcladder.users.authorization.UserRole;
import ca.sfu.cmpt373.alpha.vrcladder.users.personal.EmailAddress;
import ca.sfu.cmpt373.alpha.vrcladder.users.personal.PhoneNumber;
import ca.sfu.cmpt373.alpha.vrcladder.users.personal.UserId;
import org.apache.shiro.authz.AuthorizationException;

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
    private static final String ERROR_FORMAT_INVALID_AUTHORIZATION = "This user is not authorized to %s.";
    private static final int RESET_ATTEMPTS = 0;

    @EmbeddedId
    private UserId userId;

    @Enumerated(EnumType.STRING)
    @Column(name = PersistenceConstants.COLUMN_USER_ROLE, nullable = false)
    private UserRole userRole;

    @Column(name = PersistenceConstants.COLUMN_FIRST_NAME, nullable = false, length = PersistenceConstants.MAX_NAME_LENGTH)
    private String firstName;

    @Column(name = PersistenceConstants.COLUMN_MIDDLE_NAME, nullable = false, length = PersistenceConstants.MAX_NAME_LENGTH)
    private String middleName;

    @Column(name = PersistenceConstants.COLUMN_LAST_NAME, nullable = false, length = PersistenceConstants.MAX_NAME_LENGTH)
    private String lastName;

    @Column(name = PersistenceConstants.COLUMN_FAILED_ATTEMPTS, nullable = false)
    private int attempts;

    @Embedded
    private EmailAddress emailAddress;

    @Embedded
    private PhoneNumber phoneNumber;

    @Embedded
    private Password password;

    private User() {
        // Required by Hibernate.
    }

    public User(UserId userId, UserRole userRole, String firstName, String middleName, String lastName,
        EmailAddress emailAddress, PhoneNumber phoneNumber, Password password, int attempts) {
        this.userId = userId;
        setUserRole(userRole);
        setFirstName(firstName);
        setMiddleName(middleName);
        setLastName(lastName);
        setEmailAddress(emailAddress);
        setPhoneNumber(phoneNumber);
        setPassword(password);
        setAttempts(attempts);
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
        verifyName(firstName);
        this.firstName = firstName;
    }

    public boolean hasMiddleName() {
        return (!middleName.isEmpty());
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        if (middleName.length() > PersistenceConstants.MAX_NAME_LENGTH) {
            throw new ValidationException();
        }
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        verifyName(lastName);
        this.lastName = lastName;
    }


    private void verifyName(String word){
        if(word.isEmpty()){
            throw new ValidationException();
        }
        if (word.length() > PersistenceConstants.MAX_NAME_LENGTH) {
            throw new ValidationException();
        }
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

    public Password getPassword() {
        return password;
    }

    public void setPassword(Password password) {
        this.password = password;
    }

    public void checkPermission(UserAction action) {
        if (!userRole.hasAuthorizationToPerform(action)) {
            String errorMsg = String.format(ERROR_FORMAT_INVALID_AUTHORIZATION, action.name());
            throw new AuthorizationException(errorMsg);
        }
    }

    public int getAttempts(){
        return this.attempts;
    }

    public void setAttempts(int attempts){
        this.attempts = attempts;
    }

    public void resetApttempts(){
        this.attempts = RESET_ATTEMPTS;
    }

    public void incrementAttempts(){
        this.attempts++;
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