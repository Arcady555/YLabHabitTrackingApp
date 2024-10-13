package ru.parfenov.homework_1.server.pages.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.parfenov.homework_1.server.model.Habit;
import ru.parfenov.homework_1.server.model.User;
import ru.parfenov.homework_1.server.pages.UserMenuPage;
import ru.parfenov.homework_1.server.service.HabitService;

import java.io.IOException;

/**
 * Зайдя на эту страницу, клиент увидит все свои привычки, которые необходимо выполнить сегодня
 */
@Slf4j
@RequiredArgsConstructor
public class YourHabitsTodayPage implements UserMenuPage {
    private final User user;
    private final HabitService habitService;
    @Override
    public void run() throws IOException, InterruptedException {
        System.out.println("Hello dear!!!!" + System.lineSeparator() + "It's your habits today :");
        for (Habit habit : habitService.todayPerforms(user)) {
            System.out.println(habit);
            System.out.println(habitService.remind(habit.getId()));
        }
    }
}