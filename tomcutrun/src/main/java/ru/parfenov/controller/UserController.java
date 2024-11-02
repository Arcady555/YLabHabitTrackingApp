package ru.parfenov.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.parfenov.dto.user.UserUpdateDTO;
import ru.parfenov.dto.user.UserUpdatePassDTO;
import ru.parfenov.emailsend.cases.RemindViaEmail;
import ru.parfenov.emailsend.cases.ResetPasswordViaEmail;
import ru.parfenov.model.User;
import ru.parfenov.service.HabitService;
import ru.parfenov.service.UserService;
import ru.parfenov.utility.Utility;

import javax.mail.MessagingException;
import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;
    private final HabitService habitService;

    @Autowired
    public UserController(UserService userService, HabitService habitService) {
        this.userService = userService;
        this.habitService = habitService;
    }

    /**
     * Страница вывода юзера по введённому id
     * Данный метод, доступный только админу(через фильтр сервлетов),
     *
     * @param userId ID юзера
     * @return ответ сервера
     */
    @GetMapping("/user/{userId}")
    public ResponseEntity<User> viewUser(@PathVariable int userId) {
        Optional<User> userOptional = userService.findById(userId);
        return userOptional
                .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Страница позволяет провести поиск юзеров по нужным параметрам, можно указывать не все
     * Данный метод, доступный только админу(через фильтр сервлетов),
     *
     * @param role  роль юзера
     * @param name  имя юзера
     * @param block заблокирован ли юзер
     * @return ответ сервера
     */
    @GetMapping("/find-by_parameters")
    public ResponseEntity<List<User>> findByParam(
            @RequestParam String role,
            @RequestParam String name,
            @RequestParam String block
    ) {
        List<User> userList = userService.findByParameters(role, name, block);
        if (!userList.isEmpty()) {
            return new ResponseEntity<>(userList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Страница обновления информации о юзере
     * Данный метод, доступный только админу(через фильтр сервлетов),
     *
     * @param userDTO сущность User, обвёрнутая в DTO для подачи в виде Json
     * @return ответ сервера
     */
    @PostMapping("/update")
    public ResponseEntity<User> update(@RequestBody UserUpdateDTO userDTO) {
        Optional<User> userOptional = userService.update(userDTO, "");
        return userOptional
                .map(user -> new ResponseEntity<>(userOptional.get(), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    /**
     * Страница удаления карточки юзера
     * Данный метод, доступный только админу(через фильтр сервлетов),
     *
     * @param userId ID юзера
     * @return ответ сервера
     */
    @DeleteMapping("/delete/{userId}")
    public ResponseEntity<String> delete(@PathVariable int userId) {
        boolean isUserDeleted = userService.delete(userId);
        return isUserDeleted ?
                new ResponseEntity<>("User is deleted", HttpStatus.OK) :
                new ResponseEntity<>("User is not deleted", HttpStatus.BAD_REQUEST);
    }

    /**
     * Страница вывода списка всех юзеров
     * Данный метод, доступный только админу(через фильтр сервлетов),
     *
     * @return ответ сервера
     */
    @GetMapping("/all")
    public ResponseEntity<List<User>> findAll() {
        List<User> userList = userService.findAll();
        if (!userList.isEmpty()) {
            return new ResponseEntity<>(userList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Страница запуска рассылки напоминаний юзерам на их емайлы
     * Данный метод, доступный только админу(через фильтр сервлетов),
     *
     * @return ответ сервера
     */
    @GetMapping("/remind-via-email")
    public ResponseEntity<List<User>> remindViaEmail() throws MessagingException {
        RemindViaEmail remindViaEmail = new RemindViaEmail(userService, habitService);
        try {
            remindViaEmail.run();
            return new ResponseEntity<>(HttpStatus.OK);
        } catch (MessagingException e) {
            return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
        }
    }

    /**
     * Запрос, чтобы прислали на емайл код сброса пароля
     * Данный метод доступен любому зарегистрированному юзеру
     *
     * @return ответ сервера
     */
    @GetMapping("/request-password-reset")
    public ResponseEntity<List<User>> resetPassword(HttpServletRequest request) {
        Optional<User> userOptional = userService.findByEmail(Utility.getUserEmail(request));
        if (userOptional.isPresent()) {
            try {
                ResetPasswordViaEmail resetPasswordViaEmail = new ResetPasswordViaEmail(userOptional.get());
                resetPasswordViaEmail.run();
                return new ResponseEntity<>(HttpStatus.OK);
            } catch (MessagingException e) {
                return new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Запрос. Сбросить старый пароль, ввести новый.
     * Необходим ввод кода, полученный через емайл.
     * Данный метод доступен любому зарегистрированному юзеру
     *
     * @param request HTTP запрос
     * @param userDTO сущность User, обвёрнутая в DTO для подачи в виде Json
     * @return ответ сервера
     */
    @PostMapping("/reset_password")
    public ResponseEntity<User> perform(HttpServletRequest request, @RequestBody UserUpdatePassDTO userDTO) {
        Optional<User> userOptional = userService.findByEmail(Utility.getUserEmail(request));
        if (userOptional.isPresent()) {
            Optional<User> updateUser = userService.updatePass(userOptional.get().getId(), userDTO.getPassword(), userDTO.getResetPassword());
            return updateUser
                    .map(user -> new ResponseEntity<>(updateUser.get(), HttpStatus.OK))
                    .orElseGet(() -> new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE));
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}