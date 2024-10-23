package ru.parfenov.server;

import ru.parfenov.server.emailsend.cases.RemindViaEmail;
import ru.parfenov.server.pages.StartPage;
import ru.parfenov.server.repository.HabitRepository;
import ru.parfenov.server.repository.UserRepository;
import ru.parfenov.server.repository.impl.HabitRepositoryJdbcImpl;
import ru.parfenov.server.repository.impl.UserRepositoryJdbcImpl;
import ru.parfenov.server.service.HabitService;
import ru.parfenov.server.service.UserService;
import ru.parfenov.server.service.impl.HabitServiceConsoleImpl;
import ru.parfenov.server.service.impl.UserServiceConsoleImpl;
import ru.parfenov.server.utility.LiquibaseUpdate;
import ru.parfenov.server.utility.Utility;

public class ServerClass {

    /**
     * Запуск хранилищ и сервисов
     * Вывод стартовой страницы
     * Запуск класса, отвечающего за ежедневную рассылку напоминаний на емайлы
     */
    public void run() throws Exception {
        LiquibaseUpdate liquibaseUpdate = new LiquibaseUpdate(Utility.loadConnection());
        liquibaseUpdate.run();

        UserRepository userRepository = new UserRepositoryJdbcImpl();
        UserService userService = new UserServiceConsoleImpl(userRepository);

        HabitRepository habitRepository = new HabitRepositoryJdbcImpl(userRepository);
        HabitService habitService = new HabitServiceConsoleImpl(habitRepository);

        StartPage page = new StartPage(userService, habitService);
        RemindViaEmail remindViaEmail = new RemindViaEmail(userService, habitService);
        page.run();
        remindViaEmail.run();
    }
}