package ru.parfenov.server.pages.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.parfenov.server.model.Habit;
import ru.parfenov.server.model.User;
import ru.parfenov.server.pages.UserMenuPage;
import ru.parfenov.server.service.HabitService;

import java.io.IOException;

/**
 * Страница вывода списка всех привычек юзера
 */
@Slf4j
@RequiredArgsConstructor
public class YourHabitListPage implements UserMenuPage {
    private final User user;
    private final HabitService habitService;

    @Override
    public void run() throws IOException, InterruptedException {
        for (Habit habit : habitService.findByUser(user)) {
            System.out.println(habit);
            System.out.println(habitService.remind(habit.getId()));
        }
    }
}