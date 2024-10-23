package ru.parfenov.homework_2.server.pages.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.parfenov.homework_2.server.model.Habit;
import ru.parfenov.homework_2.server.model.User;
import ru.parfenov.homework_2.server.pages.UserMenuPage;
import ru.parfenov.homework_2.server.service.HabitService;

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