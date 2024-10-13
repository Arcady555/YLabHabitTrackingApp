package ru.parfenov.homework_1.server.store;

import ru.parfenov.homework_1.server.model.Habit;
import ru.parfenov.homework_1.server.model.User;

import java.util.List;

public interface HabitStore {
    /**
     * Сохранение привычки в хранилище
     * @param habit Модель - привычка. С заявленными данными
     * @return Модель - привычка. С данными, полученными при сохранении
     */
    Habit create(Habit habit);

    /**
     * Удаление привычки из хранилища
     * @param habitId ID привычки
     * @return привычка, какой она была, с данными в хранилище до удаления
     */
    Habit delete(long habitId);

    /**
     * Поиск списка привычек по их создателю-юзеру
     * @param user Модель -user
     * @return список привычек
     */
    List<Habit> findByUser(User user);

    /**
     * Поиск привычки по её ID
     * @param id ID привычки
     * @return   Модель - привычка
     */
    Habit findById(long id);

    /**
     * Обновить привычку
     * @param habit Модель - привычка. С исходными данными
     * @return Модель - привычка. С обновлёнными данными
     */
    Habit update(Habit habit);

    /**
     * Удаление привычки
     * @param habit Модель - привычка.
     * @return Модель - привычка.
     */
    Habit delete(Habit habit);
}