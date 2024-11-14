package ru.parfenov.service;

import ru.parfenov.dto.user.UserGeneralDTO;
import ru.parfenov.dto.user.UserUpdateDTO;
import ru.parfenov.model.User;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

/**
 * Класс данного слоя служит прослойкой между голым хранилищем юзеров с его строгими методами и фронтом-страницами,
 * добавляя и изменяя некоторую логику-функционал
 */
public interface UserService {

    /**
     * Создание юзера при регистрации в приложении
     *
     * @param email    емайл юзера
     * @param password его пароль
     * @param name     его имя
     * @return Модель - юзер.
     */
    Optional<UserGeneralDTO> createByReg(String email, String password, String name);

    /**
     * Поиск юзера по его ID
     *
     * @param userId ID юзера
     * @return юзер, обёрнутый в Optional
     */
    Optional<UserGeneralDTO> findById(int userId);

    Optional<User> findByIdForHabitService(int userId);

    /**
     * Поиск юзера по его емайл
     *
     * @param email емайл юзера
     * @return юзер, обёрнутый в Optional
     */
    Optional<User> findByEmail(String email);

    /**
     * Поиск всех юзеров, зарегистрированных в хранилище
     *
     * @return список юзеров
     */
    List<UserGeneralDTO> findAll();

    /**
     * Поиск всех юзеров, зарегистрированных в хранилище. Для рассылки
     * @return список юзеров
     */
    List<User> findAllForMail();

    /**
     * Удаление юзера
     *
     * @param userId ID юзера
     * @return получилось удалить или нет
     */
    boolean delete(int userId);

    /**
     * Изменение данных по юзеру
     *
     * @param userDTO   модель User, обёрнутая в DTO
     * @param resetPass строка кода для сброса пароля. Обычно заглушается. Нужна только в одном случае - ResetPasswordServlet
     * @return юзер с новыми данными, если получится
     */
    Optional<UserGeneralDTO> update(UserUpdateDTO userDTO, String resetPass);

    /**
     * Обновление пароля юзера
     *
     * @param request HTTP запрос
     * @param newPassword   пароль для замены
     * @param resetPassword код для сброса старого пароля
     * @return юзер с новыми данными, если получится
     */
    Optional<UserGeneralDTO> updatePass(HttpServletRequest request, String newPassword, String resetPassword);

    /**
     * Метод предполагает поиск по параметрам (всем или некоторые можно не указать)
     *
     * @param role  роль юзера
     * @param name  имя юзера
     * @param block заблокирован ли он
     * @return список юзеров по данным параметрам
     */
    List<UserGeneralDTO> findByParameters(String role, String name, String block);
}
