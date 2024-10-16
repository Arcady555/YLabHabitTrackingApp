package ru.parfenov.homework_1.server;

import ru.parfenov.homework_1.server.email_send.cases.RemindViaEmail;
import ru.parfenov.homework_1.server.pages.StartPage;
import ru.parfenov.homework_1.server.service.HabitService;
import ru.parfenov.homework_1.server.service.UserService;
import ru.parfenov.homework_1.server.service.impl.HabitServiceConsoleImpl;
import ru.parfenov.homework_1.server.service.impl.UserServiceConsoleImpl;
import ru.parfenov.homework_1.server.store.HabitStore;
import ru.parfenov.homework_1.server.store.UserStore;
import ru.parfenov.homework_1.server.store.impl.HabitStoreConsoleImpl;
import ru.parfenov.homework_1.server.store.impl.UserStoreConsoleImpl;

import javax.mail.MessagingException;
import java.io.IOException;

public class ServerClass {
    /**
     * Запуск хранилищ и сервисов
     * Вывод стартовой страницы
     */
    public void run() throws IOException, InterruptedException, MessagingException {

        UserStore userStore = new UserStoreConsoleImpl();
        UserService userService = new UserServiceConsoleImpl(userStore);

        HabitStore habitStore = new HabitStoreConsoleImpl();
        HabitService habitService = new HabitServiceConsoleImpl(habitStore);

        StartPage page = new StartPage(userService, habitService);
        RemindViaEmail remindViaEmail = new RemindViaEmail(userService, habitService);
        page.run();
        remindViaEmail.run();
    }
}