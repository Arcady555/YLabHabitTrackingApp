package ru.parfenov.homework_1.server.pages.client;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.parfenov.homework_1.server.model.Habit;
import ru.parfenov.homework_1.server.model.User;
import ru.parfenov.homework_1.server.pages.UserMenuPage;
import ru.parfenov.homework_1.server.service.HabitService;
import ru.parfenov.homework_1.server.utility.Utility;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.time.Period;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class UpdateHabitPage implements UserMenuPage {
    private final User user;
    private final HabitService habitService;
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    @Override
    public void run() throws IOException, InterruptedException {
        System.out.println("Enter habit ID");
        String answerId = reader.readLine();
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
                Habit habit = optionalHabit.get();
                System.out.println(habit + System.lineSeparator());

                System.out.println("Do You wont to delete the habit? 0 - YES,  another key - NO");
                if (reader.readLine().equals("0")) {
                    System.out.println(habitService.delete(habitId) ? "The habit is deleted!" : "The habit is NOT deleted!");
                    run();
                }

                System.out.println("Do you want to change usefulness?" + System.lineSeparator() + "0 - yes, another key - no");
                boolean usefulness = reader.readLine().equals("0") != habit.isUseful();

                System.out.println("Do you want to change active?" + System.lineSeparator() + "0 - yes, another key - no");
                boolean active = reader.readLine().equals("0") != habit.isActive();

                System.out.println("Do you want to change name?" + System.lineSeparator() + "0 - yes, another key - no");
                String name = habit.getName();
                if (reader.readLine().equals("0")) {
                    System.out.println("Enter new name");
                    name = reader.readLine();
                }

                System.out.println("Do you want to change description?" + System.lineSeparator() + "0 - yes, another key - no");
                String description = habit.getDescription();
                if (reader.readLine().equals("0")) {
                    System.out.println("Enter new description");
                    description = reader.readLine();
                }

                System.out.println("Do you want to change frequency?" + System.lineSeparator() + "0 - yes, another key - no");
                Period frequency = habit.getFrequency();
                if (reader.readLine().equals("0")) {
                    System.out.println("Enter new frequency");
                    frequency = Utility.enterFrequency(reader);
                    if (frequency == null) run();
                }

                System.out.println(habitService.update(habit, usefulness, active, name, description, frequency) ?
                        "The habit is updated!" : "The habit is NOT updated!");
                System.out.println(habitService.findById(habitId));
            }
        } else {
            System.out.println("Habit is not exist!" + System.lineSeparator());
        }
    }
}