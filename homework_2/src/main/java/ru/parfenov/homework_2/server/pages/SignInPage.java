package ru.parfenov.homework_2.server.pages;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.parfenov.homework_2.server.email_send.cases.ResetPasswordViaEmail;
import ru.parfenov.homework_2.server.enums.user.Role;
import ru.parfenov.homework_2.server.model.User;
import ru.parfenov.homework_2.server.pages.client.ResetPasswordPage;
import ru.parfenov.homework_2.server.service.HabitService;
import ru.parfenov.homework_2.server.service.UserService;

import javax.mail.MessagingException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Optional;

/**
 * Страница, где зарегистрированные пользователи заходят в приложение, авторизуясь через ввод
 * своего емайл и пароля.
 * Предусмотрен сброс пароля (через ввод кода, полученного по емайл), если юзер его забыл
 */
@Slf4j
@RequiredArgsConstructor
public class SignInPage {
    private final UserService userService;
    private final HabitService habitService;

    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public void run() throws IOException, InterruptedException, MessagingException {
        System.out.println("Enter email(or exit)");
        String email = reader.readLine();
        if (email.equals("exit")) return;
        Optional<User> userOptional1 = userService.findByEmail(email);
        if (userOptional1.isPresent()) {
            System.out.println("Forgot password? - 0, another key - Enter password");
            if (reader.readLine().equals("0")) {
                ResetPasswordViaEmail resetPasswordViaEmail = new ResetPasswordViaEmail(email, userOptional1.get());
                resetPasswordViaEmail.run();
                ResetPasswordPage resetPasswordPage = new ResetPasswordPage(userOptional1.get(), userService);
                resetPasswordPage.run();
                run();
            } else {
                System.out.println("Enter password");
                String password = reader.readLine();
                Optional<User> userOptional2 = userService.findByEmailAndPassword(email, password);
                if (userOptional2.isEmpty()) {
                    System.out.println("Not user with the email and the password!");
                    run();
                } else {
                    User user = userOptional2.get();
                    if (user.isBlocked()) {
                        System.out.println("Sorry, You are blocked!(( Contact support.");
                        run();
                    }
                    Map<Role, UserMenuPage> userMenuMap = Map.of(
                            Role.ADMIN, new AdminPage(userService, habitService),
                            Role.CLIENT, new ClientPage(user, habitService)
                    );
                    UserMenuPage userMenuPage = userMenuMap.get(user.getRole());
                    userMenuPage.run();
                }
            }
        } else {
            System.out.println("Not user with the email!" + System.lineSeparator());
        }
    }
}