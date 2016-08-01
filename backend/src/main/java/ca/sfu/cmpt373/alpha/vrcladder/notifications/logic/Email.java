package ca.sfu.cmpt373.alpha.vrcladder.notifications.logic;

import ca.sfu.cmpt373.alpha.vrcladder.exceptions.CannotSetFromEmailException;
import ca.sfu.cmpt373.alpha.vrcladder.exceptions.EmailHeaderNotCreatedException;
import ca.sfu.cmpt373.alpha.vrcladder.exceptions.MessageNotDeliveredException;
import ca.sfu.cmpt373.alpha.vrcladder.exceptions.SubjectNotFoundException;
import ca.sfu.cmpt373.alpha.vrcladder.users.personal.EmailAddress;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.io.UnsupportedEncodingException;
import java.util.Properties;

public class Email {

    private final String contentType;
    private final MimeMessage message;
    private Transport transport;

    public Email() {
        contentType = getCharset();
        String xMailer = "JAVA/" + Runtime.class.getPackage().getImplementationVersion();
        Properties properties = System.getProperties();
        properties.put("mail.smtp.host", EmailSettings.SERVER);
        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", EmailSettings.SERVER_PORT);
        Session session = Session.getDefaultInstance(properties);
        message = new MimeMessage(session);

        try {
            message.setFrom(new InternetAddress(EmailSettings.USERNAME, EmailSettings.FROM_NAME));
            message.setHeader("MIME-VERSION", MimeMessage.class.getPackage().getImplementationVersion());
            message.setHeader("X-Mailer", xMailer);
            transport = session.getTransport("smtp");
        } catch (MessagingException e) {
            throw new EmailHeaderNotCreatedException();
        } catch (UnsupportedEncodingException e) {
            throw new CannotSetFromEmailException();
        }
    }

    public void sendEmail(EmailAddress receiver, String messageContent, NotificationType type) {
        new Thread(() -> asyncSendEmail(receiver, messageContent,type)).start();
    }

    private void asyncSendEmail(EmailAddress receiver, String messageContent, NotificationType type){
        try {
            String subject = getEmailSubject(type.getTemplate());
            MimeMessage currentMessage = message;
            currentMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(receiver.toString()));
            currentMessage.setSubject(subject);
            currentMessage.setContent(messageContent, contentType);
            currentMessage.setHeader("Content-Type", contentType);

            transport.connect(EmailSettings.SERVER, EmailSettings.USERNAME, EmailSettings.PASSWORD);
            transport.sendMessage(currentMessage, currentMessage.getAllRecipients());
            transport.close();
        } catch (MessagingException e) {
            throw new MessageNotDeliveredException();
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
                throw new SubjectNotFoundException();
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
