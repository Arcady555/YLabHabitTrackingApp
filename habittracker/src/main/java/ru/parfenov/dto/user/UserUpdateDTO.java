package ru.parfenov.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO для удобной подачи в json. Оставлены только поля, нужные для ввода при обновлении данных юзера
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserUpdateDTO {
    @Schema(description = "Уникальный идентификатор юзера", example = "11", accessMode = Schema.AccessMode.READ_ONLY)
    private int id;

    @Schema(description = "Пароль юзера", example = "password")
    private String password;

    @Schema(description = "Имя юзера", example = "Вася", accessMode = Schema.AccessMode.READ_ONLY)
    private String name;

    @Schema(description = "Роль юзера", allowableValues = {"ADMIN", "CLIENT"})
    private String role;

    @Schema(description = "Блокировка юзера", allowableValues = {"true", "false"})
    private String blocked;
}
