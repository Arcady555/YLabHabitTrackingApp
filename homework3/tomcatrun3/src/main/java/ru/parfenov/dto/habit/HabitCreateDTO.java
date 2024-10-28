package ru.parfenov.dto.habit;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO для удобной подачи в json. Enum заменили на String
 */
@Data
@AllArgsConstructor
public class HabitCreateDTO {
    private String usefulness;
    private String name;
    private String description;
    private String firstPerform;
    private int frequency;
}