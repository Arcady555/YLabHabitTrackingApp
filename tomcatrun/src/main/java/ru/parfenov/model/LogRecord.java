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
     * Дата и время создания записи лога
     */
    private LocalDateTime logTime;
    /**
     * Уровень логирования
     */
    private String level;
    /**
     * Сообщение лога
     */
    private String message;
}
