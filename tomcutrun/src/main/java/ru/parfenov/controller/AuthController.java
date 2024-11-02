package ru.parfenov.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.parfenov.dto.user.UserSignUpDTO;
import ru.parfenov.model.User;
import ru.parfenov.service.UserService;

import java.util.Optional;

@Slf4j
@RestController
public class AuthController {
    private final UserService userService;

    @Autowired
    public AuthController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Страница регистрации.
     * Метод обработает HTTP запрос Post.
     * Пользователь вводит свои данные и регистрируется в БД
     * @param userDTO сущность User, обвёрнутая в DTO для подачи в виде Json
     * @return ответ сервера
     */
    @PostMapping("/sign-up")
    public ResponseEntity<User> signUp(@RequestBody UserSignUpDTO userDTO) {
        Optional<User> userOptional =
                userService.createByReg(
                        userDTO.getEmail(),
                        userDTO.getPassword(),
                        userDTO.getName()
                );
        return userOptional
                .map(user -> new ResponseEntity<>(user, HttpStatus.CREATED))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }
}