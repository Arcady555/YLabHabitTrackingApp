package ru.parfenov.logappender;

import ch.qos.logback.classic.db.DBAppender;
import ch.qos.logback.classic.spi.ILoggingEvent;
import lombok.extern.slf4j.Slf4j;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

@Slf4j
public class MyDBAppender extends DBAppender {
    @Override
    protected String getInsertSQL() {
        return "INSERT INTO ht_log_schema.user_logs (date_time,user_id,action) VALUES (?,?,?)";
    }

    @Override
    protected void subAppend(ILoggingEvent event, Connection connection, PreparedStatement preparedStatement) {
        try {
            String message = event.getMessage();
            String userIdStr = message.split(",")[0];
            int userId = Integer.parseInt(userIdStr);
            preparedStatement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            preparedStatement.setInt(2, userId);
            preparedStatement.setString(3, message);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            log.error("Problem in SQL!", e);
        }
    }
}