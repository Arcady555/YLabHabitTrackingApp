package ru.parfenov.dto.habit;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO для удобной подачи в json. Enum заменили на String
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class HabitCreateDTO {
    private String usefulness;
    private String name;
    private String description;
    private String firstPerform;
    private int frequency;
}