package ru.parfenov.server.pages.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.parfenov.server.model.User;
import ru.parfenov.server.pages.UserMenuPage;
import ru.parfenov.server.service.UserService;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

/**
 * Страница, где админ может вывести список юзеров, введя параметры, под которые они подпадают
 */
@Slf4j
@RequiredArgsConstructor
public class UsersByParametersPage implements UserMenuPage {
    private final UserService service;
    BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));

    @Override
    public void run() throws IOException {
        System.out.println("Enter user role 0 - ADMIN,  1 - CLIENT, another key - not parameter");
        String answerRole = reader.readLine();
        String role = answerRole.equals("0") ? "ADMIN" : (answerRole.equals("1") ? "CLIENT" : null);

        System.out.println("Enter user name");
        String name = reader.readLine();

        System.out.println(
                "Enter the block of the user" + System.lineSeparator() + "0 - true, 1 - false, another key - not parameter"
        );
        String answerBlock = reader.readLine();
        String block = answerBlock.equals("0") ? "true" : (answerBlock.equals("1") ? "false" : "");

        for (User user : service.findByParameters(role, name, block)) {
            System.out.println(user);
        }
    }
}