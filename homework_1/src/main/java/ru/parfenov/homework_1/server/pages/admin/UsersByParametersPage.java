package ru.parfenov.homework_1.server.pages.admin;

import lombok.RequiredArgsConstructor;
import ru.parfenov.homework_1.server.enums.user.Role;
import ru.parfenov.homework_1.server.model.User;
import ru.parfenov.homework_1.server.pages.UserMenuPage;
import ru.parfenov.homework_1.server.service.UserService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@RequiredArgsConstructor
public class UsersByParametersPage implements UserMenuPage {
    private final UserService service;
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    @Override
    public void run() throws IOException {
        System.out.println("Enter user role 0 - ADMIN,  1 - CLIENT");
        String answer = reader.readLine();
        Role role;
        switch (answer) {
            case "0" -> role = Role.ADMIN;
            case "2" -> role = Role.CLIENT;
            default -> {
                role = null;
                System.out.println("Please enter correct" + System.lineSeparator());
                run();
            }
        }

        System.out.println("Enter user name");
        String name = reader.readLine();

        System.out.println(
                "Enter the block of the user" + System.lineSeparator() + "0 - true, 1 - false, another key - not parameter"
        );
        String answerBlock = reader.readLine();
        String block = answerBlock.equals("0") ? "true" : (answerBlock.equals("1") ? "false" : "");

        System.out.println("Enter habit (part of the text in the name or description)");
        String habit = reader.readLine();

        for (User user : service.findByParameters(role, name, block, habit)) {
            System.out.println(user);
        }
    }
}