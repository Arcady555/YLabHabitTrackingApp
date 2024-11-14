package ru.parfenov.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO для удобной подачи в json. Enum заменили на String
 * И оставлены только поля, нужные для ввода при sign-up
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserSignUpDTO {
    @Schema(description = "Емайл юзера", example = "user@mail.ru")
    private String email;

    @Schema(description = "Пароль юзера", example = "password")
    private String password;

    @Schema(description = "Имя юзера", example = "Вася", accessMode = Schema.AccessMode.READ_ONLY)
    private String name;
}
