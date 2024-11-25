package ru.parfenov.emailsend;

import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

/**
 * API для отправки сообщений на емайлы
 * Создан в расчёте на то, что емайл приложения базируется на Mail.ru.
 * Там можно в личном кабинете сгенерировать параметр "пароли приложений".
 * Он вписывается в поле Utility.mailPassword
 */
@Slf4j
@RequiredArgsConstructor
@Component
@Setter
public class SendViaEmail {
    private @Value("${emailInfoApp}") String emailOfApp;
    private @Value("${emailInfoPass}") String password;
    private @Value("${hostForSending}") String host;

    public void run(String email, String subject, String messageBody) throws MessagingException {
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
                        return new PasswordAuthentication(emailOfApp, password);
                    }
                }
        );
        session.setDebug(true);
        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(emailOfApp));
            message.addRecipient(MimeMessage.RecipientType.TO, new InternetAddress(email));
            message.setSubject(subject);
            message.setText(messageBody);
            Transport.send(message);
        } catch (Exception e) {
            log.error("Not send email!", e);
        }
    }
}