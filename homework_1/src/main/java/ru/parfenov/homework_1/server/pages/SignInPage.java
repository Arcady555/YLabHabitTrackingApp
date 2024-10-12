package ru.parfenov.homework_1.server.pages;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.parfenov.homework_1.server.enums.user.Role;
import ru.parfenov.homework_1.server.model.User;
import ru.parfenov.homework_1.server.service.HabitService;
import ru.parfenov.homework_1.server.service.UserService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class SignInPage {
    private final UserService userService;
    private final HabitService habitService;

    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public void run() throws IOException, InterruptedException {
        System.out.println("Enter email");
        String email = reader.readLine();
        System.out.println("Enter password");
        String password = reader.readLine();
        Optional<User> userOptional = userService.findByEmailAndPassword(email, password);
        if (userOptional.isEmpty()) {
            System.out.println("Not user with the email and the password!");
            run();
        } else {
            User user = userOptional.get();
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
}