package ru.parfenov.homework_2.server.repository.impl;

import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.parfenov.homework_2.server.enums.user.Role;
import ru.parfenov.homework_2.server.model.User;
import ru.parfenov.homework_2.server.repository.UserRepository;
import ru.parfenov.homework_2.server.utility.LiquibaseUpdate;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Testcontainers
class UserRepositoryJdbcImplTest {
    @Container
    private static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:13");
    private static Connection connection;

    private static UserRepository userRepository;

    @BeforeAll
    public static void initConnection() throws Exception {
        connection = DriverManager.getConnection(
                postgreSQLContainer.getJdbcUrl(),
                postgreSQLContainer.getUsername(),
                postgreSQLContainer.getPassword());
        LiquibaseUpdate liquibaseUpdate = new LiquibaseUpdate(connection);
        liquibaseUpdate.run();
        userRepository = new UserRepositoryJdbcImpl(connection);
        User user = new User(0, "Arcady@mail.ru","password", "resetPassword", "Arcady",  Role.CLIENT, false);
        userRepository.create(user);
    }

    @AfterAll
    public static void closeConnection() throws SQLException {
        connection.close();
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