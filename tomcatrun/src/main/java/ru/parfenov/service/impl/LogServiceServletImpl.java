package ru.parfenov.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import ru.parfenov.model.LogRecord;
import ru.parfenov.repository.LogRepository;
import ru.parfenov.service.LogService;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RequiredArgsConstructor
public class LogServiceServletImpl implements LogService {
    private final LogRepository logRepository;

    @Override
    public List<LogRecord> findByDateTime(String dateTimeFromStr, String dateTimeToStr) {
        LocalDateTime dateTimeFrom = LocalDateTime.parse(dateTimeFromStr);
        LocalDateTime dateTimeTo = LocalDateTime.parse(dateTimeToStr);
        return logRepository.findByDateTime(dateTimeFrom, dateTimeTo);
    }
}
