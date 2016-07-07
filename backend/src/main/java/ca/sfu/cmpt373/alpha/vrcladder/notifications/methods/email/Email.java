package ca.sfu.cmpt373.alpha.vrcladder.notifications.methods.email;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.util.Properties;

public class Email {

    private String contentType;
    private String xMailer;
    private Session session;
    private MimeMessage message;
    private Transport transport;

    private Properties properties;

    public Email() {
        contentType = "text/html; charset=utf-8";
        xMailer = "JAVA/" + Runtime.class.getPackage().getImplementationVersion();

        // TODO: Must change to VRC SMTP server.
        properties = System.getProperties();
        properties.put("mail.smtp.host", EmailSettings.SERVER);
        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", EmailSettings.SERVER_PORT);

        session = Session.getDefaultInstance(properties);
        message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(EmailSettings.USERNAME));
            message.setHeader("Content-Type", contentType);
            message.setHeader("MIME-VERSION", MimeMessage.class.getPackage().getImplementationVersion());
            message.setHeader("X-Mailer", xMailer);

            transport = session.getTransport("smtp");
            transport.connect(EmailSettings.SERVER, EmailSettings.USERNAME, EmailSettings.PASSWORD);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public boolean sendTextEmail(String receiver, String messageContent, String template) {
        try {

            String subject = getEmailSubject(template);
            message.addRecipients(Message.RecipientType.TO, receiver);
            message.setSubject(subject);
            message.setText(messageContent);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();

        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean sendHtmlEmail(String receiver, String subject, String messageContent) {
        try {

            message.addRecipients(Message.RecipientType.TO, receiver);
            message.setSubject(subject);
            message.setContent(messageContent, contentType);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();

        } catch (MessagingException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private String getEmailSubject(String template){
        String results;
        switch(template){
            case "teamcreated":
                results = EmailSettings.SUBJECT_ADDED_TO_TEAM;
                break;
            case "teamtimeupdated":
                results = EmailSettings.SUBJECT_TEAM_TIME_UPDATED;
                break;
            case "gamescheduled":
                results = EmailSettings.SUBJECT_GAME_SCHEDULED;
                break;
            case "gamescheduledreminder":
                results = EmailSettings.SUBJECT_GAME_SCHEDULED_REMINDER;
                break;
            case "gamescorereported":
                results = EmailSettings.SUBJECT_GAME_SCORES_ENTERED;
                break;
            case "registrationconfirmed":
                results = EmailSettings.SUBJECT_ACCOUNT_ACTIVATED;
                break;
            case "passwordupdated":
                results = EmailSettings.SUBJECT_PASSWORD_UPDATED;
                break;
            case "passwordreset":
                results = EmailSettings.SUBJECT_PASSWORD_RESET;
                break;
            case "loginfailed":
                results = EmailSettings.SUBJECT_FAILED_LOGIN;
                break;
            case "gamescoresreportedwrong":
                results = EmailSettings.SUBJECT_GAME_SCORES_REPORTED_WRONG;
                break;
            case "gamescoresupdated":
                results = EmailSettings.SUBJECT_GAME_SCORES_UPDATED;
                break;
            default:
                results = EmailSettings.SUBJECT_GENERAL_EMAIL;
                break;
        }
        return results;
    }


}
