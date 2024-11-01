package ru.parfenov.dto.habit;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * DTO для удобной подачи в json. Enum заменили на String
 */
@Data
@AllArgsConstructor
public class HabitStatisticDTO {
    private long id;
    private String useful;
    private String name;
    private String description;
    private String dateOfCreate;
    private String plannedNextPerform;
    private String lastRealPerform;
    private int frequency;
    private String statistic;
}
