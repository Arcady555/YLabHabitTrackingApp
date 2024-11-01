package ru.parfenov.emailsend.cases;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.parfenov.emailsend.SendViaEmail;
import ru.parfenov.model.User;

import javax.mail.MessagingException;

/**
 * Класс формирует данные для отправки на емайл юзера сообщения для сброса пароля
 */
@Slf4j
@RequiredArgsConstructor
public class ResetPasswordViaEmail {
    private final User user;

    public void run() throws MessagingException {
        String subject = "Reset Your password";
        String text = textOfMessage(user);
        SendViaEmail sendViaEmail = new SendViaEmail(user.getEmail(), subject, text);
        sendViaEmail.run();
    }

    private String textOfMessage(User user) {
        return "Hello! Is this the code for reset password:" +
                System.lineSeparator() +
                user.getResetPassword() +
                System.lineSeparator() +
                "Please enter it on special page";
    }
}