package ru.parfenov.homework_1.server.email_send;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.parfenov.homework_1.server.model.User;
import ru.parfenov.homework_1.server.service.UserService;
import ru.parfenov.homework_1.server.utility.Utility;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Optional;
import java.util.Properties;

/**
 * API для отправки сообщений на емайлы (пока только для сброса забытого пароля)
 */
@Slf4j
@RequiredArgsConstructor
public class ResetPasswordViaEmail {
    private final String email;
    private final UserService userService;

    public void run() throws MessagingException {
        String from = Utility.emailOfApp;
        String host = Utility.hostOfApp;
        String smtpPort = "465";


        Properties properties = new Properties();
        properties.put("mail.smtp.host", host);
        properties.put("mail.smtp.port", smtpPort);
        properties.put("mail.smtp.ssl.enable", "true");
        properties.put("mail.smtp.auth", "true");

        Session session = Session.getInstance(
                properties,
                new Authenticator() {
                    @Override
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(from, Utility.mailPassword);
                    }
                }
        );
        session.setDebug(true);
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(from));
            message.addRecipient(MimeMessage.RecipientType.TO, new InternetAddress(email));
            message.setSubject("Reset Your password");
            message.setText(textOfMessage());
            Transport.send(message);
        } catch (Exception e) {
            log.error("Not send email!", e);
        }
    }

    private String textOfMessage() {
        Optional<User> user = userService.findByEmail(email);
        return user.map(value -> "Hello! Is this the code for reset password:" +
                System.lineSeparator() +
                value.getResetPassword() +
                System.lineSeparator() +
                "Please enter it on special page").orElse("");
    }
}