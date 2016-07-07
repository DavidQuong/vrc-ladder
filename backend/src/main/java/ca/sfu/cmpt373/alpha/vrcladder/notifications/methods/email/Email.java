package ca.sfu.cmpt373.alpha.vrcladder.notifications.methods.email;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.util.Properties;

public class Email {

    private String contentType;
    private MimeMessage message;
    private Transport transport;

    public Email() {
        contentType = getCharset();
        String xMailer = "JAVA/" + Runtime.class.getPackage().getImplementationVersion();

        // TODO: Must change to VRC SMTP server.
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", EmailSettings.SERVER);
        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", EmailSettings.SERVER_PORT);
        Session session = Session.getDefaultInstance(properties);
        message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(EmailSettings.USERNAME));
            message.setHeader("Content-Type", contentType);
            message.setHeader("MIME-VERSION", MimeMessage.class.getPackage().getImplementationVersion());
            message.setHeader("X-Mailer", xMailer);

            transport = session.getTransport("smtp");
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public void sendEmail(String receiver, String messageContent, String activity) {
        try {
            String subject = getEmailSubject(activity);
            message.addRecipients(Message.RecipientType.TO, receiver);
            message.setSubject(subject);
            message.setContent(messageContent, contentType);

            transport.connect(EmailSettings.SERVER, EmailSettings.USERNAME, EmailSettings.PASSWORD);
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    private String getEmailSubject(String activity) {
        String results;
        switch (activity) {
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
            case "activationrequired":
                results = EmailSettings.SUBJECT_ACCOUNT_NEEDS_ACTIVATION;
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

    private String getCharset() {
        if (EmailSettings.EMAILS_FORMAT.equals("txt")) {
            return "text/plain; charset=utf-8";
        }
        return "text/html; charset=utf-8";
    }

}
