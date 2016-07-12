package ca.sfu.cmpt373.alpha.vrcladder.notifications.logic;

public enum NotificationType {
    // Account Notification Settings.
    //-------------------------------
    ACCOUNT_ACTIVATED("registrationconfirmed"),
    ACCOUNT_NEEDS_ACTIVATION("activationrequired"),
    PASSWORD_UPDATED("passwordupdated"),
    PASSWORD_RESET("passwordreset"),
    FAILED_LOGIN("loginfailed"),

    // Games Notification Types.
    //-----------------------------
    GAME_SCORES_REPORTED_WRONG("gamescoresreportedwrong"),
    GAME_SCHEDULED_REMINDER("gamescheduledreminder"),
    GAME_SCORES_ENTERED("gamescorereported"),
    GAME_SCORES_UPDATED("gamescoreupdated"),
    GAME_SCHEDULED("gamescheduled"),
    GAME_REPORTED_THREE_TEAMS("threeteams"),
    GAME_REPORTED_FOUR_TEAMS("fourteams"),

    // Teams Notification Settings.
    //-----------------------------
    TEAM_TIME_UPDATED("teamtimeupdated"),
    ADDED_TO_TEAM("teamcreated");

    private String template;

    NotificationType(String template) {
        this.template = template;
    }

    public String getTemplate(){
        return template;
    }
}
