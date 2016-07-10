package ca.sfu.cmpt373.alpha.vrcladder.notifications;


import ca.sfu.cmpt373.alpha.vrcladder.matchmaking.MatchGroup;
import ca.sfu.cmpt373.alpha.vrcladder.notifications.logic.NotificationType;
import ca.sfu.cmpt373.alpha.vrcladder.notifications.logic.Email;
import ca.sfu.cmpt373.alpha.vrcladder.notifications.logic.EmailSettings;
import ca.sfu.cmpt373.alpha.vrcladder.teams.Team;
import ca.sfu.cmpt373.alpha.vrcladder.users.User;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotificationManager {
    private Email email;
    private static final String GAME_REPORTED_THREE_TEAMS = "threeteams";
    private static final String GAME_REPORTED_FOUR_TEAMS  = "fourteams";
    private static final int FIRST_PLAYER = 1;
    private static final int SECOND_PLAYER = 2;

    public NotificationManager() {
        email = new Email();
    }

    public void notifyUser(User user, Team team, NotificationType type) {
        Map<String, String> values = new HashMap<>();
        String receiver = generalInfoBuilder(user, values);
        String currentTemplate = type.getTemplate();
        setTeamValues(team, values);

        try {
            String messageContent = getMessageContents(EmailSettings.TEMPLATE_PATH_TEAM, currentTemplate, values);
            email.sendEmail(receiver, messageContent, type);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void notifyUser(User user, NotificationType type) {
        Map<String, String> values = new HashMap<>();
        String receiver = generalInfoBuilder(user, values);
        String currentTemplate = type.getTemplate();

        try {
            String messageContent = getMessageContents(EmailSettings.TEMPLATE_PATH_ACCOUNT, currentTemplate, values);
            email.sendEmail(receiver, messageContent, type);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void notifyUser(User user, NotificationType type, String token) {
        Map<String, String> values = new HashMap<>();
        String receiver = generalInfoBuilder(user, values);
        String currentTemplate = type.getTemplate();
        setTokenValues(token, values);

        try {
            String messageContent = getMessageContents(EmailSettings.TEMPLATE_PATH_ACCOUNT, currentTemplate, values);
            email.sendEmail(receiver, messageContent, type);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void notifyUser(MatchGroup group, NotificationType type) {
        Map<String, String> values = new HashMap<>();
        List<String> receivers = new ArrayList<>();
        List<String> names = new ArrayList<>();
        List<Team> teams = group.getScoreCard().getRankedTeams();
        String currentTemplate = getCurrentTemplate(group, type.getTemplate());
        setGameValues(teams, receivers, names, values);

        try {
            for (int counter = 0; counter < names.size(); counter++) {
                values.replace("#fullname", names.get(counter));
                String receiver = receivers.get(counter);
                String messageContent = getMessageContents(EmailSettings.TEMPLATE_PATH_GAME, currentTemplate, values);
                email.sendEmail(receiver, messageContent, type);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String generalInfoBuilder(User user, Map<String, String> values){
        values.put("#fullname", user.getDisplayName());
        values.put("#userid", user.getUserId().toString());
        return user.getEmailAddress().toString();
    }

    private String replaceTags(String messageContent, List<String> tags, Map<String, String> values) {
        for (String tag : tags) {
            if (values.containsKey(tag)) {
                String value = values.get(tag);
                messageContent = messageContent.replaceAll(tag, value);
            }
        }
        return messageContent;
    }

    private static boolean isEndOfTag(char currentLetter) {
        return (currentLetter == ',' || currentLetter == '/' || currentLetter == '<');
    }

    private static List<String> findNotificationTags(String currentLine) {
        List<String> results = new ArrayList<>();
        String currentTag = "";
        char[] lineContents = currentLine.toCharArray();
        boolean currentStatus = false;
        boolean addToTags = false;
        for (char c : lineContents) {
            if (addToTags) {
                results.add(currentTag);
                addToTags = false;
                currentTag = "";
            }

            if (c == '#') {
                currentStatus = true;
            }

            if (currentStatus) {
                if (isEndOfTag(c)) {
                    currentStatus = false;
                    addToTags = true;
                    continue;
                }
                currentTag = currentTag + c;
            }
        }

        if (!currentTag.isEmpty()) {
            results.add(currentTag);
        }
        return results;
    }

    private String getCurrentTemplate(MatchGroup group, String template){
        if (group.getTeamCount() == MatchGroup.MIN_NUM_TEAMS) {
            return (template + GAME_REPORTED_THREE_TEAMS);
        }
        return (template + GAME_REPORTED_FOUR_TEAMS);
    }

    private String getMessageContents(String path, String template, Map<String, String> values) throws IOException {
        FileReader file = new FileReader(path + template + "." + EmailSettings.EMAILS_FORMAT);
        BufferedReader reader = new BufferedReader(file);
        List<String> messageTags = new ArrayList<>();
        String messageContent = setContentsAndTags(reader, messageTags);
        messageContent = replaceTags(messageContent, messageTags, values);
        messageTags.clear();
        reader.close();
        file.close();
        return messageContent;
    }

    private String setContentsAndTags(BufferedReader reader, List<String> messageTags) throws IOException {
        String line;
        String results = "";
        while ((line = reader.readLine()) != null) {
            results += line;
            if (line.contains("#")) {
                messageTags.addAll(findNotificationTags(line));
            }
            results += "\n";
        }
        return results;
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

    private static void setGameValues(List<Team> teams, List<String> receivers, List<String> names, Map<String, String> values) {
        values.put("#fullname", "");
        for (int counter = 0; counter < teams.size(); counter++) {
            int currentTeam = counter + 1;
            User firstPlayer = teams.get(counter).getFirstPlayer();
            User secondPlayer = teams.get(counter).getSecondPlayer();

            receivers.add(firstPlayer.getEmailAddress().toString());
            receivers.add(secondPlayer.getEmailAddress().toString());
            names.add(firstPlayer.getDisplayName());
            names.add(secondPlayer.getDisplayName());
            values.put("#team" + currentTeam + "player" + FIRST_PLAYER, firstPlayer.getDisplayName());
            values.put("#team" + currentTeam + "player" + SECOND_PLAYER, secondPlayer.getDisplayName());
        }
    }

}
