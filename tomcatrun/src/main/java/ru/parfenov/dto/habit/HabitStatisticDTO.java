package ru.parfenov.dto.habit;

import lombok.*;

/**
 * DTO для удобной подачи в json. Enum заменили на String
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
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
