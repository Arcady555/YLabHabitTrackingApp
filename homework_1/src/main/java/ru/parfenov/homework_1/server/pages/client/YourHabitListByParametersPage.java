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

/**
 * Страница, где клиент может вывести отсортированный по параметрам список своих привычек,
 * если их уже стало так много
 */
@Slf4j
@RequiredArgsConstructor
public class YourHabitListByParametersPage implements UserMenuPage {
    private final User user;
    private final HabitService habitService;
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    @Override
    public void run() throws IOException, InterruptedException {
        System.out.println(
                "Enter usefulness" + System.lineSeparator() + "0 - true, 1 - false, another key - not parameter"
        );
        String answerUseful = reader.readLine();;
        String usefulness = answerUseful.equals("0") ? "true" : (answerUseful.equals("1") ? "false" : "");

        System.out.println("Enter active");
        String answerActive = reader.readLine();;
        String active = answerActive.equals("0") ? "true" : (answerActive.equals("1") ? "false" : "");

        System.out.println("Enter part of the text in the name");
        String name = reader.readLine();

        System.out.println("Enter part of the text in the description");
        String description = reader.readLine();

        System.out.println("Enter dateOfCreate");
        String dateOfCreate = reader.readLine();

        System.out.println("Enter frequency");
        String frequency = reader.readLine();

        for (Habit habit : habitService.findByParameters(user, usefulness, active, name, description, dateOfCreate, frequency)) {
            System.out.println(habit);
            System.out.println(habitService.remind(habit.getId()));
        }
    }
}