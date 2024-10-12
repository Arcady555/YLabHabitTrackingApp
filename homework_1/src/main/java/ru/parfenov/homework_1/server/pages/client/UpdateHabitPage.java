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

import static ru.parfenov.homework_1.server.utility.Utility.setPlannedNextPerform;

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

                System.out.println("Do you want to change usefulness?" + System.lineSeparator() + "0 - yes, another key - no");
                String answerUseful = reader.readLine();
                if (answerUseful.equals("0")) {
                    habit.setUsefulness(!habit.isUsefulness());
                }

                System.out.println("Do you want to change active?" + System.lineSeparator() + "0 - yes, another key - no");
                String answerActive = reader.readLine();
                if (answerActive.equals("0")) {
                    habit.setActive(!habit.isActive());
                }

                System.out.println("Do you want to change name?" + System.lineSeparator() + "0 - yes, another key - no");
                String answerName = reader.readLine();
                if (answerActive.equals("0")) {
                    habit.setName(answerName);
                }

                System.out.println("Do you want to change description?" + System.lineSeparator() + "0 - yes, another key - no");
                String answerDescription = reader.readLine();
                if (answerDescription.equals("0")) {
                    habit.setDescription(answerDescription);
                }

                System.out.println("Do you want to change frequency?" + System.lineSeparator() + "0 - yes, another key - no");
                String answerFrequency = reader.readLine();
                if (answerFrequency.equals("0")) {
                    Period frequency = Utility.enterFrequency(reader);
                    if (frequency == null) run();
                    habit.setFrequency(frequency);
                    habit.setPlannedNextPerform(setPlannedNextPerform(habit));
                }
                System.out.println(habitService.update(habit));
            }
        } else {
            System.out.println("Habit is not exist!" + System.lineSeparator());
        }
    }
}