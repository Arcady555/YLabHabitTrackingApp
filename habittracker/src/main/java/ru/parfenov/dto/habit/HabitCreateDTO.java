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
public class HabitCreateDTO {
    @Schema(description = "Полезность привычки", allowableValues = {"true", "false"}, accessMode = Schema.AccessMode.READ_ONLY)
    private String usefulness;

    @Schema(description = "Название привычки", example = "Бегать", accessMode = Schema.AccessMode.READ_ONLY)
    private String name;

    @Schema(description = "Описание привычки", example = "Бегать каждое утро", accessMode = Schema.AccessMode.READ_ONLY)
    private String description;

    @Schema(description = "Дата первого выполнения привычки по плану", example = "2023-10-10")
    private String firstPerform;

    @Schema(description = "Частота выполнения привычки", example = "3")
    private int frequency;
}