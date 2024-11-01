package ru.parfenov.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.parfenov.model.LogRecord;
import ru.parfenov.repository.LogRepository;
import ru.parfenov.service.LogService;

import java.util.Collections;
import java.util.List;

@Slf4j
@Service
public class LogServiceServletImpl implements LogService {
    private final LogRepository logRepository;

    @Autowired
    public LogServiceServletImpl(LogRepository logRepository) {
        this.logRepository = logRepository;
    }

    @Override
    public List<LogRecord> findByParameters(String userId, String action, String dateTimeFrom, String dateTimeTo) {
        if (userId.isEmpty() && action.isEmpty() && dateTimeFrom.isEmpty() && dateTimeTo.isEmpty()) {
            return Collections.emptyList();
        } else {
            return logRepository.findByParam(userId, action, dateTimeFrom, dateTimeTo);
        }
    }
}