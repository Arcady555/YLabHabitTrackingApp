package ru.parfenov.homework_1.server.pages.admin;

import lombok.RequiredArgsConstructor;
import ru.parfenov.homework_1.server.model.User;
import ru.parfenov.homework_1.server.pages.UserMenuPage;
import ru.parfenov.homework_1.server.service.UserService;

@RequiredArgsConstructor
public class AllUsersPage implements UserMenuPage {
    private final UserService service;

    @Override
    public void run() {
        for (User user : service.findAll()) {
            System.out.println(user.toString());
        }
    }
}