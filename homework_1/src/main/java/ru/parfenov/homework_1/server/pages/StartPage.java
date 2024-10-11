package ru.parfenov.homework_1.server.pages;

import lombok.RequiredArgsConstructor;
import ru.parfenov.homework_1.server.service.HabitService;
import ru.parfenov.homework_1.server.service.UserService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Выводит текст - меню для пользователя.
 * Войти в приложение можно или через регистрацию, или через ввод своего email
 */
@RequiredArgsConstructor
public class StartPage {
    private final UserService userService;
    private final HabitService habitService;
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public void run() throws IOException, InterruptedException {
        System.out.println("""
                Please enter:
                1 - registration
                or
                2 - enter id
                """);
        String enter = reader.readLine();
        switch (enter) {
            case "1":
                SignUpPage signUpPage = new SignUpPage(userService);
                signUpPage.run();
                break;
            case "2":
                SignInPage signInPage = new SignInPage(userService, habitService);
                signInPage.run();
                break;
            default:
                System.out.println("Please enter correct" + System.lineSeparator());
        }
        run();
    }
}