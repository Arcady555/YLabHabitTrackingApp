package ru.parfenov.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.parfenov.model.LogRecord;

import java.time.LocalDateTime;
import java.util.List;

public interface LogRepository extends CrudRepository<LogRecord, Integer> {

    /**
     * Вывод записей лога, которые ушли в базу данных, по параметрам
     *
     * @param userId       ID юзера
     * @param action       действие юзера
     * @param dateTimeFrom с какой даты-времени искать логи
     * @param dateTimeTo   по какую дату-время искать логи
     * @return список строк-записей логов
     */

    @Query("SELECT l FROM LogRecord l WHERE " +
            "(:userId = 0 OR l.userId = :userId) AND " +
            "(:action = '' OR l.action LIKE CONCAT ('%', :action, '%')) AND" +
            "(CAST(:dateTimeFrom AS time) IS NULL OR l.dateTime > :dateTimeFrom) AND" +
            "(CAST(:dateTimeTo AS time) IS NULL OR l.dateTime < :dateTimeTo)")
    List<LogRecord> findByParam(
            @Param("userId") long userId,
            @Param("action") String action,
            @Param("dateTimeFrom") LocalDateTime dateTimeFrom,
            @Param("dateTimeTo") LocalDateTime dateTimeTo
    );
}