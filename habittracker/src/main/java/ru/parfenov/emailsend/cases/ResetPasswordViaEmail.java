package ru.parfenov.emailsend.cases;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.parfenov.emailsend.SendViaEmail;
import ru.parfenov.model.User;
import ru.parfenov.repository.UserRepository;
import ru.parfenov.utility.Utility;

import javax.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;

import java.util.Optional;

/**
 * Класс формирует данные для отправки на емайл юзера сообщения для сброса пароля
 */
@Slf4j
@RequiredArgsConstructor
@Component
public class ResetPasswordViaEmail {
    private final HttpServletRequest request;
    private final UserRepository userRepository;
    private final SendViaEmail sendViaEmail;

    public void run() {
        Optional<User> userOptional = userRepository.findByEmail(Utility.getUserEmail());
        if (userOptional.isPresent()) {
            try {
                String subject = "Reset Your password";
                String text = textOfMessage(userOptional.get());
                sendViaEmail.run(userOptional.get().getEmail(), subject, text);
            } catch (MessagingException e) {
                log.error("User for reset password is not founded!", e);
            }
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
