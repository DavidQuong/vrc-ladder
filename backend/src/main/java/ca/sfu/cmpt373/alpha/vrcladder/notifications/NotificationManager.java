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
 * TODO: Add type to all functions to make SMS notifications available.
 * Must check with Trevor for SMS API from AWS.
 */
public class NotificationManager {

    private Email email;

    public NotificationManager(){
        email = new Email();
    }

    public void notifyUserAboutTeamRelatedActivity(User user, Team team, NotificationType type, String activity){
        Map values = new HashMap();
        values.put("@fullname", user.getDisplayName());
        values.put("@preferrdtime", team.getAttendanceCard().getPreferredPlayTime().toString());
        values.put("@ladderposition", team.getLadderPosition().getValue().toString());
        values.put("@firstteammember", team.getFirstPlayer().getDisplayName());
        values.put("@secondteammember", team.getSecondPlayer().getDisplayName());
        String receiver = user.getEmailAddress().toString();

        try {
            FileReader file;
            BufferedReader reader;
            String messageContent = "";
            List<String> messageTags = new ArrayList<>();


            if(type.equals(NotificationType.EMAIL)){
                file = new FileReader("src\\main\\java\\ca\\sfu\\cmpt373\\alpha\\vrcladder\\notifications\\templates\\email_" + activity + "." + EmailSettings.EMAILS_FORMAT);

                reader = new BufferedReader(file);
                String line;

                while((line = reader.readLine()) != null){
                    messageContent += line;
                    if(line.contains("@")) {
                        String tag = findNotificationTags(line);
                        messageTags.add(tag);
                    }
                    messageContent += "\n";
                }

                file.close();
            }else{
                // TODO: implement the SMS parts here (After looking for AWS API for SMS).
            }

            messageContent = replaceTags(messageContent, messageTags, values);
            System.out.println(messageContent);
            email.sendTextEmail(receiver, messageContent, activity);
        } catch (FileNotFoundException e) {
            // TODO: handle this exception.
            e.printStackTrace();
        } catch (IOException e) {
            // TODO: handle this exception.
            e.printStackTrace();
        }

    }

    public void notifyUserAboutAccountActivity(User user, NotificationType type, String activity){

    }

    public void notifyUserAboutGameActivity(){

    }

    public void notifyRegisteration(User user){
        Map values = new HashMap();
        values.put("@fullname", user.getDisplayName());
        String receiver = user.getEmailAddress().toString();
        String subject = "Welcome to VRC";

        try {
            FileReader file = new FileReader("src\\main\\java\\ca\\sfu\\cmpt373\\alpha\\vrcladder\\notifications\\templates\\email_registrationconfirmed.txt");
            BufferedReader reader = new BufferedReader(file);
            List<String> messageTags = new ArrayList<>();
            String line;
            String messageContent = "";

            while((line = reader.readLine()) != null){
                messageContent += line;

                if(line.contains("@")) {
                    String tag = findNotificationTags(line);
                    messageTags.add(tag);
                }
                messageContent += "\n";
            }

            messageContent = replaceTags(messageContent, messageTags, values);
            email.sendTextEmail(receiver, subject, messageContent);
            file.close();
        } catch (FileNotFoundException e) {
            // TODO: handle this exception.
            e.printStackTrace();
        } catch (IOException e) {
            // TODO: handle this exception.
            e.printStackTrace();
        }
    }

    public void notifyGameScore(){

    }

    public void notifyGameScheduled(User user, Team team){
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

            while((line = reader.readLine()) != null){
                messageContent += line;

                if(line.contains("@")) {
                    String tag = findNotificationTags(line);
                    messageTags.add(tag);
                }
                messageContent += "\n";
            }

            messageContent = replaceTags(messageContent, messageTags, values);
            email.sendTextEmail(receiver, subject, messageContent);
            file.close();
        } catch (FileNotFoundException e) {
            // TODO: handle this exception.
            e.printStackTrace();
        } catch (IOException e) {
            // TODO: handle this exception.
            e.printStackTrace();
        }
    }

    private String findNotificationTags(String currentLine){
        String results = "";
        char[] lineContents = currentLine.toCharArray();
        boolean status = false;
        for(char c : lineContents){
            if(c == '@'){
                status = true;
            }

            if(status && c != ','){
                results = results + c;
            }
        }
        return results;
    }

    private String replaceTags(String messageContent, List<String> tags, Map values){
        for(String tag : tags){
            String value = values.get(tag).toString();
            messageContent = messageContent.replaceAll(tag, value);
        }
        return messageContent;
    }

}
