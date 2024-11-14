package ru.parfenov.service;

import ru.parfenov.dto.habit.HabitCreateDTO;
import ru.parfenov.dto.habit.HabitGeneralDTO;
import ru.parfenov.dto.habit.HabitStatisticDTO;
import ru.parfenov.dto.habit.HabitUpdateDTO;
import ru.parfenov.model.Habit;

import java.util.List;
import java.util.Optional;

/**
 * Класс данного слоя служит прослойкой между голым хранилищем привычек с его строгими методами и фронтом-страницами,
 * добавляя и изменяя некоторую логику-функционал
 */
public interface HabitService {

    /**
     * Создание привычки
     *
     * @param habitDTO Модель habit, обёрнутая в DTO
     * @return привычку, с данными, полученными при сохранении
     */
    Optional<HabitGeneralDTO> create(HabitCreateDTO habitDTO);

    /**
     * Удаление привычки
     *
     * @param habitId ID привычки
     * @return получилось удалить или нет
     */
    boolean delete(long habitId);

    /**
     * Удаление всех привычек, которые принадлежали юзеру.(Применяется при удалении юзера)
     *
     * @param userId ID юзера
     */
    boolean deleteWithUser(int userId);

    /**
     * Найти привычку по её ID
     *
     * @param id ID привычки
     * @return Модель - привычка
     */
    Optional<Habit> findById(long id);

    /**
     * Вывод списка привычек по юзеру, который их создал
     *
     * @return список привычек
     */
    List<HabitGeneralDTO> findByUser();

    /**
     * Поиск привычек юзера подпадающих под заданные параметры
     *
     * @param usefulness   полезность
     * @param active       активность
     * @param name         название
     * @param description  описание
     * @param dateOfCreate дата создания
     * @param frequency    частота выполнения
     * @return список привычек
     */
    List<HabitGeneralDTO> findByParameters(
            String usefulness,
            String active,
            String name,
            String description,
            String dateOfCreate,
            int frequency
    );

    /**
     * Выполнить привычку
     *
     * @param habitId ID привычки в строке
     * @return да или нет
     */
    Optional<HabitGeneralDTO> perform(long habitId);

    /**
     * Редактировать-поменять данные привычки
     *
     * @param habitDTO DTO привычки под обновление
     * @return привычка с новыми данными, если получится
     */
    Optional<HabitGeneralDTO> updateByUser(HabitUpdateDTO habitDTO);

    /**
     * Напоминание о выполнении привычек сегодня
     *
     * @return список привычек
     */
    List<HabitGeneralDTO> todayPerforms();

    /**
     * Вывод для юзера статистики по каждой его привычке
     *
     * @param dateFrom дата начала нужного периода
     * @param dateTo   дата конца нужного периода
     * @return список привычек со статистикой к каждой
     */
    List<HabitStatisticDTO> statisticForUser(String dateFrom, String dateTo);
}