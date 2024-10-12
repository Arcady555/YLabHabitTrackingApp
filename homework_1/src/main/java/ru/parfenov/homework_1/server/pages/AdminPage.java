package ru.parfenov.homework_1.server.pages;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.parfenov.homework_1.server.pages.admin.*;
import ru.parfenov.homework_1.server.service.HabitService;
import ru.parfenov.homework_1.server.service.UserService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class AdminPage implements UserMenuPage {
    private final UserService userService;
    private final HabitService habitService;
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    @Override
    public void run() throws IOException, InterruptedException {
        List<UserMenuPage> adminMenuList = List.of(
                new AllUsersPage(userService),
                new UserPage(userService),
                new UsersByParametersPage(userService),
                new UpdateUserPage(userService),
                new HabitsOfUserPage(userService, habitService)
        );
        while (true) {
            System.out.println("""
                    What operation?
                    0 - view all users
                    1 - find the user by email
                    2 - find the user by parameters
                    3 - update or delete user
                    4 - view the habits of user
                    5 - exit
                    """);
            String answerStr = reader.readLine();
            UserMenuPage adminMenuPage;
            try {
                int answer = Integer.parseInt(answerStr);
                if (answer == 5) return;
                adminMenuPage = adminMenuList.get(answer);
                if (adminMenuPage == null) {
                    System.out.println("Please enter correct" + System.lineSeparator());
                    run();

                }
                assert adminMenuPage != null;
                adminMenuPage.run();
            } catch (NumberFormatException e) {
                log.error("Please enter the NUMBER!!", e);
                System.out.println(System.lineSeparator());
                run();
            }
        }
    }
}