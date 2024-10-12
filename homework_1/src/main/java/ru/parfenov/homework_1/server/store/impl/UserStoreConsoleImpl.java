package ru.parfenov.homework_1.server.store.impl;

import lombok.extern.slf4j.Slf4j;
import ru.parfenov.homework_1.server.enums.user.Role;
import ru.parfenov.homework_1.server.model.Habit;
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
        User user = new User(1, Utility.adminEmail, Utility.adminPassword, "ADMIN", Role.ADMIN, false, List.of());
        create(user);
    }
    @Override
    public void create(User user) {
        userId++;
        user.setId(userId);
        userMap.put(user.getId(), user);
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

    @Override
    public List<User> findByParameters(Role role, String name, String block, String habit) {
        List<User> allUsers = findAll();
        List<User> result = new ArrayList<>();
        for (User user : allUsers) {
            if (select (user, role, name, block, habit)) {
                result.add(user);
            }
        }
        return result;
    }

    private boolean select(User user, Role role, String name, String block, String habit) {
        boolean check1 = role == null || user.getRole().equals(role);
        boolean check2 = name.isEmpty() || user.getName().contains(name);
        boolean check3 = block.isEmpty() ||
                (block.equals("true") && user.isBlocked()) ||
                (block.equals("false") && !user.isBlocked());
        boolean check4 = habit.isEmpty() || checkHabit(user, habit);

        return check1 && check2 && check3 && check4;
    }

    private boolean checkHabit (User user, String habitStr) {
        boolean result = false;
        List<Habit> habits = user.getHabits();
        for (Habit habit : habits) {
            if (habit.getName().contains(habitStr)) {
                result = true;
                break;
            }
            if (habit.getDescription().contains(habitStr)) {
                result = true;
                break;
            }
        }
        return  result;
    }
}