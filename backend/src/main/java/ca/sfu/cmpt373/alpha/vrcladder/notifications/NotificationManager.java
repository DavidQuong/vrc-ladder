package ca.sfu.cmpt373.alpha.vrcladder.notifications;


import ca.sfu.cmpt373.alpha.vrcladder.exceptions.TemplateNotFoundException;
import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.MatchGroup;
import ca.sfu.cmpt373.alpha.vrcladder.notifications.logic.NotificationType;
import ca.sfu.cmpt373.alpha.vrcladder.notifications.logic.Email;
import ca.sfu.cmpt373.alpha.vrcladder.notifications.logic.EmailSettings;
import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
import ca.sfu.cmpt373.alpha.vrcladder.util.TemplateManager;
import ca.sfu.cmpt373.alpha.vrcladder.users.User;
import ca.sfu.cmpt373.alpha.vrcladder.users.personal.EmailAddress;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * This class handles all notifications sent to users. It uses
 * TemplateManager class to open HTML and TEXT tempaltes of this
 * class. Templates are located in the resources folder.
 */
public class NotificationManager {

    private static final String GAME_REPORTED_THREE_TEAMS = "threeteams";
    private static final String GAME_REPORTED_FOUR_TEAMS  = "fourteams";
    private static final String RECEIVER_NAME_TAG         = "#fullname";
    private static final String RECEIVER_ID_TAG           = "#userid";
    private static final String TOKEN_TAG                 = "#token";
    private static final String WEBSITE_LINK_TAG          = "#sitelink";
    private static final String TEAM_TIME_TAG             = "#preferrdtime";
    private static final String LADDER_POSITION_TAG       = "#ladderposition";
    private static final String FIRST_PLAYER_TAG          = "#firstteammember";
    private static final String SECOND_PLAYER_TAG         = "#secondteammember";
    private static final String SCORE_TEAM_LEFT_TAG       = "#team";
    private static final String SCORE_TEAM_CLOSE_TAG      = "player";
    private static final String TEMPLATE_PDF              = "pdf";
    private static final String EXTENSION_SEPARATOR       = ".";
    private static final int FIRST_PLAYER = 1;
    private static final int SECOND_PLAYER = 2;
    private final TemplateManager template;
    private Email email;

    public NotificationManager() {
        template = new TemplateManager();
        email = new Email();
    }

    public void notifyUser(Team team, NotificationType type) {
        Map<String, String> values = new HashMap<>();
        List<EmailAddress> receivers = new ArrayList<>();
        List<String> names = new ArrayList<>();
        String currentTemplate = type.getTemplate();
        String path = EmailSettings.TEMPLATE_PATH_TEAM + currentTemplate + EXTENSION_SEPARATOR + EmailSettings.EMAILS_FORMAT;
        setTeamValues(team, receivers, names, values);

        try {
            for(int counter = 0; counter < names.size(); counter++){
                values.replace(RECEIVER_NAME_TAG, names.get(counter));
                String messageContent = template.getContents(path, values);
                email.sendEmail(receivers.get(counter), messageContent, type);
            }
        } catch (IOException e) {
            throw new TemplateNotFoundException();
        }
    }

    public void notifyUser(User user, NotificationType type) {
        Map<String, String> values = buildUserInfo(user);
        EmailAddress userEmail = user.getEmailAddress();
        String currentTemplate = type.getTemplate();
        String path = EmailSettings.TEMPLATE_PATH_ACCOUNT + currentTemplate + EXTENSION_SEPARATOR + EmailSettings.EMAILS_FORMAT;

        try {
            String messageContent = template.getContents(path, values);
            email.sendEmail(userEmail, messageContent, type);
        } catch (IOException e) {
            throw new TemplateNotFoundException();
        }
    }

    public void notifyUser(User user, NotificationType type, String token) {
        Map<String, String> values = buildUserInfo(user);
        EmailAddress userEmail = user.getEmailAddress();
        String currentTemplate = type.getTemplate();
        String path = EmailSettings.TEMPLATE_PATH_ACCOUNT + currentTemplate + EXTENSION_SEPARATOR + EmailSettings.EMAILS_FORMAT;
        setTokenValues(token, values);

        try {
            String messageContent = template.getContents(path, values);
            email.sendEmail(userEmail, messageContent, type);
        } catch (IOException e) {
            throw new TemplateNotFoundException();
        }
    }

    public void notifyUser(MatchGroup group, NotificationType type) {
        Map<String, String> values = new HashMap<>();
        List<EmailAddress> receivers = new ArrayList<>();
        List<String> names = new ArrayList<>();
        List<Team> teams = group.getScoreCard().getRankedTeams();
        String currentTemplate = getCurrentTemplate(group, type.getTemplate());
        String path = EmailSettings.TEMPLATE_PATH_GAME + currentTemplate + EXTENSION_SEPARATOR + EmailSettings.EMAILS_FORMAT;
        setGameValues(teams, receivers, names, values);

        try {
            for (int counter = 0; counter < names.size(); counter++) {
                values.replace(RECEIVER_NAME_TAG, names.get(counter));
                EmailAddress receiver = receivers.get(counter);
                String messageContent = template.getContents(path, values);
                email.sendEmail(receiver, messageContent, type);
            }

        } catch (IOException e) {
            throw new TemplateNotFoundException();
        }
    }

    public void sendPDF(User user, String pdfPath) {
        Map<String, String> values = new HashMap<>();
        EmailAddress receiver = user.getEmailAddress();
        String currentTemplate = TEMPLATE_PDF;
        String path = EmailSettings.TEMPLATE_PATH_PDFS + currentTemplate + EXTENSION_SEPARATOR + EmailSettings.EMAILS_FORMAT;

        try {
            String messageContent = template.getContents(path, values);
            email.sendEmail(receiver, messageContent, currentTemplate, pdfPath);
        } catch (IOException e) {
            throw new TemplateNotFoundException();
        }
    }

    private Map<String, String> buildUserInfo(User user){
        Map<String, String> values = new HashMap<>();
        values.put(RECEIVER_NAME_TAG, user.getDisplayName());
        values.put(RECEIVER_ID_TAG, user.getUserId().toString());
        return values;
    }

    private String getCurrentTemplate(MatchGroup group, String template){
        if (group.getTeamCount() == MatchGroup.MIN_NUM_TEAMS) {
            return (template + GAME_REPORTED_THREE_TEAMS);
        }
        return (template + GAME_REPORTED_FOUR_TEAMS);
    }

    private static void setTokenValues(String token, Map<String, String> values){
        values.put(TOKEN_TAG, token);
        values.put(WEBSITE_LINK_TAG, EmailSettings.WEBSITE_LINK);
    }

    private static void setTeamValues(Team team, List<EmailAddress> receivers, List<String> names, Map<String, String> values) {
        values.put(RECEIVER_NAME_TAG, "");
        values.put(TEAM_TIME_TAG, team.getAttendanceCard().getPreferredPlayTime().getDisplayTime());
        values.put(LADDER_POSITION_TAG, team.getLadderPosition().getValue().toString());

        String firstPlayer = team.getFirstPlayer().getDisplayName();
        String secondPlayer = team.getSecondPlayer().getDisplayName();

        values.put(FIRST_PLAYER_TAG, firstPlayer);
        values.put(SECOND_PLAYER_TAG, secondPlayer);
        names.add(firstPlayer);
        names.add(secondPlayer);

        receivers.add(team.getFirstPlayer().getEmailAddress());
        receivers.add(team.getSecondPlayer().getEmailAddress());
    }

    private static void setGameValues(List<Team> teams, List<EmailAddress> receivers, List<String> names, Map<String, String> values) {
        values.put(RECEIVER_NAME_TAG, "");
        for (int counter = 0; counter < teams.size(); counter++) {
            int currentTeam = counter + 1;
            User firstPlayer = teams.get(counter).getFirstPlayer();
            User secondPlayer = teams.get(counter).getSecondPlayer();

            receivers.add(firstPlayer.getEmailAddress());
            receivers.add(secondPlayer.getEmailAddress());
            names.add(firstPlayer.getDisplayName());
            names.add(secondPlayer.getDisplayName());
            values.put(SCORE_TEAM_LEFT_TAG + currentTeam + SCORE_TEAM_CLOSE_TAG + FIRST_PLAYER, firstPlayer.getDisplayName());
            values.put(SCORE_TEAM_LEFT_TAG + currentTeam + SCORE_TEAM_CLOSE_TAG + SECOND_PLAYER, secondPlayer.getDisplayName());
        }
    }
}
