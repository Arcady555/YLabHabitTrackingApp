package ru.parfenov.repository;

import ru.parfenov.model.LogRecord;

import java.util.List;

public interface LogRepository {
    List<LogRecord> findByParam(String userId, String action, String dateTimeFrom, String dateTimeTo);
}