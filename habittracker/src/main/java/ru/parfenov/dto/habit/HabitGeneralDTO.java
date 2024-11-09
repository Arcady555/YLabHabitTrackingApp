package ru.parfenov.dto.habit;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class HabitGeneralDTO {
    @Schema(description = "Уникальный идентификатор привычки", example = "11111", accessMode = Schema.AccessMode.READ_ONLY)
    private long id;

    @Schema(description = "Создатель привычки - юзер", example = "user@mail.ru")
    private String user;

    @Schema(description = "Полезность привычки", accessMode = Schema.AccessMode.READ_ONLY)
    private boolean useful;

    @Schema(description = "Активность привычки")
    private boolean active;

    @Schema(description = "Количество стриков привычки", example = "2")
    private int streaksAmount;

    @Schema(description = "Название привычки", example = "Бегать", accessMode = Schema.AccessMode.READ_ONLY)
    private String name;

    @Schema(description = "Описание привычки", example = "Бегать каждое утро", accessMode = Schema.AccessMode.READ_ONLY)
    private String description;

    @Schema(description = "Дата создания привычки", example = "2023-10-01")
    private String dateOfCreate;

    @Schema(description = "Дата предыдущего выполнения привычки по плану", example = "2023-10-10")
    private String plannedNextPerform;

    @Schema(description = "Дата последнего выполнения привычки в реале", example = "2023-10-10")
    private String lastRealPerform;

    @Schema(description = "Частота выполнения привычки", example = "3")
    private int frequency;

    @Schema(description = "Количество реальных выполнений привычки", example = "3")
    private int performsAmount;

    @Schema(description = "Напоминание о выполнении привычки", example = "Perform of the habit is 6 days overdue!!!")
    private String remind;
}