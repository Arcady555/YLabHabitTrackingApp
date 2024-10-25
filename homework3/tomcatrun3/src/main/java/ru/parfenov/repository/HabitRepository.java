package ru.parfenov.repository;

import ru.parfenov.model.Habit;
import ru.parfenov.model.User;

import java.util.List;

public interface HabitRepository {

    /**
     * Сохранение привычки в хранилище
     * @param habit Модель - привычка. С заявленными данными
     * @return Модель - привычка. С данными, полученными при сохранении
     */
    Habit create(Habit habit);

    /**
     * Удаление привычки из хранилища
     * @param habitId ID привычки
     */
    void delete(long habitId);

    void deleteWithUser(int userId);

    /**
     * Поиск списка привычек по их создателю-юзеру
     * @param user Модель -user
     * @return список привычек
     */
    List<Habit> findByUser(User user);

    /**
     * Выборка из БД привычек по юзеру и по сегодняшней дате
     * @param user Модель - user
     * @return список привычек
     */
    List<Habit> findByUserForToday(User user);

    /**
     * Поиск привычки по её ID
     * @param id ID привычки
     * @return   Модель - привычка
     */
    Habit findById(long id);

    /**
     * Редактировать-поменять данные привычки
     * Некоторые параметры могут быть пустой строкой, если их не надо менять
     *
     * @param habitId id привычки, которую надо обновить
     * @param newUsefulness полезность
     * @param newActive активность
     * @param newName название
     * @param newDescription описание
     * @param newFrequency частота
     * @return habit Модель - привычка, с обновлёнными данными
     */
    Habit updateByUser(long habitId, String newUsefulness, String newActive, String newName, String newDescription, int newFrequency);

    Habit updateViaPerform(Habit habit);

    /**
     * Поиск привычек юзера подпадающих под заданные параметры
     * @param user Модель -user
     * @param usefulness полезность
     * @param active активность
     * @param name название
     * @param description описание
     * @param dateOfCreate дата создания
     * @param frequency частота выполнения
     * @return список привычек
     */
    List<Habit> findByParameters(
            User user,
            String usefulness,
            String active,
            String  name,
            String description,
            String dateOfCreate,
            String frequency
    );
}