package ru.parfenov.homework_1.server.store;

import ru.parfenov.homework_1.server.enums.user.Role;
import ru.parfenov.homework_1.server.model.User;

import java.util.List;

public interface UserStore {
    void create(User user);

    User findByEmail(String email);

    User findById(int userId);

    User findByEmailAndPassword(String email, String password);

    User update(User user);

    User delete(int userId);

    List<User> findAll();

    /**
     * * Метод предполагает поиск по параметрам (всем или некоторые можно не указать)
     * id юзера, его роль, имя, строка(может содержаться в контактной информации), число покупок
     */
    List<User> findByParameters(Role role, String name, String block, String habit);
}