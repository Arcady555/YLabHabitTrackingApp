package ru.parfenov.homework_1.server.pages.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.parfenov.homework_1.server.model.Habit;
import ru.parfenov.homework_1.server.model.User;
import ru.parfenov.homework_1.server.pages.UserMenuPage;
import ru.parfenov.homework_1.server.service.HabitService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

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
            System.out.println("Please enter correct format!");
            run();
        }

        System.out.println("Enter date to");
        LocalDate dateTo  = LocalDate.now();
        try {
            dateFrom = LocalDate.parse(reader.readLine());
        } catch (DateTimeParseException e) {
            System.out.println("Please enter correct format!");
            run();
        }

        for (Habit habit : habitService.findByUser(user)) {
            System.out.println(habit);
            System.out.println(habitService.statistic(habit.getId(), dateFrom, dateTo));
        }
    }
}