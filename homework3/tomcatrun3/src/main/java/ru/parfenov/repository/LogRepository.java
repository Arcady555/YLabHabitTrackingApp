package ru.parfenov.repository;

import ru.parfenov.model.LogRecord;

import java.time.LocalDateTime;
import java.util.List;

public interface LogRepository {
    List<LogRecord> findByDateTime(LocalDateTime dateTimeFrom, LocalDateTime dateTimeTo);
}