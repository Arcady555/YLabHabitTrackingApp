package ru.parfenov.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.parfenov.enums.user.Role;

/**
 * Модель пользователя приложения
 * У него есть роль с разными правами, которые обнаружатся в дальнейших слоях (клиент, админ),
 * имя,
 */
@Entity
@Table(name = "users", schema = "ht_schema")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {
    /**
     * Уникальный идентификатор юзера. Даётся хранилищем
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    /**
     * Емайл юзера, заявленный при регистрации. Должен быть уникальным для хранилища
     */
    private String email;

    /**
     * Пароль, который придумал юзер при регистрации
     */
    private String password;

    /**
     * Рандомно сгенерированная строка, ввод которой позволит юзеру сбросить пароль и ввести новый.
     * В случае, если она пригодилась - после использования генерируется заново
     */
    @Column
    private String resetPassword;

    /**
     * Имя, которое дал себе юзер при регистрации. Может не давать
     */
    private String name;

    /**
     * Роль юзера. Всегда -- Client. Если только админ её не поменяет.
     */
    @Column(name = "user_role")
    @Enumerated(EnumType.STRING)
    private Role role;

    /**
     * И админ сможет юзера ещё и заблокировать. А пока пусть будет false)))
     */
    private boolean blocked;

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", resetPassword='" + resetPassword + '\'' +
                ", name='" + name + '\'' +
                ", role=" + role +
                ", blocked=" + blocked +
                '}';
    }
}