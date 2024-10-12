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
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class PerformHabitPage implements UserMenuPage {
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
            System.out.println("Please enter the NUMBER!" + System.lineSeparator());
            run();
        }
        Optional<Habit> optionalHabit = habitService.findById(habitId);
        if (optionalHabit.isPresent()) {
            if (!optionalHabit.get().getUser().equals(user)) {
                System.out.println("Habit is not Your!" + System.lineSeparator());
                run();
            } else {
                Habit habit = optionalHabit.get();
                System.out.println(habitService.perform(habit) ? "Habit is performed!" : "Habit is not performed!");
            }
        } else {
            System.out.println("Habit is not exist!" + System.lineSeparator());
        }
    }
}
