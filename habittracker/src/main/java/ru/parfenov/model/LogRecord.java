package ru.parfenov.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "user_logs", schema = "ht_log_schema")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LogRecord {
    /**
     * ID записи в логе
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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