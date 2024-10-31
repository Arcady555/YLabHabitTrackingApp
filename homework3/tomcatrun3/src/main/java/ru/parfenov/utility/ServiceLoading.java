package ru.parfenov.utility;

import ru.parfenov.repository.HabitRepository;
import ru.parfenov.repository.LogRepository;
import ru.parfenov.repository.UserRepository;
import ru.parfenov.repository.impl.HabitRepositoryJdbcImpl;
import ru.parfenov.repository.impl.LogRepositoryJdbcImpl;
import ru.parfenov.repository.impl.UserRepositoryJdbcImpl;
import ru.parfenov.service.HabitService;
import ru.parfenov.service.LogService;
import ru.parfenov.service.UserService;
import ru.parfenov.service.impl.HabitServiceServletImpl;
import ru.parfenov.service.impl.LogServiceServletImpl;
import ru.parfenov.service.impl.UserServiceServletImpl;

public class ServiceLoading {

    private ServiceLoading() {
    }

    public static UserRepository repository;

    static {
        try {
            repository = new UserRepositoryJdbcImpl();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static UserService loadUserService() throws Exception {
        return new UserServiceServletImpl(repository);
    }

    public static HabitService loadHabitService() throws Exception {
        HabitRepository habitRepository = new HabitRepositoryJdbcImpl(repository);
        return new HabitServiceServletImpl(habitRepository);
    }

    public static LogService loadLogService() throws Exception {
        LogRepository logRepository = new LogRepositoryJdbcImpl();
        return new LogServiceServletImpl(logRepository);
    }
}