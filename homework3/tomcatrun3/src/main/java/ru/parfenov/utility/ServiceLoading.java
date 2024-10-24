package ru.parfenov.utility;

import ru.parfenov.repository.UserRepository;
import ru.parfenov.repository.impl.UserRepositoryJdbcImpl;
import ru.parfenov.service.UserService;
import ru.parfenov.service.impl.UserServiceConsoleImpl;

public class ServiceLoading {
    public static UserService loadUserservice() throws Exception {
        UserRepository store = new UserRepositoryJdbcImpl();
        return new UserServiceConsoleImpl(store);
    }
}