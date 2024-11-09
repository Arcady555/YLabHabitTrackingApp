package ru.parfenov.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import ru.parfenov.dto.user.UserGeneralDTO;
import ru.parfenov.dto.user.UserUpdateDTO;
import ru.parfenov.dto.user.UserUpdatePassDTO;
import ru.parfenov.emailsend.cases.RemindViaEmail;
import ru.parfenov.emailsend.cases.ResetPasswordViaEmail;
import ru.parfenov.model.User;
import ru.parfenov.service.HabitService;
import ru.parfenov.service.UserService;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
@Tag(name = "Контроллер юзеров", description = "служит для работы с данными юзеров")
public class UserController {
    private final UserService userService;
    private final HabitService habitService;

    @Autowired
    private RemindViaEmail remindViaEmail;

    @Autowired
    private ResetPasswordViaEmail resetPasswordViaEmail;

    /**
     * Страница вывода юзера по введённому id
     * Данный метод, доступный только админу(через SecurityFilterChain),
     *
     * @param userId ID юзера
     * @return ответ сервера
     */
    @Operation(
            summary = "Карточка юзера",
            description = "Вывод данных по юзеру"
    )
    @GetMapping("/user/{userId}")
    public ResponseEntity<UserGeneralDTO> viewUser(@PathVariable int userId) {
        Optional<UserGeneralDTO> userOptional = userService.findById(userId);
        return userOptional
                .map(user -> new ResponseEntity<>(user, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * Страница позволяет провести поиск юзеров по нужным параметрам, можно указывать не все
     * Данный метод, доступный только админу(через SecurityFilterChain),
     *
     * @param role  роль юзера
     * @param name  имя юзера
     * @param block заблокирован ли юзер
     * @return ответ сервера
     */
    @Operation(
            summary = "Поиск по параметрам",
            description = "Вывод списка юзеров, отсортированных по указанным параметрам"
    )
    @GetMapping("/find-by_parameters")
    public ResponseEntity<List<UserGeneralDTO>> findByParam(
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String block
    ) {
        List<UserGeneralDTO> userList = userService.findByParameters(role, name, block);
        if (userList.isEmpty()) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(userList, HttpStatus.OK);
        }
    }

    /**
     * Страница обновления информации о юзере
     * Данный метод, доступный только админу(через SecurityFilterChain),
     *
     * @param userDTO сущность User, обвёрнутая в DTO для подачи в виде Json
     * @return ответ сервера
     */
    @Operation(
            summary = "Обновление",
            description = "Изменение данных по юзеру"
    )
    @PostMapping("/update")
    public ResponseEntity<UserGeneralDTO> update(@RequestBody UserUpdateDTO userDTO) {
        Optional<UserGeneralDTO> userOptional = userService.update(userDTO, "");
        return userOptional
                .map(user -> new ResponseEntity<>(userOptional.get(), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }


    /**
     * Страница удаления карточки юзера
     * Данный метод, доступный только админу(через SecurityFilterChain),
     *
     * @param userId ID юзера
     * @return ответ сервера
     */
    @Operation(
            summary = "Удаление",
            description = "Удаление данных юзера из БД"
    )
    @DeleteMapping("/delete/{userId}")
    @Transactional
    public ResponseEntity<String> delete(@PathVariable int userId) {
        boolean isHabitDeleted = habitService.deleteWithUser(userId);
        boolean isUserDeleted = userService.delete(userId);
        return isHabitDeleted && isUserDeleted ?
                new ResponseEntity<>("User is deleted", HttpStatus.OK) :
                new ResponseEntity<>("User is not deleted", HttpStatus.BAD_REQUEST);
    }

    /**
     * Страница вывода списка всех юзеров
     * Данный метод, доступный только админу(через SecurityFilterChain),
     *
     * @return ответ сервера
     */
    @Operation(
            summary = "Все юзеры",
            description = "Вывод списка всех юзеров"
    )
    @GetMapping("/all")
    public ResponseEntity<List<UserGeneralDTO>> findAll() {
        List<UserGeneralDTO> userList = userService.findAll();
        if (!userList.isEmpty()) {
            return new ResponseEntity<>(userList, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Запрос, чтобы прислали на емайл код сброса пароля
     * Данный метод доступен любому зарегистрированному юзеру
     *
     * @return ответ сервера
     */
    @Operation(
            summary = "Запрос сброса пароля",
            description = "Запросить прислать код сброса на емайл"
    )
    @GetMapping("/request-password-reset")
    public ResponseEntity<User> reqPassReset(HttpServletRequest request) {
        resetPasswordViaEmail.run();
        return new ResponseEntity<>(HttpStatus.OK);
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
    @Operation(
            summary = "Сброс пароля",
            description = "Обновить пароль после получения кода на емайл"
    )
    @PostMapping("/reset_password")
    public ResponseEntity<UserGeneralDTO> resetPass(HttpServletRequest request, @RequestBody UserUpdatePassDTO userDTO) {
        Optional<UserGeneralDTO> updateUser = userService.updatePass(request, userDTO.getPassword(), userDTO.getResetPassword());
        return updateUser
                .map(user -> new ResponseEntity<>(updateUser.get(), HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.SERVICE_UNAVAILABLE));
    }
}