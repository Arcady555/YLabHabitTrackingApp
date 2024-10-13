package ru.parfenov.homework_1.server.service;

import ru.parfenov.homework_1.server.model.User;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User createByReg(String email, String password, String name);

    Optional<User> findByEmail(String email);

    Optional<User> findById(int userId);

    Optional<User> findByEmailAndPassword(String email, String password);

    List<User> findAll();

    boolean delete(User user);

    User update(User user);

    /**
     * Метод предполагает поиск по параметрам (всем или некоторые можно не указать)
     * @param role
     * @param name
     * @param block
     * @return
     */
    List<User> findByParameters(String role, String name, String block);
}
