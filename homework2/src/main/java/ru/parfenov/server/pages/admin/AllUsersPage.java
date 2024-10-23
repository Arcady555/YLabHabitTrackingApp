package ru.parfenov.server.pages.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.parfenov.server.model.User;
import ru.parfenov.server.pages.UserMenuPage;
import ru.parfenov.server.service.UserService;

/**
 * Страница вывода всех юзеров из хранилища
 */
@Slf4j
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