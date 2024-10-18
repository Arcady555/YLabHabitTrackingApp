package ru.parfenov.homework_2.server.repository.impl;

import org.junit.jupiter.api.*;
import org.testcontainers.junit.jupiter.Container;
import ru.parfenov.homework_2.server.enums.user.Role;
import ru.parfenov.homework_2.server.model.User;

import java.sql.SQLException;

class UserRepositoryJdbcImplTest {
    @Container
    public static InitContainer initContainer;
    private static UserRepositoryJdbcImpl userRepository;

    @BeforeAll
    static void beforeAll() {
        initContainer.getPostgreSQLContainer().start();
    }

    @AfterAll
    static void afterAll() {
        initContainer.getPostgreSQLContainer().stop();
    }

    @BeforeAll
    public static void initConnection() throws Exception {
        userRepository = new UserRepositoryJdbcImpl(initContainer.getConnection());
        User user = new User(0, "Arcady@mail.ru","password", "resetPassword", "Arcady",  Role.CLIENT, false);
        userRepository.create(user);
    }

    @AfterAll
    public static void closeConnection() throws SQLException {
        initContainer.getConnection().close();
    }

    @Test
    @DisplayName("Проверка findAll()")
    void whenCreateAndGetAllThanOk() {
        Assertions.assertEquals(userRepository.findAll().get(0).getId(), 1);
        Assertions.assertEquals(userRepository.findAll().get(0).getEmail(), "Arcady@mail.ru");
        Assertions.assertEquals(userRepository.findAll().get(0).getPassword(), "password");
        Assertions.assertEquals(userRepository.findAll().get(0).getResetPassword(), "resetPassword");
        Assertions.assertEquals(userRepository.findAll().get(0).getName(), "Arcady");
        Assertions.assertEquals(userRepository.findAll().get(0).getRole(), Role.CLIENT);
        Assertions.assertFalse(userRepository.findAll().get(0).isBlocked());
    }

    @Test
    @DisplayName("Проверка findById()")
    void whenCreateAndFindByIdThanOk() {
        Assertions.assertEquals(userRepository.findById(1).getId(), 1);
        Assertions.assertEquals(userRepository.findById(1).getEmail(), "Arcady@mail.ru");
        Assertions.assertEquals(userRepository.findById(1).getPassword(), "password");
        Assertions.assertEquals(userRepository.findById(1).getResetPassword(), "resetPassword");
        Assertions.assertEquals(userRepository.findById(1).getName(), "Arcady");
        Assertions.assertEquals(userRepository.findById(1).getRole(), Role.CLIENT);
        Assertions.assertFalse(userRepository.findById(1).isBlocked());
    }
}