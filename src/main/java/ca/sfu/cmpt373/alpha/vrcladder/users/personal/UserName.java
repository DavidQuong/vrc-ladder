package ca.sfu.cmpt373.alpha.vrcladder.users.personal;

// TODO - Implement Comparable interface
public class UserName {

    private static final String INITIAL_DOT = ". ";

    private String firstName;
    private String middleName;
    private String lastName;
    private String displayName;

    public UserName(String firstName, String middleName, String lastName) {
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
        this.displayName = constructDisplayName(firstName, middleName, lastName);
    }

    public String getFirstName() {
        return firstName;
    }

    public boolean hasMiddleName() {
        return (middleName != null);
    }

    public String getMiddleName() {
        return middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getDisplayName() {
        return displayName;
    }

    private String constructDisplayName(String firstName, String middleName, String lastName) {
        String constructedName = firstName + " ";
        if (middleName != null) {
            char middleInitial = middleName.charAt(0);
            constructedName = constructedName + middleInitial + INITIAL_DOT;
        }
        constructedName = constructedName + lastName;

        return constructedName;
    }

}
