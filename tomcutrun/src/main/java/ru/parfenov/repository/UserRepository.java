package ru.parfenov.repository;

import ru.parfenov.enums.user.Role;
import ru.parfenov.model.User;

import java.util.List;

public interface UserRepository {

    /**
     * Сохранение юзера в хранилище
     * @param user модель -user, с заданными данными
     * @return user модель -user, с данными, полученными при сохранении
     */
    User create(User user);

    /**
     * Поиск юзера по его емайл
     * @param email емайл юзера
     * @return user модель -user
     */
    User findByEmail(String email);

    /**
     * Поиск юзера по его ID
     * @param userId ID юзера
     * @return user модель -user
     */
    User findById(int userId);

    /**
     * Поиск юзера пол его емайл и паролю
     * @param email емайл юзера
     * @param password пароль юзера
     * @return user модель -user
     */
    User findByEmailAndPassword(String email, String password);

    /**
     * Обновление инфы об юзере
     * @param userId ID юзера, вокруг которого весь процесс
     * @param newPassword новый пароль, может быть пустая строка, если не запросили его изменение
     * @param newName новое имя, может быть пустая строка, если не запросили его изменение
     * @param newUserRole новая роль, может быть null, если не запросили её изменение
     * @param blocked блокировка, может быть пустая строка, если не запросили его изменение
     * @return user модель -user, с данными, полученными при обновлении
     */
    User update(int userId, String newPassword, String newResetPassword, String newName, Role newUserRole, String blocked);

    /**
     * Удаление юзера из хранилища
     * @param userId  ID юзера
     * @return юзер, каким его мы знали(его последние данные в хранилище)
     */
    void delete(int userId);

    /**
     * Список всех юзеров из хранилища
     * @return список юзеров
     */
    List<User> findAll();

    /**
     * Метод предполагает поиск по параметрам (всем или некоторые можно не указать)
     * @param role роль юзера
     * @param name имя юзера
     * @param block заблокирован ли он
     * @return список юзеров по данным параметрам
     */
    List<User> findByParameters(String role, String name, String block);
}