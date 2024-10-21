package ru.parfenov.homework_2.server;

import ru.parfenov.homework_2.server.email_send.cases.RemindViaEmail;
import ru.parfenov.homework_2.server.pages.StartPage;
import ru.parfenov.homework_2.server.repository.HabitRepository;
import ru.parfenov.homework_2.server.repository.UserRepository;
import ru.parfenov.homework_2.server.repository.impl.HabitRepositoryJdbcImpl;
import ru.parfenov.homework_2.server.repository.impl.UserRepositoryJdbcImpl;
import ru.parfenov.homework_2.server.service.HabitService;
import ru.parfenov.homework_2.server.service.UserService;
import ru.parfenov.homework_2.server.service.impl.HabitServiceConsoleImpl;
import ru.parfenov.homework_2.server.service.impl.UserServiceConsoleImpl;
import ru.parfenov.homework_2.server.utility.LiquibaseUpdate;

public class ServerClass {

    /**
     * Запуск хранилищ и сервисов
     * Вывод стартовой страницы
     * Запуск класса, отвечающего за ежедневную рассылку напоминаний на емайлы
     */
    public void run() throws Exception {
        LiquibaseUpdate liquibaseUpdate = new LiquibaseUpdate();
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