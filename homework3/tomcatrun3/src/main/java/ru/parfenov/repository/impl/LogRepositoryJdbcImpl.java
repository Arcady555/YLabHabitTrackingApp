package ru.parfenov.repository.impl;

import lombok.extern.slf4j.Slf4j;
import ru.parfenov.model.LogRecord;
import ru.parfenov.model.User;
import ru.parfenov.repository.LogRepository;
import ru.parfenov.utility.JdbcRequests;
import ru.parfenov.utility.Utility;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class LogRepositoryJdbcImpl implements LogRepository {
    private final Connection connection;

    public LogRepositoryJdbcImpl() throws Exception {
        this.connection = Utility.loadConnection();
    }
    @Override
    public List<LogRecord> findByDateTime(LocalDateTime dateTimeFrom, LocalDateTime dateTimeTo) {
        List<LogRecord> result = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(JdbcRequests.findLogsOnPeriod)) {
            statement.setTimestamp(1, Timestamp.valueOf(dateTimeFrom));
            statement.setTimestamp(2, Timestamp.valueOf(dateTimeTo));
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    LogRecord logRecord = returnLogRecord(resultSet);
                    result.add(logRecord);
                }
            }
        } catch (Exception e) {
            log.error("Exception in LogRepositoryJdbcImpl.findAll(). ", e);
        }
        return result;
    }

    private LogRecord returnLogRecord(ResultSet resultSet) throws SQLException {
        return new LogRecord(
                resultSet.getTimestamp("log_time").toLocalDateTime(),
                resultSet.getString("log_level"),
                resultSet.getString("message")
        );
    }
}