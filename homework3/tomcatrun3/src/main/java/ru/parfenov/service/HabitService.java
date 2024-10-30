package ru.parfenov.service;

import ru.parfenov.dto.habit.HabitCreateDTO;
import ru.parfenov.dto.habit.HabitGeneralDTO;
import ru.parfenov.dto.habit.HabitStatisticDTO;
import ru.parfenov.dto.habit.HabitUpdateDTO;
import ru.parfenov.model.Habit;
import ru.parfenov.model.User;

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
     * @param habitDTO Модель habit, обёрнутая в DTO
     * @return привычку, с данными, полученными при сохранении
     */
    Optional<HabitGeneralDTO> create(User user, HabitCreateDTO habitDTO);

    /**
     * Удаление привычки
     * @param habitId ID привычки
     * @return получилось удалить или нет
     */
    boolean delete(long habitId);

    /**
     * Удаление всех привычек, которые принадлежали юзеру.(Применяется при удалении юзера)
     * @param userId ID юзера
     */
    boolean deleteWithUser(String userId);

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
    List<HabitGeneralDTO> findByUser(User user);

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
    List<HabitGeneralDTO> findByParameters(
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
     * @param user сущность юзер
     * @param habitId ID привычки в строке
     * @return да или нет
     */
    Optional<Habit> perform(User user, String habitId);

    /**
     * Редактировать-поменять данные привычки
     *
     * @param user юзер, чьи привычки
     * @param habitDTO DTO привычки под обновление
     * @return привычка с новыми данными, если получится
     */
    Optional<HabitGeneralDTO> updateByUser(User user, HabitUpdateDTO habitDTO);

    /**
     * Напоминание о выполнении привычек сегодня
     * @param user Модель - user
     * @return список привычек
     */
    List<HabitGeneralDTO> todayPerforms(User user);

    /**
     * Вывод для юзера статистики по каждой его привычке
     *
     * @param user Модель - user
     * @param dateFrom дата начала нужного периода
     * @param dateTo дата конца нужного периода
     * @return список привычек со статистикой к каждой
     */
    List<HabitStatisticDTO> statisticForUser(User user, String dateFrom, String dateTo);
}