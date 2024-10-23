package ru.parfenov.homework_2.server.enums.user;

/**
 * Класс определяет роли с разными правами для юзера
 */
public enum Role {
    ADMIN("ADMIN"),
    CLIENT("CLIENT");

    private final String name;

    Role(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}