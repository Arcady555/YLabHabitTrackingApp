package ru.parfenov.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.parfenov.model.LogRecord;
import ru.parfenov.repository.LogRepository;
import ru.parfenov.service.LogService;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class LogServiceSpringImpl implements LogService {
    private final LogRepository logRepository;

    @Override
    public List<LogRecord> findByParameters(String userEmail, String action, String dateTimeFromStr, String dateTimeToStr) {
        List<LogRecord> result;
        if (userEmail.isEmpty() && action.isEmpty() && dateTimeFromStr.isEmpty() && dateTimeToStr.isEmpty()) {
            result = Collections.emptyList();
        } else {
            LocalDateTime dateTimeFrom = LocalDateTime.parse(dateTimeFromStr);
            LocalDateTime dateTimeTo = LocalDateTime.parse(dateTimeToStr);
            result = logRepository.findByParam(userEmail, action, dateTimeFrom, dateTimeTo);
        }
        return result;
    }
}