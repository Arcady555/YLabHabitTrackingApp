package ru.parfenov.homework_1.server.pages.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.parfenov.homework_1.server.model.User;
import ru.parfenov.homework_1.server.pages.UserMenuPage;
import ru.parfenov.homework_1.server.service.UserService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;

/**
 * Страница вывода инфы по юзеру
 */
@Slf4j
@RequiredArgsConstructor
public class UserPage implements UserMenuPage {
    private final UserService service;
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    @Override
    public void run() throws IOException {
        System.out.println("Enter user email");
        String userEmail = reader.readLine();
        Optional<User> userOptional = service.findByEmail(userEmail);
        System.out.println(userOptional.isPresent() ? userOptional.get() : "Not user with the email!");
    }
}