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
     * @return a String that contains information about all the errors that occured when adding data to the database
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
//                    !isEmpty(firstPlayerLastName) && we currently don't care about last names
                    !isEmpty(firstPlayerFirstName) &&
                    !isEmpty(firstPlayerId) &&
                    isEmpty(statusPlayer1) && //note that this is supposed to be empty!
//                    !isEmpty(secondPlayerLastName) && we currently don't care about last names
                    !isEmpty(secondPlayerFirstName) &&
                    !isEmpty(secondPlayerId) &&
                    isEmpty(statusPlayer2) && //also should be empty
                    !isEmpty(position)) {
                try {
                    User user1 = addPlayer(userManager, firstPlayerFirstName, firstPlayerLastName, firstPlayerId, emailCount);
                    emailCount++;
                    User user2 = addPlayer(userManager, secondPlayerFirstName, secondPlayerLastName, secondPlayerId, emailCount);
                    emailCount++;
                    teamManager.create(user1, user2);
                } catch (ConstraintViolationException | ValidationException e) {
                    //currently, we're not checking the form of the data. Ex: userId is only numbers
                    //so, if invalid data is passed in, an error will occur
                    errorLog += ERROR_ADDING_TEAM + (i + 1) + NEW_LINE;
                }
            } else {
                errorLog += ERROR_ADDING_TEAM + (i + 1) + NEW_LINE;
            }
        }
        return errorLog;
    }

    private boolean isEmpty(String string) {
        return string == null || string.equals("");
    }

    private User addPlayer(UserManager userManager, String firstName, String lastName, String id, int emailCount) {
        User user;
        try {
            //note: hardcoded values are values not currently supplied in the csv file
            user = userManager.create(new UserBuilder()
                    .setFirstName(firstName)
                    .setLastName(lastName)
                    .setUserId(id)
                    .setMiddleName("")
                    .setUserRole(UserRole.PLAYER)
                    .setEmailAddress("test" + emailCount + "@test.com")
                    .setPhoneNumber("1111111111")
                    .setPassword(securityManager.hashPassword("123"))
                    .buildUser());
        } catch (ConstraintViolationException e) {
            //if there is a duplicate user created, just return the existing one from the database
            return userManager.getById(new UserId(id));
        }
        return user;
    }
}

