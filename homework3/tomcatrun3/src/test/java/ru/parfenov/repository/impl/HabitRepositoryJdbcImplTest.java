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
        Habit foundedHabit = habitRepository.findByUser(user.getId()).get(0);
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

    // Creating a habit successfully returns the habit with a generated ID
  /*  @Test
    public void test_create_habit_success() throws Exception {
        UserRepository userRepository = mock(UserRepository.class);
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        ResultSet resultSet = mock(ResultSet.class);

        when(connection.prepareStatement(JdbcRequests.createHabit,
                Statement.RETURN_GENERATED_KEYS)).thenReturn(statement);
        when(statement.getGeneratedKeys()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true);
        when(resultSet.getInt(1)).thenReturn(1);

        HabitRepositoryJdbcImpl habitRepo = new
                HabitRepositoryJdbcImpl(connection, userRepository);
        User user = new User(1, "test@example.com", "password", "Test
                User", Role.CLIENT, false);
                Habit habit = new Habit(user, true, true, 0, "Test Habit",
                "Description", LocalDate.now(), LocalDate.now(), null,
                LocalDate.now(), null, Period.ofDays(1), 0);

        Habit createdHabit = habitRepo.create(habit);

        assertNotNull(createdHabit);
        assertEquals(1, createdHabit.getId());
    }

    // Deleting a habit by ID removes it from the database
    @Test
    public void test_delete_habit_by_id() throws Exception {
        UserRepository userRepository = mock(UserRepository.class);
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);

        when(connection.prepareStatement(JdbcRequests.deleteHabit)).thenReturn(statement);

        HabitRepositoryJdbcImpl habitRepo = new
                HabitRepositoryJdbcImpl(connection, userRepository);

        habitRepo.delete(1L);

        verify(statement).setLong(1, 1L);
        verify(statement).execute();
    }

    // Finding habits by user returns a list of habits associated with that user
    @Test
    public void test_find_habits_by_user() throws Exception {
        UserRepository userRepository = mock(UserRepository.class);
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        ResultSet resultSet = mock(ResultSet.class);

        when(connection.prepareStatement(JdbcRequests.findByUser)).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(true).thenReturn(false);

        User user = new User(1, "test@example.com", "password", "Test
                User", Role.CLIENT, false);
                when(userRepository.findById(1)).thenReturn(user);

        HabitRepositoryJdbcImpl habitRepo = new
                HabitRepositoryJdbcImpl(connection, userRepository);

        List<Habit> habits = habitRepo.findByUser(user);

        assertNotNull(habits);
        assertEquals(1, habits.size());
    }

    // Updating a habit's details by user returns the updated habit
    @Test
    public void test_update_habit_by_user() throws Exception {
        UserRepository userRepository = mock(UserRepository.class);
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);

        when(connection.prepareStatement(anyString())).thenReturn(statement);

        HabitRepositoryJdbcImpl habitRepo = new
                HabitRepositoryJdbcImpl(connection, userRepository);

        Habit updatedHabit = habitRepo.updateByUser(1L, "true",
                "true", "Updated Name", "Updated Description", 7);

        assertNotNull(updatedHabit);
    }

    // Creating a habit with missing fields should handle exceptions gracefully
    @Test
    public void test_create_habit_with_missing_fields() throws Exception {
        UserRepository userRepository = mock(UserRepository.class);
        Connection connection = mock(Connection.class);

        HabitRepositoryJdbcImpl habitRepo = new
                HabitRepositoryJdbcImpl(connection, userRepository);

        User user = new User(1, "test@example.com", "password", "Test
                User", Role.CLIENT, false);
                Habit habit = new Habit(user, true, true, 0, null, null,
                LocalDate.now(), LocalDate.now(), null, LocalDate.now(), null,
                Period.ofDays(1), 0);

        assertDoesNotThrow(() -> habitRepo.create(habit));
    }

    // Deleting a non-existent habit should not throw an error
    @Test
    public void test_delete_non_existent_habit() throws Exception {
        UserRepository userRepository = mock(UserRepository.class);
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);

        when(connection.prepareStatement(JdbcRequests.deleteHabit)).thenReturn(statement);

        HabitRepositoryJdbcImpl habitRepo = new
                HabitRepositoryJdbcImpl(connection, userRepository);

        assertDoesNotThrow(() -> habitRepo.delete(999L));
    }

    // Finding habits for a user with no habits should return an empty list
    @Test
    public void test_find_no_habits_for_user() throws Exception {
        UserRepository userRepository = mock(UserRepository.class);
        Connection connection = mock(Connection.class);
        PreparedStatement statement = mock(PreparedStatement.class);
        ResultSet resultSet = mock(ResultSet.class);

        when(connection.prepareStatement(JdbcRequests.findByUser)).thenReturn(statement);
        when(statement.executeQuery()).thenReturn(resultSet);
        when(resultSet.next()).thenReturn(false);

        User user = new User(1, "test@example.com", "password", "Test
                User", Role.CLIENT, false);

                HabitRepositoryJdbcImpl habitRepo = new
                HabitRepositoryJdbcImpl(connection, userRepository);

        List<Habit> habits = habitRepo.findByUser(user);

        assertNotNull(habits);
        assertTrue(habits.isEmpty());
    }

    // Updating a habit with invalid parameters should handle
    exceptions gracefully
    @Test
    public void test_update_habit_with_invalid_parameters() throws Exception {
        UserRepository userRepository = mock(UserRepository.class);
        Connection connection = mock(Connection.class);

        HabitRepositoryJdbcImpl habitRepo = new
                HabitRepositoryJdbcImpl(connection, userRepository);

        assertDoesNotThrow(() -> habitRepo.updateByUser(1L, "", "",
                "", "", -1));
    } */
}