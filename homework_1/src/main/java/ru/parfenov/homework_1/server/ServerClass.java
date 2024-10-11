package ru.parfenov.homework_1.server;

import ru.parfenov.homework_1.server.pages.StartPage;
import ru.parfenov.homework_1.server.service.HabitService;
import ru.parfenov.homework_1.server.service.UserService;
import ru.parfenov.homework_1.server.service.impl.HabitServiceConsoleImpl;
import ru.parfenov.homework_1.server.service.impl.UserServiceConsoleImpl;
import ru.parfenov.homework_1.server.store.HabitStore;
import ru.parfenov.homework_1.server.store.UserStore;
import ru.parfenov.homework_1.server.store.impl.HabitStoreConsoleImpl;
import ru.parfenov.homework_1.server.store.impl.UserStoreConsoleImpl;

import java.io.IOException;

public class ServerClass {
    /**
     * Запуск хранилищ и сервисов
     * Вывод стартовой страницы
     */
    public void run() throws IOException, InterruptedException {

        UserStore userStore = new UserStoreConsoleImpl();
        UserService userService = new UserServiceConsoleImpl(userStore);

        HabitStore habitStore = new HabitStoreConsoleImpl();
        HabitService habitService = new HabitServiceConsoleImpl(habitStore);

        StartPage page = new StartPage(userService, habitService);
        page.run();
    }
}