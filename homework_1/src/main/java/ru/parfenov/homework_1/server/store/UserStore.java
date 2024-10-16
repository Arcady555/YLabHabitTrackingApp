package ru.parfenov.homework_1.server.store;

import ru.parfenov.homework_1.server.model.User;

import java.util.List;

public interface UserStore {
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
     * @param user модель -user, с исходными данными
     * @return user модель -user, с данными, полученными при обновлении
     */
    User update(User user);

    /**
     * Удаление юзера из хранилища
     * @param userId  ID юзера
     * @return юзер, каким его мы знали(его последние данные в хранилище)
     */
    User delete(int userId);

    /**
     * Список всех юзеров из хранилища
     * @return список юзеров
     */
    List<User> findAll();
}