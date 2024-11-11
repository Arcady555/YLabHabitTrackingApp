package ru.parfenov.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;
import ru.parfenov.model.Habit;
import ru.parfenov.model.User;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

public interface HabitRepository extends CrudRepository<Habit, Integer> {

    /**
     * Удаление из БД всех привычек, связанных с данным юзером
     * @param user  сущность юзер
     */
    @Modifying
    @Transactional
    @Query("DELETE FROM Habit h WHERE h.user = :user")
    void deleteWithUser(@Param("user") User user);

    /**
     * Поиск списка привычек по их создателю-юзеру
     *
     * @param user юзер
     * @return список привычек
     */
    List<Habit> findByUser(User user);

    /**
     * Выборка из БД привычек по юзеру и по сегодняшней дате
     *
     * @param user сущность юзер
     * @return список привычек
     */
    @Query("SELECT h FROM Habit h WHERE h.user = :user AND h.active = true AND h.plannedNextPerform = current_date")
    List<Habit> findByUserForToday(@Param("user") User user);

    /**
     * Поиск привычки по её ID
     *
     * @param id ID привычки
     * @return Модель - привычка
     */
    Optional<Habit> findById(long id);

    /**
     * Поиск привычек юзера подпадающих под заданные параметры
     *
     * @param userId ID юзера
     * @param usefulness   полезность
     * @param active       активность
     * @param name         название
     * @param description  описание
     * @param dateOfCreate дата создания
     * @param frequency    частота выполнения
     * @return список привычек
     */
    @Query("SELECT h FROM Habit h WHERE " +
            "h.user = :userId AND " +
            "(:usefulnessStr = '' OR h.useful = :usefulness) AND " +
            "(:activeStr = '' OR h.active = :active) AND " +
            "(:name IS NULL OR h.name = :name) AND " +
            "(:description IS NULL OR h.description = :description) AND " +
            "(CAST(:dateOfCreate AS date) IS NULL OR h.dateOfCreate = :dateOfCreate) AND " +
            "(CAST(:frequency AS int) = 0 OR h.frequency = :frequency)")
    List<Habit> findByParameters(
            @Param("userId") int userId,
            @Param("usefulnessStr") String usefulnessStr,
            @Param("usefulness") boolean usefulness,
            @Param("activeStr") String activeStr,
            @Param("active") boolean active,
            @Param("name") String name,
            @Param("description") String description,
            @Param("dateOfCreate") LocalDate dateOfCreate,
            @Param("frequency") Period frequency
    );
}