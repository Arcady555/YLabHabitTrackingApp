package ru.parfenov.homework_1.server.service;

import ru.parfenov.homework_1.server.model.User;

import java.util.List;
import java.util.Optional;

/**
 * Класс данного слоя служит прослойкой между голым хранилищем юзеров с его строгими методами и фронтом-страницами,
 * добавляя и изменяя некоторую логику-функционал
 */
public interface UserService {

    /**
     * Создание юзера при регистрации в приложении
     * @param email емайл юзера
     * @param password его пароль
     * @param name его имя
     * @return Модель - юзер.
     */
    User createByReg(String email, String password, String name);

    /**
     * Поиск юзера по его емайл
     * @param email емайл юзера
     * @return юзер, обёрнутый в Optional
     */
    Optional<User> findByEmail(String email);

    /**
     * Поиск юзера по его емайл
     * @param userId ID юзера
     * @return юзер, обёрнутый в Optional
     */
    Optional<User> findById(int userId);

    /**
     * Поиск юзера по его емайл
     * @param email емайл юзера
     * @param password пароль юзера
     * @return юзер, обёрнутый в Optional
     */
    Optional<User> findByEmailAndPassword(String email, String password);

    /**
     * Поиск всех юзеров, зарегистрированных в хранилище
     * @return список юзеров
     */
    List<User> findAll();

    /**
     * Удаление юзера
     * @param user модель -user
     * @return получилось удалить или нет
     */
    boolean delete(User user);

    /**
     * Обновление данных по юзеру
     * @param user модель -user
     * @return модель -user.  С новыми данными
     */
    User update(User user);

    /**
     * Метод предполагает поиск по параметрам (всем или некоторые можно не указать)
     * @param role роль юзера
     * @param name имя юзера
     * @param block заблокирован ли он
     * @return список юзеров по данным параметрам
     */
    List<User> findByParameters(String role, String name, String block);
}
