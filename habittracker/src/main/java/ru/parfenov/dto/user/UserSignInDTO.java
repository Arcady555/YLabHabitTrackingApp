package ru.parfenov.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO для удобной подачи в json. Оставлены только поля, нужные входа в приложение зарегистрированным юзерам
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserSignInDTO {
    @Schema(description = "Емайл юзера", example = "user@mail.ru")
    private String email;

    @Schema(description = "Пароль юзера", example = "password")
    private String password;
}