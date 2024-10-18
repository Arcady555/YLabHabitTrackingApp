package ru.parfenov.homework_2.server.pages.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.parfenov.homework_2.server.model.Habit;
import ru.parfenov.homework_2.server.model.User;
import ru.parfenov.homework_2.server.pages.UserMenuPage;
import ru.parfenov.homework_2.server.service.HabitService;
import ru.parfenov.homework_2.server.service.UserService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;

/**
 * Страница, на которой админ может вывести список всех привычек нужного юзера.
 * При желании, может удалить некоторые привычки (если есть для этого причины(законодательство или политика сайта) )
 */
@Slf4j
@RequiredArgsConstructor
public class HabitsOfUserPage implements UserMenuPage {
    private final UserService service;
    private final HabitService habitService;
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    @Override
    public void run() throws IOException, InterruptedException {
        System.out.println("Enter the email of the user");
        String email = reader.readLine();
        Optional<User> userOptional = service.findByEmail(email);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            for (Habit habit : habitService.findByUser(user)) {
                System.out.println(habit);
            }
        }
        System.out.println("Do You wont to delete some habit? 0 - YES,  another key - NO");
        if (reader.readLine().equals("0")) {
            System.out.println("Enter habit ID");
            String answerId = reader.readLine();
            long habitId;
            try {
                habitId = Long.parseLong(answerId);
                System.out.println(habitService.delete(habitId) ? "The habit is deleted!" : "The habit is NOT deleted!");
            } catch (NumberFormatException e) {
                log.error("Please enter the NUMBER!", e);
                System.out.println(System.lineSeparator());
                run();
            }
        }
    }
}