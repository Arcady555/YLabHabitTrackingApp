package ru.parfenov.dto.habit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO для удобной подачи в json обновлении привычки.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class HabitUpdateDTO {
    private long habitId;
    private String usefulness;
    private String active;
    private String name;
    private String description;
    private int frequency;
}