package ca.sfu.cmpt373.alpha.vrcladder.notifications;


import ca.sfu.cmpt373.alpha.vrcladder.notifications.methods.NotificationType;
import ca.sfu.cmpt373.alpha.vrcladder.notifications.methods.email.Email;
import ca.sfu.cmpt373.alpha.vrcladder.notifications.methods.email.EmailSettings;
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

    public NotificationManager() {
        email = new Email();
    }

    public void notifyUserAboutTeamRelatedActivity(User user, Team team, NotificationType type, String activity) {
        Map values = new HashMap();
        values.put("#fullname", user.getDisplayName());
        setTeamValues(team, type, values);
        String receiver = getReceiver(user, type);
        String currentType = type.toString().toLowerCase();

        try {
            FileReader file = new FileReader("src\\main\\java\\ca\\sfu\\cmpt373\\alpha\\vrcladder\\notifications\\templates\\team\\" + currentType + "_" + activity + "." + EmailSettings.EMAILS_FORMAT);
            BufferedReader reader = new BufferedReader(file);
            String messageContent = "";
            List<String> messageTags = new ArrayList<>();

            String line;
            while ((line = reader.readLine()) != null) {
                messageContent += line;
                if (line.contains("#")) {
                    String tag = findNotificationTags(line);
                    messageTags.add(tag);
                }
                messageContent += "\n";
            }

            file.close();
            messageContent = replaceTags(messageContent, messageTags, values);
            System.out.println(messageContent);

            sendNotification(receiver, messageContent, activity, type);
        } catch (FileNotFoundException e) {
            // TODO: handle this exception.
            e.printStackTrace();
        } catch (IOException e) {
            // TODO: handle this exception.
            e.printStackTrace();
        }
    }

    public void notifyUserAboutAccountRelatedActivity(User user, NotificationType type, String activity){
        Map values = new HashMap();
        values.put("#fullname", user.getDisplayName());
        String receiver = getReceiver(user, type);
        String currentType = type.toString().toLowerCase();

        try {
            FileReader file = new FileReader("src\\main\\java\\ca\\sfu\\cmpt373\\alpha\\vrcladder\\notifications\\templates\\account\\" + currentType + "_" + activity + "." + EmailSettings.EMAILS_FORMAT);
            BufferedReader reader = new BufferedReader(file);
            String messageContent = "";
            List<String> messageTags = new ArrayList<>();

            String line;
            while ((line = reader.readLine()) != null) {
                messageContent += line;
                if (line.contains("#")) {
                    String tag = findNotificationTags(line);
                    messageTags.add(tag);
                }
                messageContent += "\n";
            }

            file.close();
            messageContent = replaceTags(messageContent, messageTags, values);
            System.out.println(messageContent);

            sendNotification(receiver, messageContent, activity, type);
        } catch (FileNotFoundException e) {
            // TODO: handle this exception.
            e.printStackTrace();
        } catch (IOException e) {
            // TODO: handle this exception.
            e.printStackTrace();
        }
    }

    public void notifyUserTokenBasedRequests(User user, NotificationType type, String activity, String token){
        Map values = new HashMap();
        values.put("#fullname", user.getDisplayName());
        values.put("#token", token);
        values.put("#userid", user.getUserId().toString());
        values.put("#sitelink", "http://www.vrc.ca");
        String receiver = getReceiver(user, type);
        String currentType = type.toString().toLowerCase();

        try {
            FileReader file = new FileReader("src\\main\\java\\ca\\sfu\\cmpt373\\alpha\\vrcladder\\notifications\\templates\\account\\" + currentType + "_" + activity + "." + EmailSettings.EMAILS_FORMAT);
            BufferedReader reader = new BufferedReader(file);
            String messageContent = "";
            List<String> messageTags = new ArrayList<>();

            String line;
            while ((line = reader.readLine()) != null) {
                messageContent += line;
                if (line.contains("#")) {
                    String tag = findNotificationTags(line);
                    messageTags.add(tag);
                }
                messageContent += "\n";
            }
            messageTags.add("#userid");
            messageTags.add("#token");

            file.close();
            messageContent = replaceTags(messageContent, messageTags, values);
            System.out.println(messageContent);

            sendNotification(receiver, messageContent, activity, type);
        } catch (FileNotFoundException e) {
            // TODO: handle this exception.
            e.printStackTrace();
        } catch (IOException e) {
            // TODO: handle this exception.
            e.printStackTrace();
        }
    }

    public void notifyUserAboutGameRelatedActivity(User user, NotificationType type, String activity){

    }

    /*
    public void notifyGameScheduled(User user, Team team) {
        Map values = new HashMap();
        values.put("@fullname", user.getDisplayName());
        values.put("@preferrdtime", team.getAttendanceCard().getPreferredPlayTime());
        values.put("@ladderposition", team.getLadderPosition().getValue().toString());
        values.put("@firstteammember", team.getFirstPlayer().getDisplayName());
        values.put("@secondteammember", team.getSecondPlayer().getDisplayName());
        String receiver = user.getEmailAddress().toString();
        String subject = "You have a new scheduled game";

        try {
            FileReader file = new FileReader("src\\main\\java\\ca\\sfu\\cmpt373\\alpha\\vrcladder\\notifications\\templates\\email_registrationconfirmed.txt");
            BufferedReader reader = new BufferedReader(file);
            List<String> messageTags = new ArrayList<>();
            String line;
            String messageContent = "";

            while ((line = reader.readLine()) != null) {
                messageContent += line;

                if (line.contains("@")) {
                    String tag = findNotificationTags(line);
                    messageTags.add(tag);
                }
                messageContent += "\n";
            }

            messageContent = replaceTags(messageContent, messageTags, values);
            email.sendTextEmail(receiver, subject, messageContent);
            file.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    */


    private static void setTeamValues(Team team, NotificationType type, Map values){
        if(type.equals(NotificationType.EMAIL)){
            values.put("#preferrdtime", team.getAttendanceCard().getPreferredPlayTime());
            values.put("#ladderposition", team.getLadderPosition().getValue().toString());
            values.put("#firstteammember", team.getFirstPlayer().getDisplayName());
            values.put("#secondteammember", team.getSecondPlayer().getDisplayName());
        }
    }

    private String findNotificationTags(String currentLine) {
        String results = "";
        char[] lineContents = currentLine.toCharArray();
        boolean status = false;
        for (char c : lineContents) {
            if (c == '#') {
                status = true;
            }

            if (status) {
                if(c == ',' || c == '/'){
                    break;
                }
                results = results + c;
            }
        }
        return results;
    }

    private String replaceTags(String messageContent, List<String> tags, Map values) {
        for (String tag : tags) {
            String value = values.get(tag).toString();
            messageContent = messageContent.replaceAll(tag, value);
        }
        return messageContent;
    }

    private void sendNotification(String reciever, String content, String activity, NotificationType type){
        if(type.equals(NotificationType.EMAIL)){
            email.sendEmail(reciever, content, activity);
        }
    }

    private String getReceiver(User user, NotificationType type){
        if(type.equals(NotificationType.EMAIL)){
            return user.getEmailAddress().toString();
        }
        return user.getPhoneNumber().toString();
    }

}
