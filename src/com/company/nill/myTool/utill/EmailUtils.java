package com.company.nill.myTool.utill;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

public class EmailUtils {

    final static Logger L = LoggerFactory.getLogger(EmailUtils.class);

    public static boolean sendMail(String toEmail,String title,String html) {
        final String username = "";
        final String password = "";

        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.starttls.enable", "true"); //TLS
        prop.put("mail.transport.protocol", "smtp");
        prop.put("mail.debug", "true");
        prop.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        prop.put("mail.smtp.ssl.protocols", "TLSv1.2");

        Session session = Session.getInstance(prop,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {

            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress("support@first-raven.io","first-raven"));
            message.setRecipients(
                    Message.RecipientType.TO,
                    InternetAddress.parse(toEmail)
            );
            message.setSubject(title);
            message.setContent(html, "text/html; charset=utf-8");
            Transport.send(message);
            return true;
        } catch (MessagingException | UnsupportedEncodingException e) {
            L.error(e.getLocalizedMessage(),e);
            return false;
        }

    }
}
