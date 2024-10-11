package ru.parfenov.homework_1.server.pages.client;

import lombok.RequiredArgsConstructor;
import ru.parfenov.homework_1.server.model.Habit;
import ru.parfenov.homework_1.server.model.User;
import ru.parfenov.homework_1.server.pages.UserMenuPage;
import ru.parfenov.homework_1.server.service.HabitService;

import java.io.IOException;

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