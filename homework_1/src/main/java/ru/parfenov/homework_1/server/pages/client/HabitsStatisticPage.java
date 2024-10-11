package ru.parfenov.homework_1.server.pages.client;

import lombok.RequiredArgsConstructor;
import ru.parfenov.homework_1.server.model.Habit;
import ru.parfenov.homework_1.server.model.User;
import ru.parfenov.homework_1.server.pages.UserMenuPage;
import ru.parfenov.homework_1.server.service.HabitService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;

@RequiredArgsConstructor
public class HabitsStatisticPage implements UserMenuPage {
    private final User user;
    private final HabitService habitService;
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    @Override
    public void run() throws IOException, InterruptedException {
        System.out.println("Hello dear!!!!" + System.lineSeparator() + "Would You see your habits statistic ?");
        System.out.println("Enter date from");
        String dateFromStr = reader.readLine();
        LocalDate dateFrom = LocalDate.parse(dateFromStr);
        System.out.println("Enter date to");
        String dateToStr = reader.readLine();
        LocalDate dateTo = LocalDate.parse(dateToStr);
        for (Habit habit : habitService.findByUser(user)) {
            System.out.println(habit);
            System.out.println(habitService.statistic(habit.getId(), dateFrom, dateTo));
        }
    }
}
