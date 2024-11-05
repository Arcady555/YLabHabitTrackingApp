package ru.parfenov.dto.habit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO для удобной подачи в json обновлении привычки.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class HabitUpdateDTO {
    private long habitId;
    private String usefulness;
    private String active;
    private String name;
    private String description;
    private int frequency;
}