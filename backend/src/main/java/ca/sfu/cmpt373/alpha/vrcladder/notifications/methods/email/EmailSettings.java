package ca.sfu.cmpt373.alpha.vrcladder.notifications.methods.email;

/**
 * This class holds all the constants that is related to email notifications.
 */
public class EmailSettings {
    // TODO: must change to VRC email account info.
    static final String SERVER   = "mail.espuresystems.com";
    static final int SERVER_PORT = 25;
    static final String USERNAME = "hassan@espuresystems.com";
    static final String PASSWORD = "M49D4s5";
    public static final String EMAILS_FORMAT = "txt"; // Can be "txt" or "html".

    static final String SUBJECT_ADDED_TO_TEAM              = "You have been added to a team";
    static final String SUBJECT_TEAM_TIME_UPDATED          = "Your team preferred time has been updated";
    static final String SUBJECT_GAME_SCHEDULED             = "You have a new scheduled game";
    static final String SUBJECT_GAME_SCHEDULED_REMINDER    = "REMINDER: You have a scheduled game";
    static final String SUBJECT_GAME_SCORES_ENTERED        = "A new score has been entered for your game";
    static final String SUBJECT_ACCOUNT_ACTIVATED          = "Welcome to VRC";
    static final String SUBJECT_PASSWORD_UPDATED           = "Your password has been updated successfully";
    static final String SUBJECT_PASSWORD_RESET             = "Password reset";
    static final String SUBJECT_FAILED_LOGIN               = "You have a failed login attempt";
    static final String SUBJECT_GAME_SCORES_REPORTED_WRONG = "Game scores has been reported to be wrongful";
    static final String SUBJECT_GAME_SCORES_UPDATED        = "Game scores has been updated";
    static final String SUBJECT_GENERAL_EMAIL              = "Notification from VRC";

}
