package ru.parfenov.dto.habit;

import io.swagger.v3.oas.annotations.media.Schema;
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
public class HabitStatisticDTO {
    @Schema(description = "Уникальный идентификатор привычки", example = "11111", accessMode = Schema.AccessMode.READ_ONLY)
    private long id;

    @Schema(description = "Полезность привычки", allowableValues = {"true", "false"}, accessMode = Schema.AccessMode.READ_ONLY)
    private String useful;

    @Schema(description = "Название привычки", example = "Бегать", accessMode = Schema.AccessMode.READ_ONLY)
    private String name;

    @Schema(description = "Описание привычки", example = "Бегать каждое утро", accessMode = Schema.AccessMode.READ_ONLY)
    private String description;

    @Schema(description = "Дата создания привычки", example = "2023-10-01")
    private String dateOfCreate;

    @Schema(description = "Дата следующего выполнения привычки по плану", example = "2023-10-10")
    private String plannedNextPerform;

    @Schema(description = "Дата последнего выполнения привычки в реале", example = "2023-10-10")
    private String lastRealPerform;

    @Schema(description = "Частота выполнения привычки", example = "3")
    private int frequency;

    @Schema(description = "Статистика по данной привычке", example = """
            From 2023-10-10 to 2023-10-20 the habit was completed by 70%
            And during all time of the habit there were 3 cases of going beyond the execution schedule.
            """)
    private String statistic;
}
