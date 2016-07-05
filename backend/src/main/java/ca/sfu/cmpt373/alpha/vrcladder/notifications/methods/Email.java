package ca.sfu.cmpt373.alpha.vrcladder.notifications.methods;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import java.util.Properties;

/**
 * Created by Hassan Fahad on 2016-07-02.
 */
public class Email {

    private String contentType;
    private String xMailer;
    private Session session;
    private MimeMessage message;
    private Transport transport;

    private static final String EMAIL_USERNAME = "hassan@espuresystems.com";
    private static final String EMAIL_PASSWORD = "M49D4s5";

    Properties properties;

    public Email() {
        contentType = "text/html; charset=utf-8";
        xMailer = "JAVA/" + Runtime.class.getPackage().getImplementationVersion();

        // TODO: Must change to VRC SMTP server.
        properties = System.getProperties();
        properties.put("mail.smtp.host", "mail.espuresystems.com");
        properties.put("mail.transport.protocol", "smtp");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", 25);

        session = Session.getDefaultInstance(properties);
        message = new MimeMessage(session);
        try {
            message.setFrom(new InternetAddress(EMAIL_USERNAME));
            message.setHeader("Content-Type", contentType);
            message.setHeader("MIME-VERSION", MimeMessage.class.getPackage().getImplementationVersion());
            message.setHeader("X-Mailer", xMailer);

            transport = session.getTransport("smtp");
            transport.connect("just124.justhost.com", EMAIL_USERNAME, EMAIL_PASSWORD);
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }

    public boolean sendTextEmail(String receiver, String subject, String messageContent) {
        try {

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


}
