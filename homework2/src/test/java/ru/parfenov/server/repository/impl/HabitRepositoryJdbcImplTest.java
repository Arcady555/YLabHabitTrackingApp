package ru.parfenov.server.repository.impl;

import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.parfenov.server.enums.user.Role;
import ru.parfenov.server.model.Habit;
import ru.parfenov.server.model.User;
import ru.parfenov.server.repository.HabitRepository;
import ru.parfenov.server.repository.UserRepository;
import ru.parfenov.server.utility.LiquibaseUpdate;

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
                1L, user, true, true, 1, "run", "run everyday", LocalDate.now(), LocalDate.now().plusDays(1L),
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
        Habit foundedHabit = habitRepository.findByUser(user).get(0);
        Assertions.assertEquals(foundedHabit.getId(), 1);
        Assertions.assertEquals(foundedHabit.getUser(), user);
        Assertions.assertTrue(foundedHabit.isUseful());
        Assertions.assertTrue(foundedHabit.isActive());
        Assertions.assertEquals(foundedHabit.getStreaksAmount(), 1);
        Assertions.assertEquals(foundedHabit.getName(), "run");
        Assertions.assertEquals(foundedHabit.getDescription(), "run everyday");
        Assertions.assertEquals(foundedHabit.getDateOfCreate(), LocalDate.now());
        Assertions.assertEquals(foundedHabit.getPlannedFirstPerform(), LocalDate.now().plusDays(1L));
        Assertions.assertNull(foundedHabit.getPlannedPrevPerform());
        Assertions.assertEquals(foundedHabit.getPlannedNextPerform(), LocalDate.now().plusDays(10L));
        Assertions.assertNull(foundedHabit.getLastRealPerform());
        Assertions.assertEquals(foundedHabit.getFrequency(), Period.of(0, 0, 10));
        Assertions.assertEquals(foundedHabit.getPerformsAmount(), 0);
    }

    @Test
    @DisplayName("Проверка findById()")
    void whenCreateAndFindByIdThanOk() {
        Habit foundedHabit = habitRepository.findById(1);
        Assertions.assertEquals(foundedHabit.getId(), 1);
        Assertions.assertEquals(foundedHabit.getUser().getId(), user.getId());
        Assertions.assertTrue(foundedHabit.isUseful());
        Assertions.assertTrue(foundedHabit.isActive());
        Assertions.assertEquals(foundedHabit.getStreaksAmount(), 1);
        Assertions.assertEquals(foundedHabit.getName(), "run");
        Assertions.assertEquals(foundedHabit.getDescription(), "run everyday");
        Assertions.assertEquals(foundedHabit.getDateOfCreate(), LocalDate.now());
        Assertions.assertEquals(foundedHabit.getPlannedFirstPerform(), LocalDate.now().plusDays(1L));
        Assertions.assertNull(foundedHabit.getPlannedPrevPerform());
        Assertions.assertEquals(foundedHabit.getPlannedNextPerform(), LocalDate.now().plusDays(10L));
        Assertions.assertNull(foundedHabit.getLastRealPerform());
        Assertions.assertEquals(foundedHabit.getFrequency(), Period.of(0, 0, 10));
        Assertions.assertEquals(foundedHabit.getPerformsAmount(), 0);
    }
}