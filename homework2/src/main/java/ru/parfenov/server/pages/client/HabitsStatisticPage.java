package ru.parfenov.server.pages.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.parfenov.server.model.Habit;
import ru.parfenov.server.model.User;
import ru.parfenov.server.pages.UserMenuPage;
import ru.parfenov.server.service.HabitService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

/**
 *  Страница, где клиент может увидеть статистику по выполнению всех своих привычек
 *  Любой период(входящий в рамки). Достаточно ввести 2 даты - от и до
 *  Если какая-либо из привычек в рамки не влезает - там так и напишут
 */
@Slf4j
@RequiredArgsConstructor
public class HabitsStatisticPage implements UserMenuPage {
    private final User user;
    private final HabitService habitService;
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    @Override
    public void run() throws IOException, InterruptedException {
        System.out.println("Hello dear!!!!" + System.lineSeparator() + "Would You see your habits statistic ?");

        System.out.println("Enter date from");
        LocalDate dateFrom  = LocalDate.now();
        try {
            dateFrom = LocalDate.parse(reader.readLine());
        } catch (DateTimeParseException e) {
            log.error("Please enter correct format!", e);
            System.out.println(System.lineSeparator());
            run();
        }

        System.out.println("Enter date to");
        LocalDate dateTo  = LocalDate.now();
        try {
            dateFrom = LocalDate.parse(reader.readLine());
        } catch (DateTimeParseException e) {
            log.error("Please enter correct format!", e);
            System.out.println(System.lineSeparator());
            run();
        }

        for (Habit habit : habitService.findByUser(user)) {
            System.out.println(habit);
            System.out.println(habitService.statistic(habit.getId(), dateFrom, dateTo));
        }
    }
}