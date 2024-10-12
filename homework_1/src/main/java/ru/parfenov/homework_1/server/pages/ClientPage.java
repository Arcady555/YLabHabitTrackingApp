package ru.parfenov.homework_1.server.pages;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.parfenov.homework_1.server.model.User;
import ru.parfenov.homework_1.server.pages.client.*;
import ru.parfenov.homework_1.server.service.HabitService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class ClientPage implements UserMenuPage {
    private final User user;
    private final HabitService habitService;
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    @Override
    public void run() throws IOException, InterruptedException {
        List<UserMenuPage> clientMenuList = List.of(
                new YourHabitsTodayPage(user, habitService),
                new PerformHabitPage(user, habitService),
                new CreateHabitPage(user, habitService),
                new YourHabitListPage(user, habitService),
                new UpdateHabitPage(user, habitService),
                new HabitsStatisticPage(user, habitService)
        );
        while (true) {
            System.out.println("""
                    What operation?
                    0 - view Your habits today
                    1 - perform habit
                    2 - create habit
                    3 - view all Your habits
                    4 - Your habit: view, edit or delete
                    5 - view statistic
                    6 - exit
                    """);
            String answerStr = reader.readLine();
            UserMenuPage clientMenuPage;
            try {
                int answer = Integer.parseInt(answerStr);
                if (answer == 6) return;
                clientMenuPage = clientMenuList.get(answer);
                if (clientMenuPage == null) {
                    System.out.println("Please enter correct" + System.lineSeparator());
                    run();
                }
                assert clientMenuPage != null;
                clientMenuPage.run();
            } catch (NumberFormatException e) {
                System.out.println("Please enter the NUMBER!" + System.lineSeparator());
                run();
            }
        }
    }
}