package ca.sfu.cmpt373.alpha.vrcladder.notifications.logic;

import ca.sfu.cmpt373.alpha.vrcladder.exceptions.CannotSetFromEmailException;
import ca.sfu.cmpt373.alpha.vrcladder.exceptions.EmailHeaderNotCreatedException;
import ca.sfu.cmpt373.alpha.vrcladder.exceptions.MessageNotDeliveredException;
import ca.sfu.cmpt373.alpha.vrcladder.exceptions.SubjectNotFoundException;
import ca.sfu.cmpt373.alpha.vrcladder.users.personal.EmailAddress;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

public class Email {

    private final String contentType;
    private final String contentTypeAttachment;
    private final MimeMessage message;
    private Transport transport;

    public Email() {
        contentType = getCharset();
        contentTypeAttachment = "multipart/mixed";
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

    public void sendEmail(EmailAddress receiver, String messageContent, String type, String pdfPath) {
        new Thread(() -> asyncSendEmail(receiver, messageContent, type, pdfPath)).start();
    }

    private void asyncSendEmail(EmailAddress receiver, String messageContent, String type, String pdfPath){
        try {
            String subject = getEmailSubject(type);
            MimeMessage currentMessage = message;
            currentMessage.setRecipient(Message.RecipientType.TO, new InternetAddress(receiver.toString()));
            currentMessage.setSubject(subject);
            BodyPart bodyPart = getMessageContent(messageContent);
            BodyPart AttachmentPart = getAttachment(pdfPath);

            Multipart multiPart = new MimeMultipart();
            multiPart.addBodyPart(bodyPart);
            multiPart.addBodyPart(AttachmentPart);
            currentMessage.setContent(multiPart);

            transport.connect(EmailSettings.SERVER, EmailSettings.USERNAME, EmailSettings.PASSWORD);
            transport.sendMessage(currentMessage, currentMessage.getAllRecipients());
            transport.close();
        } catch (MessagingException e) {
            throw new MessageNotDeliveredException();
        }
    }

    private BodyPart getMessageContent(String messageContent) throws MessagingException {
        BodyPart bodyPart = new MimeBodyPart();
        bodyPart.setContent(messageContent, contentType);
        bodyPart.setHeader("Content-Type", contentType);
        return bodyPart;
    }

    private BodyPart getAttachment(String pdfPath) throws MessagingException {
        BodyPart AttachmentPart = new MimeBodyPart();
        DataSource sourcePDF = new FileDataSource(pdfPath);
        AttachmentPart.setDataHandler(new DataHandler(sourcePDF));
        AttachmentPart.setFileName(pdfPath);
        return AttachmentPart;
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
            case "pdf":
                results = EmailSettings.SUBJECT_PDF_CONTENT;
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
