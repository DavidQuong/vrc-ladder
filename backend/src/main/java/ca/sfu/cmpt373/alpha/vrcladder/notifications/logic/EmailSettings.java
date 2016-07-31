package ca.sfu.cmpt373.alpha.vrcladder.notifications.logic;

/**
 * This class holds all the constants that is related to email notifications.
 */
public class EmailSettings {

    // Server Connection Parameters
    // TODO: must change to VRC email account info.
    static final String SERVER   = "mail.espuresystems.com";
    static final String SERVER_PORT = "26";
    static final String USERNAME = "no-reply@vrc.espuresystems.com";
    static final String PASSWORD = "gzc4tZj+98";
    static final String FROM_NAME = "VRC";
    public static final String WEBSITE_LINK = "http://www.vrc.ca";
    public static final String EMAILS_FORMAT = "html"; // Can be "txt" or "html".

    // Template Settings.
    //-------------------
    public static final String TEMPLATE_PATH_ACCOUNT = "src/main/resources/notifications/templates/account/email_";
    public static final String TEMPLATE_PATH_GAME = "src/main/resources/notifications/templates/game/email_";
    public static final String TEMPLATE_PATH_TEAM = "src/main/resources/notifications/templates/team/email_";


    // Email Subjects.
    //----------------
    static final String SUBJECT_GAME_SCORES_REPORTED_WRONG = "Game scores has been reported to be wrongful";
    static final String SUBJECT_PASSWORD_UPDATED           = "Your password has been updated successfully";
    static final String SUBJECT_GAME_SCORES_ENTERED        = "A new score has been entered for your game";
    static final String SUBJECT_TEAM_TIME_UPDATED          = "Your team preferred time has been updated";
    static final String SUBJECT_GAME_SCHEDULED_REMINDER    = "REMINDER: You have a scheduled game";
    static final String SUBJECT_FAILED_LOGIN               = "You have a failed login attempt";
    static final String SUBJECT_ADDED_TO_TEAM              = "You have been added to a team";
    static final String SUBJECT_GAME_SCHEDULED             = "You have a new scheduled game";
    static final String SUBJECT_GAME_SCORES_UPDATED        = "Game scores has been updated";
    static final String SUBJECT_ACCOUNT_NEEDS_ACTIVATION   = "VRC Account Confirmation";
    static final String SUBJECT_GENERAL_EMAIL              = "Notification from VRC";
    static final String SUBJECT_ACCOUNT_ACTIVATED          = "Welcome to VRC";
    static final String SUBJECT_PASSWORD_RESET             = "Password reset";

}
