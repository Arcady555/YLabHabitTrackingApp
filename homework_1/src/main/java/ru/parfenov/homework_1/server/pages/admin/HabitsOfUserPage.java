package ru.parfenov.homework_1.server.pages.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.parfenov.homework_1.server.model.Habit;
import ru.parfenov.homework_1.server.model.User;
import ru.parfenov.homework_1.server.pages.UserMenuPage;
import ru.parfenov.homework_1.server.service.HabitService;
import ru.parfenov.homework_1.server.service.UserService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;

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
    }
}
