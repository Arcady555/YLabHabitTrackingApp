package ru.parfenov.emailsend.cases;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.parfenov.emailsend.SendViaEmail;
import ru.parfenov.model.User;
import ru.parfenov.repository.UserRepository;
import ru.parfenov.utility.Utility;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;

/**
 * Класс формирует данные для отправки на емайл юзера сообщения для сброса пароля
 */
@Slf4j
@RequiredArgsConstructor
public class ResetPasswordViaEmail {
    private final HttpServletRequest request;
    private final UserRepository userRepository;

    public void run() throws MessagingException {
        User user = userRepository.findByEmail(Utility.getUserEmail(request));
        if (user != null) {
            String subject = "Reset Your password";
            String text = textOfMessage(user);
            SendViaEmail sendViaEmail = new SendViaEmail(user.getEmail(), subject, text);
            sendViaEmail.run();
        } else {
            log.error("User for reset password is not founded!");
        }
    }

    private String textOfMessage(User user) {
        return "Hello! Is this the code for reset password:" +
                System.lineSeparator() +
                user.getResetPassword() +
                System.lineSeparator() +
                "Please enter it on special page";
    }
}
