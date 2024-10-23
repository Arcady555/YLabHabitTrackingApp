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
import java.util.Optional;

/**
 * Страница, где клиент после ввода ID привычки сообщил приложению, что он совершил текущее выполнение
 */
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
                System.out.println(habitService.perform(habit) ? "Habit is performed!" : "Habit is not performed!");
            }
        } else {
            System.out.println("Habit is not exist!" + System.lineSeparator());
        }
    }
}
