package ru.parfenov.homework_2.server.email_send;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.parfenov.homework_2.server.utility.Utility;

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
public class SendViaEmail {
    private final String email;
    private final String subject;
    private final String messageBody;

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
            message.setSubject(subject);
            message.setText(messageBody);
            Transport.send(message);
        } catch (Exception e) {
            log.error("Not send email!", e);
        }
    }
}