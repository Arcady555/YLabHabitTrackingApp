package ru.parfenov.homework_1.server.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.parfenov.homework_1.server.enums.user.Role;

/**
 * Модель пользователя приложения
 * У него есть роль с разными правами, которые обнаружатся в дальнейших слоях (клиент, админ),
 * имя,
 */
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class User {
    private int id;
    private String email;
    private String password;
    private String name;
    private Role role;
    private boolean blocked;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", name='" + name + '\'' +
                ", role=" + role +
                ", blocked=" + blocked +
                '}';
    }
}