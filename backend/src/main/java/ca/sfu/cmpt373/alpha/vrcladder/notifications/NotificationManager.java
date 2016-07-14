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
    private static final TemplateManager template = new TemplateManager();
    private static final String GAME_REPORTED_THREE_TEAMS = "threeteams";
    private static final String GAME_REPORTED_FOUR_TEAMS  = "fourteams";
    private static final int FIRST_PLAYER = 1;
    private static final int SECOND_PLAYER = 2;
    private Email email;

    public NotificationManager() {
        email = new Email();
    }

    public void notifyUser(User user, Team team, NotificationType type) {
        Map<String, String> values = new HashMap<>();
        EmailAddress receiver = buildUserInfoAndGetEmail(user, values);
        String currentTemplate = type.getTemplate();
        String path = EmailSettings.TEMPLATE_PATH_TEAM + currentTemplate + "." + EmailSettings.EMAILS_FORMAT;
        setTeamValues(team, values);

        try {
            String messageContent = template.getContents(path, values);
            email.sendEmail(receiver, messageContent, type);
        } catch (IOException e) {
            throw new TemplateNotFoundException();
        }
    }

    public void notifyUser(User user, NotificationType type) {
        Map<String, String> values = new HashMap<>();
        EmailAddress receiver = buildUserInfoAndGetEmail(user, values);
        String currentTemplate = type.getTemplate();
        String path = EmailSettings.TEMPLATE_PATH_ACCOUNT + currentTemplate + "." + EmailSettings.EMAILS_FORMAT;

        try {
            String messageContent = template.getContents(path, values);
            email.sendEmail(receiver, messageContent, type);
        } catch (IOException e) {
            throw new TemplateNotFoundException();
        }
    }

    public void notifyUser(User user, NotificationType type, String token) {
        Map<String, String> values = new HashMap<>();
        EmailAddress receiver = buildUserInfoAndGetEmail(user, values);
        String currentTemplate = type.getTemplate();
        String path = EmailSettings.TEMPLATE_PATH_ACCOUNT + currentTemplate + "." + EmailSettings.EMAILS_FORMAT;
        setTokenValues(token, values);

        try {
            String messageContent = template.getContents(path, values);
            email.sendEmail(receiver, messageContent, type);
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
        String path = EmailSettings.TEMPLATE_PATH_GAME + currentTemplate + "." + EmailSettings.EMAILS_FORMAT;
        setGameValues(teams, receivers, names, values);

        try {
            for (int counter = 0; counter < names.size(); counter++) {
                values.replace("#fullname", names.get(counter));
                EmailAddress receiver = receivers.get(counter);
                String messageContent = template.getContents(path, values);
                email.sendEmail(receiver, messageContent, type);
            }

        } catch (IOException e) {
            throw new TemplateNotFoundException();
        }
    }

    private EmailAddress buildUserInfoAndGetEmail(User user, Map<String, String> values){
        values.put("#fullname", user.getDisplayName());
        values.put("#userid", user.getUserId().toString());
        return user.getEmailAddress();
    }

    private String getCurrentTemplate(MatchGroup group, String template){
        if (group.getTeamCount() == MatchGroup.MIN_NUM_TEAMS) {
            return (template + GAME_REPORTED_THREE_TEAMS);
        }
        return (template + GAME_REPORTED_FOUR_TEAMS);
    }

    private static void setTokenValues(String token, Map<String, String> values){
        values.put("#token", token);
        values.put("#sitelink", EmailSettings.WEBSITE_LINK);
    }

    private static void setTeamValues(Team team, Map<String, String> values) {
        values.put("#preferrdtime", team.getAttendanceCard().getPreferredPlayTime().getDisplayTime());
        values.put("#ladderposition", team.getLadderPosition().getValue().toString());
        values.put("#firstteammember", team.getFirstPlayer().getDisplayName());
        values.put("#secondteammember", team.getSecondPlayer().getDisplayName());
    }

    private static void setGameValues(List<Team> teams, List<EmailAddress> receivers, List<String> names, Map<String, String> values) {
        values.put("#fullname", "");
        for (int counter = 0; counter < teams.size(); counter++) {
            int currentTeam = counter + 1;
            User firstPlayer = teams.get(counter).getFirstPlayer();
            User secondPlayer = teams.get(counter).getSecondPlayer();

            receivers.add(firstPlayer.getEmailAddress());
            receivers.add(secondPlayer.getEmailAddress());
            names.add(firstPlayer.getDisplayName());
            names.add(secondPlayer.getDisplayName());
            values.put("#team" + currentTeam + "player" + FIRST_PLAYER, firstPlayer.getDisplayName());
            values.put("#team" + currentTeam + "player" + SECOND_PLAYER, secondPlayer.getDisplayName());
        }
    }
}
