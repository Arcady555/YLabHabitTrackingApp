package ru.parfenov.homework_2.server.repository.impl;

import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.parfenov.homework_2.server.enums.user.Role;
import ru.parfenov.homework_2.server.model.Habit;
import ru.parfenov.homework_2.server.model.User;
import ru.parfenov.homework_2.server.repository.HabitRepository;
import ru.parfenov.homework_2.server.repository.UserRepository;
import ru.parfenov.homework_2.server.utility.LiquibaseUpdate;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;

@Testcontainers
class HabitRepositoryJdbcImplTest {
    @Container
    private static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:13");
    private static Connection connection;

    private static UserRepository userRepository;
    private static HabitRepository habitRepository;
    private static User user;

    @BeforeAll
    public static void initConnection() throws Exception {
        connection = DriverManager.getConnection(
                postgreSQLContainer.getJdbcUrl(),
                postgreSQLContainer.getUsername(),
                postgreSQLContainer.getPassword());
        LiquibaseUpdate liquibaseUpdate = new LiquibaseUpdate(connection);
        liquibaseUpdate.run();
        userRepository = new UserRepositoryJdbcImpl(connection);
        user = new User(0, "Arcady@mail.ru","password", "resetPassword", "Arcady",  Role.CLIENT, false);
        userRepository.create(user);
        habitRepository = new HabitRepositoryJdbcImpl(connection, userRepository);
        Period frequency = Period.of(0, 0, 10);
        Habit habit = new Habit(
                0L, user, true, true, 1, "run", "run everyday", LocalDate.now(), LocalDate.now().plusDays(1L),
                LocalDate.now().plusDays(1L), LocalDate.now().plus(frequency), null, frequency, 0);
        habitRepository.create(habit);
    }

    @AfterAll
    public static void closeConnection() throws SQLException {
        connection.close();
    }

    @Test
    @DisplayName("Проверка findByUser()")
    void whenCreateAndFindByUserThanOk() {
        Assertions.assertEquals(habitRepository.findByUser(user).get(0).getId(), 0);
        Assertions.assertEquals(habitRepository.findByUser(user).get(0).getUser(), user);
        Assertions.assertTrue(habitRepository.findByUser(user).get(0).isUseful());
        Assertions.assertTrue(habitRepository.findByUser(user).get(0).isActive());
        Assertions.assertEquals(habitRepository.findByUser(user).get(0).getStreaksAmount(), 1);
        Assertions.assertEquals(habitRepository.findByUser(user).get(0).getName(), "run");
        Assertions.assertEquals(habitRepository.findByUser(user).get(0).getDescription(), "run everyday");
        Assertions.assertEquals(habitRepository.findByUser(user).get(0).getId(), 0);
        Assertions.assertEquals(habitRepository.findByUser(user).get(0).getDateOfCreate(), LocalDate.now());
        Assertions.assertEquals(habitRepository.findByUser(user).get(0).getPlannedFirstPerform(), LocalDate.now().plusDays(1L));
        Assertions.assertEquals(habitRepository.findByUser(user).get(0).getPlannedPrevPerform(), LocalDate.now().plusDays(1L));
        Assertions.assertEquals(habitRepository.findByUser(user).get(0).getPlannedNextPerform(), LocalDate.now().plusDays(11L));
        Assertions.assertNull(habitRepository.findByUser(user).get(0).getLastRealPerform());
        Assertions.assertEquals(habitRepository.findByUser(user).get(0).getFrequency(), Period.of(0, 0, 10));
        Assertions.assertEquals(habitRepository.findByUser(user).get(0).getPerformsAmount(), 0);
    }

    @Test
    @DisplayName("Проверка findById()")
    void whenCreateAndFindByIdThanOk() {
        Assertions.assertEquals(habitRepository.findById(0).getId(), 0);
        Assertions.assertEquals(habitRepository.findById(0).getUser(), user);
        Assertions.assertTrue(habitRepository.findById(0).isUseful());
        Assertions.assertTrue(habitRepository.findById(0).isActive());
        Assertions.assertEquals(habitRepository.findById(0).getStreaksAmount(), 1);
        Assertions.assertEquals(habitRepository.findById(0).getName(), "run");
        Assertions.assertEquals(habitRepository.findById(0).getDescription(), "run everyday");
        Assertions.assertEquals(habitRepository.findById(0).getId(), 0);
        Assertions.assertEquals(habitRepository.findById(0).getDateOfCreate(), LocalDate.now());
        Assertions.assertEquals(habitRepository.findById(0).getPlannedFirstPerform(), LocalDate.now().plusDays(1L));
        Assertions.assertEquals(habitRepository.findById(0).getPlannedPrevPerform(), LocalDate.now().plusDays(1L));
        Assertions.assertEquals(habitRepository.findById(0).getPlannedNextPerform(), LocalDate.now().plusDays(11L));
        Assertions.assertNull(habitRepository.findById(0).getLastRealPerform());
        Assertions.assertEquals(habitRepository.findById(0).getFrequency(), Period.of(0, 0, 10));
        Assertions.assertEquals(habitRepository.findById(0).getPerformsAmount(), 0);
    }
}