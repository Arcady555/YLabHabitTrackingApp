package ru.parfenov.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * DTO для удобной подачи в json. Оставлены только поля, нужные для сброса пароля
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserUpdatePassDTO {
    @Schema(description = "Новый пароль юзера", example = "newPassword")
    private String password;

    @Schema(description = "Код сброса пароля", example = "1234")
    private String resetPassword;
}
