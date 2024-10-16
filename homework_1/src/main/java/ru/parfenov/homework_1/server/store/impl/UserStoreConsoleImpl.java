package ru.parfenov.homework_1.server.store.impl;

import lombok.extern.slf4j.Slf4j;
import ru.parfenov.homework_1.server.enums.user.Role;
import ru.parfenov.homework_1.server.model.User;
import ru.parfenov.homework_1.server.store.UserStore;
import ru.parfenov.homework_1.server.utility.Utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
public class UserStoreConsoleImpl implements UserStore {
    private static int userId = 0;
    private final Map<Integer, User> userMap = new HashMap<>();

    public UserStoreConsoleImpl() {
        User user = new User(1, Utility.adminEmail, Utility.adminPassword, Utility.adminPassword, "ADMIN", Role.ADMIN, false);
        create(user);
    }
    @Override
    public User create(User user) {
        userId++;
        user.setId(userId);
        return userMap.put(user.getId(), user);
    }

    @Override
    public User findByEmail(String email) {
        User result = null;
        for (User user : userMap.values()) {
            if (email.equals(user.getEmail())) {
                result = user;
            }
        }
        return result;
    }

    @Override
    public User findById(int userId) {
        return userMap.get(userId);
    }

    @Override
    public User findByEmailAndPassword(String email, String password) {
        User user = findByEmail(email);
        return user == null ? null : (user.getPassword().equals(password) ? user : null);
    }

    @Override
    public User update(User user) {
        return userMap.replace(user.getId(), user);
    }

    @Override
    public User delete(int userId) {
        return userMap.remove(userId);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(userMap.values());
    }
}