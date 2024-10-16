package ru.parfenov.homework_1.server.pages.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.parfenov.homework_1.server.model.User;
import ru.parfenov.homework_1.server.pages.UserMenuPage;
import ru.parfenov.homework_1.server.service.HabitService;
import ru.parfenov.homework_1.server.utility.Utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeParseException;

/**
 * Страница, где клиент создаст свою привычку
 */
@Slf4j
@RequiredArgsConstructor
public class CreateHabitPage implements UserMenuPage {
    private final User user;
    private final HabitService habitService;
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    @Override
    public void run() throws IOException {
        System.out.println("Will the habit useful for You?  0 - YES,  another key - NO");
        String answerUseful = reader.readLine();
        boolean usefulness = answerUseful.equals("0");

        System.out.println("Enter name of habit");
        String name = reader.readLine();

        System.out.println("Enter description of habit");
        String description = reader.readLine();

        System.out.println("Enter date of first perform(format 2024-10-15)");
        LocalDate firstPerform = LocalDate.now();
        try {
            firstPerform = LocalDate.parse(reader.readLine());
        } catch (DateTimeParseException e) {
            log.error("Please enter correct format!", e);
            System.out.println(System.lineSeparator());
            run();
        }
        if (firstPerform.isBefore(LocalDate.now())) {
            System.out.println("Please enter correct!");
            run();
        }

        System.out.println("Enter frequency of performing of the habit (days amount)");

        Period frequency = Utility.enterFrequency(reader);
        if (frequency == null) run();

        System.out.println(habitService
                .create(user, usefulness, name, description, LocalDate.now(), firstPerform, frequency)
        );
    }
}