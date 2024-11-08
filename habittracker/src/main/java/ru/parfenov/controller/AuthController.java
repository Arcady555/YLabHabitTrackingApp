package ru.parfenov.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import ru.parfenov.dto.user.UserGeneralDTO;
import ru.parfenov.dto.user.UserSignUpDTO;
import ru.parfenov.model.User;
import ru.parfenov.security.JwtToken;
import ru.parfenov.security.UserDetailsServiceImpl;
import ru.parfenov.service.UserService;

import java.util.Optional;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtToken jwtToken;

    /**
     * Страница регистрации.
     * Метод обработает HTTP запрос Post.
     * Пользователь вводит свои данные и регистрируется в БД
     *
     * @param userDTO сущность User, обвёрнутая в DTO для подачи в виде Json
     * @return ответ сервера
     */
    @PostMapping("/sign-up")
    public ResponseEntity<UserGeneralDTO> signUp(@RequestBody UserSignUpDTO userDTO) {
         Optional<UserGeneralDTO> userOptional =
                userService.createByReg(
                        userDTO.getEmail(),
                        userDTO.getPassword(),
                        userDTO.getName()
                );
        return userOptional
                .map(user -> new ResponseEntity<>(user, HttpStatus.CREATED))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }

    /**
     * Страница получения токена.
     * Метод обработает HTTP запрос Post.
     * Пользователь вводит свои данные(ID и пароль) и получает токен,
     * который он вставляет в заголовок в дальнейших запросах.
     * Которые требуют авторизацию
     *
     * @param userDTO сущность User, обвёрнутая в DTO для подачи в виде Json
     * @return ответ сервера
     */
    @PostMapping("/sign-in")
    public ResponseEntity<?> signIn(@RequestBody UserSignUpDTO userDTO) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(userDTO.getEmail(), userDTO.getPassword())
            );
        } catch (AuthorizationServiceException e) {
            log.error("Неправильный ID или пароль", e);
            return new ResponseEntity<>("Неправильный ID или пароль", HttpStatus.UNAUTHORIZED);
        }
        UserDetails userDetails = userDetailsService.loadUserByUsername(userDTO.getEmail());
        String token = jwtToken.generateToken(userDetails);
        return ResponseEntity.ok(token);
    }
}