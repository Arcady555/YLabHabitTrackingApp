package ru.parfenov.dto.habit;

import lombok.Data;

/**
 * DTO для удобной подачи в json. Enum заменили на String
 */
@Data
public class HabitCreateDTO {
    private String usefulness;
    private String name;
    private String description;
    private String firstPerform;
    private int frequency;
}