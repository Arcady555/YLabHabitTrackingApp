package ru.parfenov.server.pages.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.parfenov.server.model.User;
import ru.parfenov.server.pages.UserMenuPage;
import ru.parfenov.server.service.UserService;
import ru.parfenov.server.utility.Utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Страница, где юзер может ввести код, полученный по емайл, чтобы обновить забытый пароль
 */
@Slf4j
@RequiredArgsConstructor
public class ResetPasswordPage implements UserMenuPage {
    private final User user;
    private final UserService userService;
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    @Override
    public void run() throws IOException, InterruptedException {
        System.out.println("Enter the code received via email" + System.lineSeparator());
        if (reader.readLine().equals(user.getResetPassword())) {
            System.out.println("Enter new password" + System.lineSeparator());
            String newPassword = reader.readLine();
            String newResetPassword = Utility.generateForResetPassword();
            userService.update(user.getId(), newPassword, newResetPassword, "", null, "");
        } else {
            System.out.println("Incorrect code!");
        }
    }
}