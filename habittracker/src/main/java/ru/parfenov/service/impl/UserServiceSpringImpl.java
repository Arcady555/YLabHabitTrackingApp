package ru.parfenov.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.parfenov.dto.user.UserGeneralDTO;
import ru.parfenov.dto.user.UserUpdateDTO;
import ru.parfenov.dto.user.mapper.UserDTOMapper;
import ru.parfenov.enums.user.Role;
import ru.parfenov.model.User;
import ru.parfenov.repository.UserRepository;
import ru.parfenov.service.UserService;
import ru.parfenov.utility.Utility;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceSpringImpl implements UserService {
    private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final UserDTOMapper userDTOMapper;

    /**
     * (Если емайл юзера не уникален, уже зарегистрирован в приложении, то repository не сможет его
     * сохранить, выкинется SQL- исключение, т.к. колонка емайл в БД - unique="true"
     *
     * @param email    емайл юзера
     * @param password его пароль
     * @param name     его имя
     * @return модель юзера в Optional
     */
    @Override
    public Optional<UserGeneralDTO> createByReg(String email, String password, String name) {
        Optional<UserGeneralDTO> result = Optional.empty();
        if (Utility.validationEmail(email)) {
            String resetPassword = Utility.generateForResetPassword();
            User user = repository.save(new User(0, email, passwordEncoder.encode(password), resetPassword, name, Role.CLIENT, false));
            if (findById(user.getId()).isPresent()) result = Optional.of(userDTOMapper.toUserGeneralDTO(user));
        }
        return result;
    }

    @Override
    public Optional<UserGeneralDTO> findById(int userId) {
        Optional<User> userOptional = repository.findById(userId);
        return userOptional.map(userGet -> userDTOMapper.toUserGeneralDTO(userOptional.get()));
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return repository.findByEmail(email);
    }

    @Override
    public Optional<User> findByEmailAndPassword(String email, String password) {
        return repository.findByEmailAndPassword(email, password);
    }

    @Override
    public List<UserGeneralDTO> findAll() {
        return userDTOMapper.toUserGeneralDTOList(repository.findAll());
    }

    @Override
    public List<User> findAllForMail() {
        return repository.findAll();
    }

    @Override
    @Transactional
    public boolean delete(int userId) {
        boolean result = false;
        Optional<User> user = repository.findById(userId);
        if (user.isPresent()) {
            repository.delete(user.get());
            result = findById(userId).isEmpty();
        }
        return result;
    }

    @Override
    public Optional<UserGeneralDTO> update(UserUpdateDTO userDTO, String resetPass) {
        User user = null;

        int userId = userDTO.getId();
        String newPassword = userDTO.getPassword();
        String newName = userDTO.getName();
        String newRoleStr = userDTO.getRole();
        Role newRole = Utility.getUserRoleFromString(newRoleStr);
        String newBlock = userDTO.getBlocked();

        Optional<User> newUserOptional = repository.findById(userId);
        if (newUserOptional.isPresent()) {
            User newUser = newUserOptional.get();
            if (!newPassword.isEmpty()) newUser.setPassword(newPassword);
            if (!newName.isEmpty()) newUser.setName(newName);
            if (!newBlock.isEmpty()) newUser.setBlocked("true".equals(newBlock));
            if (newRole != null) newUser.setRole(newRole);
            user = repository.save(newUser);
        }

        return user != null && checkUpdate(user, newPassword, resetPass, newName, newRoleStr, newBlock) ?
                Optional.of(userDTOMapper.toUserGeneralDTO(user)) :
                Optional.empty();
    }

    @Override
    public Optional<UserGeneralDTO> updatePass(HttpServletRequest request, String newPassword, String resetPassword) {
        Optional<UserGeneralDTO> result = Optional.empty();
        Optional<User> userOptional = findByEmail(Utility.getUserEmail());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (resetPassword.equals(user.getResetPassword())) {
                String newResetPassword = Utility.generateForResetPassword();
                UserUpdateDTO userDTO = new UserUpdateDTO(user.getId(), newPassword, "", "", "");
                result = update(userDTO, newResetPassword);
            }
        }
        return result;
    }

    @Override
    public List<UserGeneralDTO> findByParameters(String role, String name, String blockStr) {
        boolean block = "true".equals(blockStr);
        return userDTOMapper.toUserGeneralDTOList(repository.findByParameters(role, name, blockStr, block));
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