package ru.parfenov.service;

import ru.parfenov.dto.habit.HabitCreateDTO;
import ru.parfenov.dto.habit.HabitGeneralDTO;
import ru.parfenov.dto.habit.HabitStatisticDTO;
import ru.parfenov.dto.habit.HabitUpdateDTO;
import ru.parfenov.model.Habit;
import ru.parfenov.model.User;

import javax.servlet.http.HttpServletRequest;
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
     * @param request HTTP запрос
     * @param habitDTO Модель habit, обёрнутая в DTO
     * @return привычку, с данными, полученными при сохранении
     */
    Optional<HabitGeneralDTO> create(HttpServletRequest request, HabitCreateDTO habitDTO);

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
    boolean deleteWithUser(String userId);

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
     * @param request HTTP запрос
     * @return список привычек
     */
    List<HabitGeneralDTO> findByUser(HttpServletRequest request);

    /**
     * Поиск привычек юзера подпадающих под заданные параметры
     *
     * @param request HTTP запрос
     * @param usefulness   полезность
     * @param active       активность
     * @param name         название
     * @param description  описание
     * @param dateOfCreate дата создания
     * @param frequency    частота выполнения
     * @return список привычек
     */
    List<HabitGeneralDTO> findByParameters(
            HttpServletRequest request,
            String usefulness,
            String active,
            String name,
            String description,
            String dateOfCreate,
            String frequency
    );

    /**
     * Выполнить привычку
     *
     * @param request HTTP запрос
     * @param habitId ID привычки в строке
     * @return да или нет
     */
    Optional<HabitGeneralDTO> perform(HttpServletRequest request, long habitId);

    /**
     * Редактировать-поменять данные привычки
     *
     * @param request HTTP запрос
     * @param habitDTO DTO привычки под обновление
     * @return привычка с новыми данными, если получится
     */
    Optional<HabitGeneralDTO> updateByUser(HttpServletRequest request, HabitUpdateDTO habitDTO);

    /**
     * Напоминание о выполнении привычек сегодня
     *
     * @param request HTTP запрос
     * @return список привычек
     */
    List<HabitGeneralDTO> todayPerforms(HttpServletRequest request);

    /**
     * Вывод для юзера статистики по каждой его привычке
     *
     * @param request HTTP запрос
     * @param dateFrom дата начала нужного периода
     * @param dateTo   дата конца нужного периода
     * @return список привычек со статистикой к каждой
     */
    List<HabitStatisticDTO> statisticForUser(HttpServletRequest request, String dateFrom, String dateTo);
}