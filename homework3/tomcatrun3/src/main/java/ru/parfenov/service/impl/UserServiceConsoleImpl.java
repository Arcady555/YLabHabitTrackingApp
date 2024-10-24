package ru.parfenov.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.parfenov.enums.user.Role;
import ru.parfenov.model.User;
import ru.parfenov.repository.UserRepository;
import ru.parfenov.service.UserService;
import ru.parfenov.utility.Utility;

import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class UserServiceConsoleImpl implements UserService {
    private final UserRepository repository;

    @Override
    public Optional<User> createByReg(String email, String password, String name) {
        String resetPassword = Utility.generateForResetPassword();
        return Optional.ofNullable(repository.create(new User(0, email, password, resetPassword, name, Role.CLIENT, false)));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(repository.findByEmail(email));
    }

    @Override
    public Optional<User> findById(int userId) {
        return Optional.ofNullable(repository.findById(userId));
    }

    @Override
    public Optional<User> findByEmailAndPassword(String email, String password) {
        return Optional.ofNullable(repository.findByEmailAndPassword(email, password));
    }

    @Override
    public List<User> findAll() {
        return repository.findAll();
    }

    @Override
    public boolean delete(User user) {
        repository.delete(user.getId());
        return findById(user.getId()).isEmpty();
    }

    @Override
    public User update(int userId, String newPassword, String newResetPassword, String newName, Role newUserRole, String blocked) {
        return repository.update(userId, newPassword, newResetPassword, newName, newUserRole, blocked);
    }

    @Override
    public List<User> findByParameters(String role, String name, String block) {
        return repository.findByParameters(role, name, block);
    }
}