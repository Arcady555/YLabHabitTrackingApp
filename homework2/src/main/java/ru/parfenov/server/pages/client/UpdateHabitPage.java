package ru.parfenov.server.pages.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.parfenov.server.model.Habit;
import ru.parfenov.server.model.User;
import ru.parfenov.server.pages.UserMenuPage;
import ru.parfenov.server.service.HabitService;
import ru.parfenov.server.utility.Utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;

/**
 * Страница, где клиент может изменить данные по своей привычке
 */
@Slf4j
@RequiredArgsConstructor
public class UpdateHabitPage implements UserMenuPage {
    private final User user;
    private final HabitService habitService;
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    @Override
    public void run() throws IOException, InterruptedException {
        System.out.println("Enter habit ID or exit");
        String answerId = reader.readLine();
        if (answerId.equals("exit")) return;
        long habitId = 0L;
        try {
            habitId = Long.parseLong(answerId);
        } catch (NumberFormatException e) {
            log.error("Please enter the NUMBER!!", e);
            System.out.println(System.lineSeparator());
            run();
        }
        Optional<Habit> optionalHabit = habitService.findById(habitId);
        if (optionalHabit.isPresent()) {
            if (!optionalHabit.get().getUser().equals(user)) {
                System.out.println("Habit is not Your!" + System.lineSeparator());
                run();
            } else {
                String newUsefulness = "";
                String newActive = "";
                String newName = "";
                String newDescription = "";
                String newFrequency = "";
                Habit habit = optionalHabit.get();
                System.out.println(habit + System.lineSeparator());

                System.out.println("Do You wont to delete the habit? 0 - YES,  another key - NO");
                if (reader.readLine().equals("0")) {
                    System.out.println(habitService.delete(habitId) ? "The habit is deleted!" : "The habit is NOT deleted!");
                    run();
                }

                System.out.println("Do you want to change usefulness?" + System.lineSeparator() + "0 - yes, another key - no");
                newUsefulness = habit.isUseful() ? "false" : "true";

                System.out.println("Do you want to change active?" + System.lineSeparator() + "0 - yes, another key - no");
                if (reader.readLine().equals("0")) {
                    newActive = habit.isActive() ? "false" : "true";;
                }

                System.out.println("Do you want to change name?" + System.lineSeparator() + "0 - yes, another key - no");
                if (reader.readLine().equals("0")) {
                    System.out.println("Enter new name");
                    newName = reader.readLine();
                }

                System.out.println("Do you want to change description?" + System.lineSeparator() + "0 - yes, another key - no");
                if (reader.readLine().equals("0")) {
                    System.out.println("Enter new description");
                    newDescription = reader.readLine();
                }

                System.out.println("Do you want to change frequency?" + System.lineSeparator() + "0 - yes, another key - no");
                if (reader.readLine().equals("0")) {
                    System.out.println("Enter new frequency");
                    newFrequency = reader.readLine();
                    if (Utility.getPeriodFromString(newFrequency) == null) run();
                }

                System.out.println(habitService
                        .updateByUser(habit.getId(), newUsefulness, newActive, newName, newDescription, newFrequency));
            }
        } else {
            System.out.println("Habit is not exist!" + System.lineSeparator());
            run();
        }
    }
}