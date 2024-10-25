package ru.parfenov.server.repository.impl;

import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.parfenov.server.enums.user.Role;
import ru.parfenov.server.model.User;
import ru.parfenov.server.repository.UserRepository;
import ru.parfenov.server.utility.LiquibaseUpdate;

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
        postgreSQLContainer.start();
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
        User foundedUser = userRepository.findAll().get(1);
        Assertions.assertEquals(foundedUser.getId(), 2);
        Assertions.assertEquals(foundedUser.getEmail(), "Arcady@mail.ru");
        Assertions.assertEquals(foundedUser.getPassword(), "password");
        Assertions.assertEquals(foundedUser.getResetPassword(), "resetPassword");
        Assertions.assertEquals(foundedUser.getName(), "Arcady");
        Assertions.assertEquals(foundedUser.getRole(), Role.CLIENT);
        Assertions.assertFalse(foundedUser.isBlocked());
    }

    @Test
    @DisplayName("Проверка findById()")
    void whenCreateAndFindByIdThanOk() {
        User foundedUser = userRepository.findById(2);
        Assertions.assertEquals(foundedUser.getId(), 2);
        Assertions.assertEquals(foundedUser.getEmail(), "Arcady@mail.ru");
        Assertions.assertEquals(foundedUser.getPassword(), "password");
        Assertions.assertEquals(foundedUser.getResetPassword(), "resetPassword");
        Assertions.assertEquals(foundedUser.getName(), "Arcady");
        Assertions.assertEquals(foundedUser.getRole(), Role.CLIENT);
        Assertions.assertFalse(foundedUser.isBlocked());
    }
}