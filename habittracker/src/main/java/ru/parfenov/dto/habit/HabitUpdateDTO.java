package ru.parfenov.dto.habit;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO для удобной подачи в json обновления привычки.
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class HabitUpdateDTO {
    @Schema(description = "Уникальный идентификатор привычки", example = "11111", accessMode = Schema.AccessMode.READ_ONLY)
    private long habitId;

    @Schema(description = "Полезность привычки", allowableValues = {"true", "false"}, accessMode = Schema.AccessMode.READ_ONLY)
    private String usefulness;

    @Schema(description = "Активность привычки", allowableValues = {"true", "false"})
    private String active;

    @Schema(description = "Название привычки", example = "Бегать", accessMode = Schema.AccessMode.READ_ONLY)
    private String name;

    @Schema(description = "Описание привычки", example = "Бегать каждое утро", accessMode = Schema.AccessMode.READ_ONLY)
    private String description;

    @Schema(description = "Частота выполнения привычки", example = "3")
    private int frequency;
}