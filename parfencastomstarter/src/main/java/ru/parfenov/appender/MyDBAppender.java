package ru.parfenov.appender;

import ch.qos.logback.classic.db.DBAppender;
import ch.qos.logback.classic.spi.ILoggingEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;

public class MyDBAppender extends DBAppender {
    @Override
    protected String getInsertSQL() {
        return "INSERT INTO ht_log_schema.user_logs (date_time,user_email,action) VALUES (?,?,?)";
    }

    @Override
    protected void subAppend(ILoggingEvent event, Connection connection, PreparedStatement preparedStatement) {
        Logger logger = LoggerFactory.getLogger("console and file logger");
        try {
            String message = event.getMessage();
            String userEmail = message.split(",")[0];
            preparedStatement.setTimestamp(1, Timestamp.valueOf(LocalDateTime.now()));
            preparedStatement.setString(2, userEmail);
            preparedStatement.setString(3, message);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            logger.error("Problem in SQL!", e);
        }
    }
}