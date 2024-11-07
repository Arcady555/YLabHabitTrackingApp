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
    public List<LogRecord> findByParameters(String userIdStr, String action, String dateTimeFromStr, String dateTimeToStr) {
        List<LogRecord> result = Collections.emptyList();
        if (userIdStr.isEmpty() && action.isEmpty() && dateTimeFromStr.isEmpty() && dateTimeToStr.isEmpty()) {
            return Collections.emptyList();
        } else {
            try {
                long userId = Long.parseLong(userIdStr);
                LocalDateTime dateTimeFrom = LocalDateTime.parse(dateTimeFromStr);
                LocalDateTime dateTimeTo = LocalDateTime.parse(dateTimeToStr);
                result = logRepository.findByParam(userId, action, dateTimeFrom, dateTimeTo);
            } catch (Exception e) {
                log.error("Not correct values for parsing!", e);
            }
        }
        return result;
    }
}