package ru.parfenov.homework_1.server.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.parfenov.homework_1.server.enums.user.Role;
import ru.parfenov.homework_1.server.model.User;
import ru.parfenov.homework_1.server.service.UserService;
import ru.parfenov.homework_1.server.store.UserStore;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class UserServiceConsoleImpl implements UserService {
    private final UserStore store;

    @Override
    public User createByReg(String email, String password, String name) {
        User user = new User(0, email, password, name, Role.CLIENT, false);
        return store.create(user);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(store.findByEmail(email));
    }

    @Override
    public Optional<User> findById(int userId) {
        return Optional.ofNullable(store.findById(userId));
    }

    @Override
    public Optional<User> findByEmailAndPassword(String email, String password) {
        return Optional.ofNullable(store.findByEmailAndPassword(email, password));
    }

    @Override
    public List<User> findAll() {
        return store.findAll();
    }

    @Override
    public boolean delete(User user) {
        store.delete(user.getId());
        return findById(user.getId()).isEmpty();
    }

    @Override
    public User update(User user) {
        return store.update(user);
    }

    @Override
    public List<User> findByParameters(String role, String name, String block) {
        List<User> result = new ArrayList<>();
        for (User user : findAll()) {
            if (select (user, role, name, block)) {
                result.add(user);
            }
        }
        return result;
    }

    private boolean select(User user, String roleStr, String name, String block) {
        Role role = roleStr.equals("ADMIN") ? Role.ADMIN : (roleStr.equals("CLIENT") ? Role.CLIENT : null);
        boolean check1 = role == null || user.getRole().equals(role);
        boolean check2 = name.isEmpty() || user.getName().contains(name);
        boolean check3 = block.isEmpty() ||
                (block.equals("true") && user.isBlocked()) ||
                (block.equals("false") && !user.isBlocked());

        return check1 && check2 && check3;
    }
}