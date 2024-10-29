package ru.parfenov.repository.impl;

import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.parfenov.enums.user.Role;
import ru.parfenov.model.Habit;
import ru.parfenov.model.User;
import ru.parfenov.repository.HabitRepository;
import ru.parfenov.repository.UserRepository;
import ru.parfenov.utility.LiquibaseUpdate;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;

@Testcontainers
class HabitRepositoryJdbcImplTest {
    @Container
    private static PostgreSQLContainer<?> postgreSQLContainer = new PostgreSQLContainer<>("postgres:13");
    private static Connection connection;

    private static HabitRepository habitRepository;
    private static User user;
    private static Habit habit;
    private static Habit habitPerformToday;

    @BeforeAll
    public static void initConnection() throws Exception {
        connection = DriverManager.getConnection(
                postgreSQLContainer.getJdbcUrl(),
                postgreSQLContainer.getUsername(),
                postgreSQLContainer.getPassword());
        LiquibaseUpdate liquibaseUpdate = new LiquibaseUpdate(connection);
        liquibaseUpdate.run();
        UserRepository userRepository = new UserRepositoryJdbcImpl(connection);
        user = new User(0, "Arcady@mail.ru","password", "resetPassword", "Arcady",  Role.CLIENT, false);
        userRepository.create(user);
        habitRepository = new HabitRepositoryJdbcImpl(connection, userRepository);
        Period frequency1 = Period.of(0, 0, 10);
        Period frequency2 = Period.of(0, 0, 0);
        habit = new Habit(
                1L, user, true, true, 1, "run", "run everyday", LocalDate.now(), LocalDate.now().plusDays(frequency1.getDays()),
                LocalDate.now().plusDays(1L), LocalDate.now().plus(frequency1), null, frequency1, 0);
        habitPerformToday = new Habit(
                2L, user, true, true, 1, "sleep", "sleep every night", LocalDate.now(), LocalDate.now().plusDays(frequency2.getDays()),
                LocalDate.now().plusDays(1L), LocalDate.now().plus(frequency2), null, frequency2, 0);
        habitRepository.create(habit);
        habitRepository.create(habitPerformToday);
    }

    @AfterAll
    public static void closeConnection() throws SQLException {
        connection.close();
    }

    @Test
    @DisplayName("Проверка findByUser()")
    void whenCreateAndFindByUserThanOk() {
        Habit foundedHabit = habitRepository.findByUser(user.getId()).get(0);
        Assertions.assertEquals(foundedHabit.getId(), 1);
        Assertions.assertEquals(foundedHabit.getUser().getEmail(), user.getEmail());
        Assertions.assertTrue(foundedHabit.isUseful());
        Assertions.assertTrue(foundedHabit.isActive());
        Assertions.assertEquals(foundedHabit.getStreaksAmount(), 1);
        Assertions.assertEquals(foundedHabit.getName(), "run");
        Assertions.assertEquals(foundedHabit.getDescription(), "run everyday");
        Assertions.assertEquals(foundedHabit.getDateOfCreate(), LocalDate.now());
        Assertions.assertEquals(foundedHabit.getPlannedFirstPerform(), LocalDate.now().plusDays(10L));
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
        Assertions.assertEquals(foundedHabit.getPlannedFirstPerform(), LocalDate.now().plusDays(10L));
        Assertions.assertNull(foundedHabit.getPlannedPrevPerform());
        Assertions.assertEquals(foundedHabit.getPlannedNextPerform(), LocalDate.now().plusDays(10L));
        Assertions.assertNull(foundedHabit.getLastRealPerform());
        Assertions.assertEquals(foundedHabit.getFrequency(), Period.of(0, 0, 10));
        Assertions.assertEquals(foundedHabit.getPerformsAmount(), 0);
    }

    @Test
    @DisplayName("Проверка findByUserForToday()")
    void whenCreateAndFindByUserForToday() {
        List<Habit> foundedHabits = habitRepository.findByUserForToday(user);
        Assertions.assertEquals(foundedHabits.get(0).getId(), 2);
        Assertions.assertEquals(foundedHabits.get(0).getUser().getId(), user.getId());
        Assertions.assertTrue(foundedHabits.get(0).isUseful());
        Assertions.assertTrue(foundedHabits.get(0).isActive());
        Assertions.assertEquals(foundedHabits.get(0).getStreaksAmount(), 1);
        Assertions.assertEquals(foundedHabits.get(0).getName(), "sleep");
        Assertions.assertEquals(foundedHabits.get(0).getDescription(), "sleep every night");
        Assertions.assertEquals(foundedHabits.get(0).getDateOfCreate(), LocalDate.now());
        Assertions.assertEquals(foundedHabits.get(0).getPlannedFirstPerform(), LocalDate.now().plusDays(0L));
        Assertions.assertNull(foundedHabits.get(0).getPlannedPrevPerform());
        Assertions.assertEquals(foundedHabits.get(0).getPlannedNextPerform(), LocalDate.now().plusDays(0L));
        Assertions.assertNull(foundedHabits.get(0).getLastRealPerform());
        Assertions.assertEquals(foundedHabits.get(0).getFrequency(), Period.of(0, 0, 0));
        Assertions.assertEquals(foundedHabits.get(0).getPerformsAmount(), 0);
    }

    @Test
    @DisplayName("Проверка updateViaPerform()")
    void whenUpdateViaPerform() {
        Habit foundedHabit = habitRepository.updateViaPerform(habitPerformToday);
        Assertions.assertEquals(foundedHabit.getId(), 2);
        Assertions.assertEquals(foundedHabit.getUser().getId(), user.getId());
        Assertions.assertTrue(foundedHabit.isUseful());
        Assertions.assertTrue(foundedHabit.isActive());
        Assertions.assertEquals(foundedHabit.getStreaksAmount(), 1);
        Assertions.assertEquals(foundedHabit.getName(), "sleep");
        Assertions.assertEquals(foundedHabit.getDescription(), "sleep every night");
        Assertions.assertEquals(foundedHabit.getDateOfCreate(), habitPerformToday.getDateOfCreate());
        Assertions.assertEquals(foundedHabit.getPlannedFirstPerform(), habitPerformToday.getPlannedFirstPerform());
        Assertions.assertNull(foundedHabit.getLastRealPerform());
        Assertions.assertEquals(foundedHabit.getPlannedNextPerform(), habitPerformToday.getPlannedNextPerform().plusDays(0L));
        Assertions.assertEquals(foundedHabit.getFrequency(), Period.of(0, 0, 0));
        Assertions.assertEquals(foundedHabit.getPerformsAmount(), 0);
    }

    @Test
    @DisplayName("Проверка delete()")
    void whenDelete() {
        habitRepository.delete(habitPerformToday.getId());
        Assertions.assertNull(habitRepository.findById(habitPerformToday.getId()));
    }
}