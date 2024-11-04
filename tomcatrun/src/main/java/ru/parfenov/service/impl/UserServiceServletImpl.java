package ru.parfenov.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.parfenov.dto.user.UserUpdateDTO;
import ru.parfenov.enums.user.Role;
import ru.parfenov.model.User;
import ru.parfenov.repository.UserRepository;
import ru.parfenov.service.UserService;
import ru.parfenov.utility.Utility;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class UserServiceServletImpl implements UserService {
    private final UserRepository repository;

    @Autowired
    public UserServiceServletImpl(UserRepository repository) {
        this.repository = repository;
    }

    @Override
    public Optional<User> createByReg(String email, String password, String name) {
        Optional<User> result = Optional.empty();
        if (Utility.validationEmail(email)) {
            String resetPassword = Utility.generateForResetPassword();
            result = Optional.ofNullable(repository.create(new User(0, email, password, resetPassword, name, Role.CLIENT, false)));
        }
        return result;
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
    public List<User> findAllForMail() {
        return repository.findAll();
    }

    @Override
    public boolean delete(int userId) {
        boolean result = false;
        if (userId != 0) {
            repository.delete(userId);
            result = findById(userId).isEmpty();
        }
        return result;
    }

    @Override
    public Optional<User> update(UserUpdateDTO userDTO, String resetPass) {
        int userId = userDTO.getId();
        String newPassword = userDTO.getPassword();
        String newName = userDTO.getName();
        String newUserRoleStr = userDTO.getRole();
        String blocked = userDTO.getBlocked();
        Role newUserRole = Utility.getUserRoleFromString(userDTO.getRole());
        User user = repository.update(userId, newPassword, resetPass, newName, newUserRole, blocked);
        return user != null && checkUpdate(user, newPassword, resetPass, newName, newUserRoleStr, blocked) ?
                Optional.of(user) :
                Optional.empty();
    }

    @Override
    public Optional<User> updatePass(int userId, String newPassword, String resetPassword) {
        Optional<User> result = Optional.empty();
        User user = repository.findById(userId);
        if (user != null && resetPassword.equals(user.getResetPassword())) {
            String newResetPassword = Utility.generateForResetPassword();
            UserUpdateDTO userDTO = new UserUpdateDTO(userId, newPassword, "", "", "");
            result = update(userDTO, newResetPassword);
        }
        return result;
    }

    @Override
    public List<User> findByParameters(String role, String name, String block) {
        return repository.findByParameters(role, name, block);
    }

    private boolean checkUpdate(User user, String newPassword, String newResetPassword, String newName, String newUserRoleStr, String blocked) {
        Role role = Utility.getUserRoleFromString(newUserRoleStr);
        boolean check1 = newPassword.isEmpty() || user.getPassword().equals(newPassword);
        boolean check2 = newResetPassword.isEmpty() || user.getResetPassword().equals(newResetPassword);
        boolean check3 = newName.isEmpty() || user.getName().equals(newName);
        boolean check4 = role == null || user.getRole().equals(role);
        boolean check5 = blocked.isEmpty() ||
                (blocked.equals("true") && user.isBlocked()) ||
                (blocked.equals("false") && !user.isBlocked());

        return check1 && check2 && check3 && check4 && check5;
    }
}