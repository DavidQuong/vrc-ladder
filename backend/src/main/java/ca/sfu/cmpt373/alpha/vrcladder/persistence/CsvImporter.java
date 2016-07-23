package ca.sfu.cmpt373.alpha.vrcladder.persistence;

import ca.sfu.cmpt373.alpha.vrcladder.exceptions.ValidationException;
import ca.sfu.cmpt373.alpha.vrcladder.teams.TeamManager;
import ca.sfu.cmpt373.alpha.vrcladder.users.User;
import ca.sfu.cmpt373.alpha.vrcladder.users.UserBuilder;
import ca.sfu.cmpt373.alpha.vrcladder.users.UserManager;
import ca.sfu.cmpt373.alpha.vrcladder.users.authentication.SecurityManager;
import ca.sfu.cmpt373.alpha.vrcladder.users.authorization.UserRole;
import ca.sfu.cmpt373.alpha.vrcladder.users.personal.UserId;
import org.hibernate.exception.ConstraintViolationException;

/**
 * Given a String for a csv file in the form (Last Name,First Name,Player #,Status,,Last Name,First Name,Player #,Status,Ranking)
 * This class will add all the data to the database. Note: there is currently little error handling done.
 */
public class CsvImporter {
    private static final String DEFAULT_EMAIL = "test%s@test.com";
    private static final String DEFAULT_PASSWORD = "123";
    private static final String DEFAULT_PHONE = "1111111111";
    private static final String DEFAULT_MIDDLE_NAME = "";

    private static final String NEW_LINE = "\n";
    private static final String COMMA = ",";
    private static final String ERROR_ADDING_TEAM = "There was an error adding team on row";

    private UserManager userManager;
    private TeamManager teamManager;
    private SecurityManager securityManager;

    public CsvImporter(UserManager userManager, TeamManager teamManager, SecurityManager securityManager) {
        this.userManager = userManager;
        this.teamManager = teamManager;
        this.securityManager = securityManager;
    }

    /**
     * Given a csv file String, this method will insert its data to the database.
     * Notes: the position column is currently being ignored to prevent gaps in the database (ex: add teams at 1, 2, 4)
     * Last names of players are also not being enforced because vrc's data doesn't supply last names for everyone
     * Middle names, email addresses, passwords and phone numbers are all not provided (or known) by the vrc, so these are
     * initialized to dummy default values
     * @return a String that contains information about all the errors that occurred when adding data to the database
     */
    public String insertData(String csvFile) {
        String errorLog = "";
        int emailCount = 0;
        String[] rows = csvFile.split(NEW_LINE);
        //skip the first row because it just contains headers with column names
        for (int i = 1; i < rows.length; i++) {
            String row = rows[i];
            String[] columns = row.split(COMMA);

            String firstPlayerLastName = columns[0];
            String firstPlayerFirstName = columns[1];
            String firstPlayerId = columns[2];

            String statusPlayer1 = columns[3];

            String secondPlayerLastName = columns[5];
            String secondPlayerFirstName = columns[6];
            String secondPlayerId = columns[7];

            String statusPlayer2 = columns[8];

            String position = columns[9];
            if (
//                    !position.isEmpty() we are currently ignoring position, and just adding teams in the order they are provided
//                    !firstPlayerLastName.isEmpty() && we currently don't care about last names
                    !firstPlayerFirstName.isEmpty() &&
                    !firstPlayerId.isEmpty() &&
                    statusPlayer1.isEmpty() && //note that this is supposed to be empty!
//                    !secondPlayerLastName.isEmpty() && we currently don't care about last names
                    !secondPlayerFirstName.isEmpty() &&
                    !secondPlayerId.isEmpty() &&
                    statusPlayer2.isEmpty()) { //also should be empty
                try {
                    User user1 = addPlayer(userManager, firstPlayerFirstName, firstPlayerLastName, firstPlayerId, emailCount);
                    emailCount++;
                    User user2 = addPlayer(userManager, secondPlayerFirstName, secondPlayerLastName, secondPlayerId, emailCount);
                    emailCount++;
                    teamManager.create(user1, user2);
                } catch (ConstraintViolationException | ValidationException e) {
                    //currently, we're not checking the form of the data. Ex: userId is only numbers
                    //so, if invalid data is passed in, an error will occur
                    errorLog += ERROR_ADDING_TEAM + i + NEW_LINE;
                }
            } else {
                errorLog += ERROR_ADDING_TEAM + i + NEW_LINE;
            }
        }
        return errorLog;
    }

    private User addPlayer(UserManager userManager, String firstName, String lastName, String id, int emailCount) {
        User user;
        try {
            //note: hardcoded values are values not currently supplied in the csv file
            user = userManager.create(new UserBuilder()
                    .setFirstName(firstName)
                    .setLastName(lastName)
                    .setUserId(id)
                    .setMiddleName(DEFAULT_MIDDLE_NAME)
                    .setUserRole(UserRole.PLAYER)
                    .setEmailAddress(String.format(DEFAULT_EMAIL, emailCount))
                    .setPhoneNumber(DEFAULT_PHONE)
                    .setPassword(securityManager.hashPassword(DEFAULT_PASSWORD))
                    .buildUser());
        } catch (ConstraintViolationException e) {
            //if there is a duplicate user created, just return the existing one from the database
            return userManager.getById(new UserId(id));
        }
        return user;
    }
}

