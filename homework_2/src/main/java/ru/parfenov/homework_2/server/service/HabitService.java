package ru.parfenov.homework_2.server.service;

import ru.parfenov.homework_2.server.model.Habit;
import ru.parfenov.homework_2.server.model.User;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

/**
 * Класс данного слоя служит прослойкой между голым хранилищем привычек с его строгими методами и фронтом-страницами,
 * добавляя и изменяя некоторую логику-функционал
 */
public interface HabitService {

    /**
     * Создание привычки
     * @param user Модель -user
     * @param usefulness полезность привычки
     * @param name название привычки
     * @param description описание привычки
     * @param dateOfCreate дата создания привычки
     * @param firstPerform запланированное первое выполнение привычки
     * @param frequency частота выполнения привычки
     * @return привычку, с данными, полученными при сохранении
     */
    Optional<Habit> create(
            User user,
            boolean usefulness,
            String name,
            String description,
            LocalDate dateOfCreate,
            LocalDate firstPerform,
            Period frequency
    );

    /**
     * Удаление привычки
     * @param habitId ID привычки
     * @return получилось удалить или нет
     */
    boolean delete(long habitId);

    /**
     * Удаление всех привычек, которые принадлежали юзеру.(Применяется при удалении юзера)
     * @param user Модель -user
     */
    void deleteWithUser(User user);

    /**
     * Найти привычку по её ID
     * @param id ID привычки
     * @return Модель - привычка
     */
    Optional<Habit> findById(long id);

    /**
     * Вывод списка привычек по юзеру, который их создал
     * @param user Модель -user
     * @return список привычек
     */
    List<Habit> findByUser(User user);

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
            String name,
            String description,
            String dateOfCreate,
            String frequency
    );

    /**
     * Выполнить привычку
     * @param habit Модель -привычка
     * @return да или нет
     */
    boolean perform(Habit habit);

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
    Habit updateByUser(
            long habitId,
            String newUsefulness,
            String newActive,
            String newName,
            String newDescription,
            String newFrequency
    );

    /**
     * Напоминание о выполнении привычки
     * @param habitId ID привычки
     * @return строку
     */
    String remind(long habitId);

    /**
     * Напоминание о выполнении привычек сегодня
     * @param user Модель - user
     * @return список привычек
     */
    List<Habit> todayPerforms(User user);

    /**
     * Заполнение статистики по конкретной привычке
     * @param habitId  ID привычки
     * @param dateFrom дата от
     * @param dateTo дата до
     * @return строка
     */
    String statistic(long habitId, LocalDate dateFrom, LocalDate dateTo);
}