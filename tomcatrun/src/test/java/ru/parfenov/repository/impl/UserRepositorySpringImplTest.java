package ru.parfenov.repository.impl;

import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.parfenov.enums.user.Role;
import ru.parfenov.model.User;
import ru.parfenov.repository.UserRepository;
import ru.parfenov.config.LiquibaseForTests;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

@Testcontainers
class UserRepositorySpringImplTest {
    @Container
    private static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:13");
    private static Connection connection;

    private static UserRepository userRepository;
    private static User user;

    @BeforeAll
    public static void initConnection() throws Exception {
        postgreSQLContainer.start();
        connection = DriverManager.getConnection(
                postgreSQLContainer.getJdbcUrl(),
                postgreSQLContainer.getUsername(),
                postgreSQLContainer.getPassword());
        LiquibaseForTests liquibase = new LiquibaseForTests(connection);
        liquibase.run();
        userRepository = new UserRepositorySpringImpl(connection);
        user = new User(0, "Arcady@mail.ru","password", "resetPassword", "Arcady",  Role.CLIENT, false);
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

    @Test
    @DisplayName("Проверка findByEmail()")
    void whenCreateAndFindByEmailThanOk() {
        User foundedUser = userRepository.findByEmail("Arcady@mail.ru");
        Assertions.assertEquals(foundedUser.getId(), 2);
        Assertions.assertEquals(foundedUser.getPassword(), "password");
        Assertions.assertEquals(foundedUser.getResetPassword(), "resetPassword");
        Assertions.assertEquals(foundedUser.getName(), "Arcady");
        Assertions.assertEquals(foundedUser.getRole(), Role.CLIENT);
        Assertions.assertFalse(foundedUser.isBlocked());
    }

    @Test
    @DisplayName("Проверка findByEmailAndPassword()")
    void whenCreateAndFindByEmailAndPasswordThanOk() {
        User foundedUser = userRepository.findByEmailAndPassword("Arcady@mail.ru", "password");
        Assertions.assertEquals(foundedUser.getId(), 2);
        Assertions.assertEquals(foundedUser.getPassword(), "password");
        Assertions.assertEquals(foundedUser.getResetPassword(), "resetPassword");
        Assertions.assertEquals(foundedUser.getName(), "Arcady");
        Assertions.assertEquals(foundedUser.getRole(), Role.CLIENT);
        Assertions.assertFalse(foundedUser.isBlocked());
    }

    @Test
    void create() {
    }

    @Test
    void findByEmail() {
    }

    @Test
    void findById() {
    }

    @Test
    void findByEmailAndPassword() {
    }

    @Test
    void update() {
    }

    @Test
    void delete() {
    }

    @Test
    void findAll() {
    }

    @Test
    void findByParameters() {
    }
}