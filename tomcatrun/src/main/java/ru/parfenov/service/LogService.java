package ru.parfenov.service;

import ru.parfenov.model.LogRecord;

import java.util.List;

public interface LogService {
    List<LogRecord> findByDateTime(String dateTimeFrom, String dateTimeTo);
}