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

/**
 * Must check with Trevor or David for SMS API from AWS.
 */
public class NotificationManager {

    private Email email;
    private static final String GAME_REPORTED_THREE_TEAMS = "threeteams";
    private static final String GAME_REPORTED_FOUR_TEAMS  = "fourteams";
    private static final int FIRST_PLAYER = 1;
    private static final int SECOND_PLAYER = 2;

    public NotificationManager() {
        email = new Email();
    }

    public void notifyUserAboutTeamRelatedActivity(User user, Team team, NotificationType type) {
        Map<String, String> values = new HashMap<>();
        values.put("#fullname", user.getDisplayName());
        setTeamValues(team, values);
        String receiver = user.getEmailAddress().toString();
        String currentTemplate = type.getTemplate();

        try {
            FileReader file = new FileReader("src\\main\\java\\ca\\sfu\\cmpt373\\alpha\\vrcladder\\notifications\\templates\\team\\email_" + currentTemplate + "." + EmailSettings.EMAILS_FORMAT);
            BufferedReader reader = new BufferedReader(file);
            String messageContent = "";
            List<String> messageTags = new ArrayList<>();

            String line;
            while ((line = reader.readLine()) != null) {
                messageContent += line;
                if (line.contains("#")) {
                    messageTags.addAll(findNotificationTags(line));
                }
                messageContent += "\n";
            }

            file.close();
            messageContent = replaceTags(messageContent, messageTags, values);
            System.out.println(messageContent);

            email.sendEmail(receiver, messageContent, type);
        } catch (IOException e) {
            // TODO: handle this exception.
            e.printStackTrace();
        }
    }

    public void notifyUserAboutAccountRelatedActivity(User user, NotificationType type) {
        Map<String, String> values = new HashMap<>();
        values.put("#fullname", user.getDisplayName());
        String receiver = user.getEmailAddress().toString();
        String currentTemplate = type.getTemplate();

        try {
            FileReader file = new FileReader("src\\main\\java\\ca\\sfu\\cmpt373\\alpha\\vrcladder\\notifications\\templates\\account\\email_" + currentTemplate + "." + EmailSettings.EMAILS_FORMAT);
            BufferedReader reader = new BufferedReader(file);
            String messageContent = "";
            List<String> messageTags = new ArrayList<>();

            String line;
            while ((line = reader.readLine()) != null) {
                messageContent += line;
                if (line.contains("#")) {
                    messageTags.addAll(findNotificationTags(line));
                }
                messageContent += "\n";
            }

            file.close();
            messageContent = replaceTags(messageContent, messageTags, values);
            System.out.println(messageContent);

            email.sendEmail(receiver, messageContent, type);
        } catch (IOException e) {
            // TODO: handle this exception.
            e.printStackTrace();
        }
    }

    public void notifyUserTokenBasedRequests(User user, NotificationType type, String token) {
        Map<String, String> values = new HashMap<>();
        values.put("#fullname", user.getDisplayName());
        values.put("#token", token);
        values.put("#userid", user.getUserId().toString());
        values.put("#sitelink", "http://www.vrc.ca");
        String receiver = user.getEmailAddress().toString();
        String currentTemplate = type.getTemplate();

        try {
            FileReader file = new FileReader("src\\main\\java\\ca\\sfu\\cmpt373\\alpha\\vrcladder\\notifications\\templates\\account\\email_" + currentTemplate + "." + EmailSettings.EMAILS_FORMAT);
            BufferedReader reader = new BufferedReader(file);
            String messageContent = "";
            List<String> messageTags = new ArrayList<>();

            String line;
            while ((line = reader.readLine()) != null) {
                messageContent += line;
                if (line.contains("#")) {
                    messageTags.addAll(findNotificationTags(line));
                }
                messageContent += "\n";
            }

            file.close();
            messageContent = replaceTags(messageContent, messageTags, values);
            System.out.println(messageContent);

            email.sendEmail(receiver, messageContent, type);
        } catch (IOException e) {
            // TODO: handle this exception.
            e.printStackTrace();
        }
    }

    public void notifyUserAboutGameScores(MatchGroup group, NotificationType type) {
        String currentTemplate = type.getTemplate();
        if (group.getTeamCount() == MatchGroup.MIN_NUM_TEAMS) {
            currentTemplate = currentTemplate + GAME_REPORTED_THREE_TEAMS;
        } else {
            currentTemplate = currentTemplate + GAME_REPORTED_FOUR_TEAMS;
        }

        Map<String, String> values = new HashMap<>();
        List<Team> teams = group.getScoreCard().getRankedTeams();
        List<String> receivers = new ArrayList<>();
        List<String> names = new ArrayList<>();
        setReceiverValues(teams, receivers, names, type);
        setGameValues(values, teams);

        try {
            values.put("#fullname", "");

            for (int counter = 0; counter < names.size(); counter++) {
                FileReader file = new FileReader("src\\main\\java\\ca\\sfu\\cmpt373\\alpha\\vrcladder\\notifications\\templates\\game\\email_" + currentTemplate + "." + EmailSettings.EMAILS_FORMAT);
                BufferedReader reader = new BufferedReader(file);
                List<String> messageTags = new ArrayList<>();
                String messageContent = "";
                String receiver = receivers.get(counter);
                values.replace("#fullname", names.get(counter));

                String line;
                while ((line = reader.readLine()) != null) {
                    messageContent += line;
                    if (line.contains("#")) {
                        messageTags.addAll(findNotificationTags(line));
                    }
                    messageContent += "\n";
                }

                messageContent = replaceTags(messageContent, messageTags, values);
                System.out.println(messageContent);
                email.sendEmail(receiver, messageContent, type);
                file.close();
            }

        } catch (IOException e) {
            // TODO: handle this exception.
            e.printStackTrace();
        }
    }

    private List<String> findNotificationTags(String currentLine) {
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

    private String replaceTags(String messageContent, List<String> tags, Map values) {
        for (String tag : tags) {
            if (values.containsKey(tag)) {
                String value = values.get(tag).toString();
                messageContent = messageContent.replaceAll(tag, value);
            }
        }
        return messageContent;
    }

    private boolean isEndOfTag(char currentLetter) {
        return (currentLetter == ',' || currentLetter == '/' || currentLetter == '<');
    }

    private static void setTeamValues(Team team, Map<String, String> values) {
            values.put("#preferrdtime", team.getAttendanceCard().getPreferredPlayTime().toString());
            values.put("#ladderposition", team.getLadderPosition().getValue().toString());
            values.put("#firstteammember", team.getFirstPlayer().getDisplayName());
            values.put("#secondteammember", team.getSecondPlayer().getDisplayName());
    }

    private static void setReceiverValues(List<Team> teams, List<String> receivers, List<String> names, NotificationType type) {
        for (Team team : teams) {
            String firstReceiver = team.getFirstPlayer().getEmailAddress().toString();
            receivers.add(firstReceiver);
            String secondReceiver = team.getSecondPlayer().getEmailAddress().toString();
            receivers.add(secondReceiver);

            names.add(team.getFirstPlayer().getDisplayName());
            names.add(team.getSecondPlayer().getDisplayName());
        }
    }

    private static void setGameValues(Map<String, String> values, List<Team> teams) {
        for (int counter = 0; counter < teams.size(); counter++) {
            int currentTeam = counter + 1;
            values.put("#team" + currentTeam + "player" + FIRST_PLAYER, teams.get(counter).getFirstPlayer().getDisplayName());
            values.put("#team" + currentTeam + "player" + SECOND_PLAYER, teams.get(counter).getSecondPlayer().getDisplayName());
        }
    }

}
