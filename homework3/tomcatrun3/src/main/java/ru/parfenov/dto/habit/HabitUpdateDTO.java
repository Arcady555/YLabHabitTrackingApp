package ru.parfenov.dto.habit;

import lombok.Data;

/**
 * DTO для удобной подачи в json обновлении привычки.
 */
@Data
public class HabitUpdateDTO {
    private long habitId;
    private String usefulness;
    private String active;
    private String name;
    private String description;
    private int frequency;
}