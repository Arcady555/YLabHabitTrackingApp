package ru.parfenov.service;

import ru.parfenov.model.LogRecord;

import java.util.List;

public interface LogService {

    /**
     * Поиск записей лога, подпадающих под заданные параметры.
     * Некоторые можно оставить пустой строкой (но не все, иначе выйдет громадный список, пусть это будет ограничено)
     *
     * @param userEmail     емайл юзера, о котором запись в логе
     * @param action       действие юзера
     * @param dateTimeFrom с какой даты-времени нужен поиск
     * @param dateTimeTo   по какую дату-время нужен поиск
     * @return требуемый список
     */
    List<LogRecord> findByParameters(
            String userEmail,
            String action,
            String dateTimeFrom,
            String dateTimeTo
    );
}
