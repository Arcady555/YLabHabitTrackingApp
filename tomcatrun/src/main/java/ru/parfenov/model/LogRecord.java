package ru.parfenov.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class LogRecord {
    /**
     * ID записи в логе
     */
    private long eventId;
    /**
     * Дата и время создания записи лога
     */
    private LocalDateTime dateTime;
    /**
     * ID юзера, о котором запись
     */
    private int userId;
    /**
     * Сообщение лога
     */
    private String action;
}