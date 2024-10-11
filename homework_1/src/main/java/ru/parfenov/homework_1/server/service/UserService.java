package ru.parfenov.homework_1.server.service;

import ru.parfenov.homework_1.server.enums.user.Role;
import ru.parfenov.homework_1.server.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    void createByReg(String email, String password, String name);

    Optional<User> findByEmail(String email);

    Optional<User> findById(int userId);

    Optional<User> findByEmailAndPassword(String email, String password);

    List<User> findAll();

    boolean delete(User user);

    User update(User user);

    List<User> findByParameters(Role role, String name, String block, String habit);
}
