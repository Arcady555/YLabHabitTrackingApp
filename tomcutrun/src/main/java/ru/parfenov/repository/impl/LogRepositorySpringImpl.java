package ru.parfenov.repository.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import ru.parfenov.model.LogRecord;
import ru.parfenov.repository.LogRepository;
import ru.parfenov.utility.JdbcRequests;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
public class LogRepositorySpringImpl implements LogRepository {
    private final Connection connection;

    @Autowired
    public LogRepositorySpringImpl(Connection connection) throws Exception {
        this.connection = connection;
    }

    @Override
    public List<LogRecord> findByParam(String userId, String action, String dateTimeFrom, String dateTimeTo) {
        List<LogRecord> result = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(JdbcRequests.findLogsByParam(userId, action, dateTimeFrom, dateTimeTo))) {
            generateStatementSets(statement, userId, action, dateTimeFrom, dateTimeTo);
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

    private void generateStatementSets(PreparedStatement statement,
                                       String userId,
                                       String action,
                                       String dateTimeFrom,
                                       String dateTimeTo) throws SQLException {
        int result = 0;
        if (!userId.isEmpty()) {
            result++;
            statement.setInt(result, Integer.parseInt(userId));
        }
        if (!action.isEmpty()) {
            result++;
            statement.setString(result, action);
        }
        if (!dateTimeFrom.isEmpty()) {
            result++;
            statement.setTimestamp(result, Timestamp.valueOf(dateTimeFrom));
        }
        if (!dateTimeTo.isEmpty()) {
            result++;
            statement.setTimestamp(result, Timestamp.valueOf(dateTimeTo));
        }
    }

    private LogRecord returnLogRecord(ResultSet resultSet) throws SQLException {
        return new LogRecord(
                resultSet.getLong("event_id"),
                resultSet.getTimestamp("log_time").toLocalDateTime(),
                resultSet.getInt("user_id"),
                resultSet.getString("message")
        );
    }
}