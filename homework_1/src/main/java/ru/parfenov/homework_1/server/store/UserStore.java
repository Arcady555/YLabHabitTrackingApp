package ru.parfenov.homework_1.server.store;

import ru.parfenov.homework_1.server.model.User;

import java.util.List;

public interface UserStore {
    User create(User user);

    User findByEmail(String email);

    User findById(int userId);

    User findByEmailAndPassword(String email, String password);

    User update(User user);

    User delete(int userId);

    List<User> findAll();
}