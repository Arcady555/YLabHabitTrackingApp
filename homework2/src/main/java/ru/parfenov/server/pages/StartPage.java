package ru.parfenov.server.pages;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.parfenov.server.service.HabitService;
import ru.parfenov.server.service.UserService;

import javax.mail.MessagingException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Выводит текст - меню для пользователя.
 * Войти в приложение можно или через регистрацию, или через ввод своего email
 */
@Slf4j
@RequiredArgsConstructor
public class StartPage {
    private final UserService userService;
    private final HabitService habitService;
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    public void run() throws IOException, InterruptedException, MessagingException {
        System.out.println("""
                Please enter:
                1 - registration
                or
                2 - enter email
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