package ru.parfenov.repository.impl;

import org.junit.jupiter.api.*;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import ru.parfenov.enums.user.Role;
import ru.parfenov.model.User;
import ru.parfenov.repository.UserRepository;
import ru.parfenov.utility.LiquibaseUpdate;

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

    // Creating a user with valid data should return the user with an assigned ID
 /*   @Test
    public void create_user_with_valid_data() throws Exception {
        Connection mockConnection = Mockito.mock(Connection.class);
        PreparedStatement mockStatement = Mockito.mock(PreparedStatement.class);
        ResultSet mockResultSet = Mockito.mock(ResultSet.class);

        Mockito.when(mockConnection.prepareStatement(Mockito.anyString(),
                        Mockito.eq(Statement.RETURN_GENERATED_KEYS)))
                .thenReturn(mockStatement);
        Mockito.when(mockStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        Mockito.when(mockResultSet.next()).thenReturn(true);
        Mockito.when(mockResultSet.getInt(1)).thenReturn(1);

        UserRepositoryJdbcImpl userRepository = new
                UserRepositoryJdbcImpl(mockConnection);
        User user = new User();
        user.setEmail("test@example.com");
        user.setPassword("password");
        user.setResetPassword("reset");
        user.setName("Test User");
        user.setRole(Role.CLIENT);
        user.setBlocked(false);

        User createdUser = userRepository.create(user);

        assertNotNull(createdUser);
        assertEquals(1, createdUser.getId());
    }

    // Finding a user by email should return the correct user object
if the email exists
    @Test
    public void find_user_by_existing_email() throws Exception {
        Connection mockConnection = Mockito.mock(Connection.class);
        PreparedStatement mockStatement = Mockito.mock(PreparedStatement.class);
        ResultSet mockResultSet = Mockito.mock(ResultSet.class);

        Mockito.when(mockConnection.prepareStatement(Mockito.anyString())).thenReturn(mockStatement);
        Mockito.when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        Mockito.when(mockResultSet.next()).thenReturn(true);
        Mockito.when(mockResultSet.getInt("id")).thenReturn(1);
        Mockito.when(mockResultSet.getString("email")).thenReturn("test@example.com");

        UserRepositoryJdbcImpl userRepository = new
                UserRepositoryJdbcImpl(mockConnection);

        User foundUser = userRepository.findByEmail("test@example.com");

        assertNotNull(foundUser);
        assertEquals("test@example.com", foundUser.getEmail());
    }

    // Updating a user with valid data should return the updated user object
    @Test
    public void update_user_with_valid_data() throws Exception {
        Connection mockConnection = Mockito.mock(Connection.class);
        PreparedStatement mockStatement = Mockito.mock(PreparedStatement.class);
        ResultSet mockResultSet = Mockito.mock(ResultSet.class);

        Mockito.when(mockConnection.prepareStatement(Mockito.anyString())).thenReturn(mockStatement);
        Mockito.when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        Mockito.when(mockResultSet.next()).thenReturn(true);
        Mockito.when(mockResultSet.getInt("id")).thenReturn(1);

        UserRepositoryJdbcImpl userRepository = new
                UserRepositoryJdbcImpl(mockConnection);

        User updatedUser = userRepository.update(1, "newPassword",
                "newReset", "New Name", Role.ADMIN, "false");

        assertNotNull(updatedUser);
        assertEquals(1, updatedUser.getId());
    }

    // Deleting a user by ID should remove the user from the database
    @Test
    public void delete_user_by_id() throws Exception {
        Connection mockConnection = Mockito.mock(Connection.class);
        PreparedStatement mockStatement = Mockito.mock(PreparedStatement.class);

        Mockito.when(mockConnection.prepareStatement(Mockito.anyString())).thenReturn(mockStatement);

        UserRepositoryJdbcImpl userRepository = new
                UserRepositoryJdbcImpl(mockConnection);

        userRepository.delete(1);

        Mockito.verify(mockStatement, Mockito.times(1)).execute();
    }

    // Creating a user with an existing email should handle the
    constraint violation
    @Test
    public void create_user_with_existing_email() throws Exception {
        Connection mockConnection = Mockito.mock(Connection.class);
        PreparedStatement mockStatement = Mockito.mock(PreparedStatement.class);

        Mockito.when(mockConnection.prepareStatement(Mockito.anyString(),
                        Mockito.eq(Statement.RETURN_GENERATED_KEYS)))
                .thenReturn(mockStatement);
        Mockito.doThrow(new SQLException("Duplicate
                entry")).when(mockStatement).execute();

                UserRepositoryJdbcImpl userRepository = new
                UserRepositoryJdbcImpl(mockConnection);
        User user = new User();
        user.setEmail("duplicate@example.com");

        User createdUser = userRepository.create(user);

        assertNull(createdUser.getId());
    }

    // Finding a user by a non-existent email should return null
    @Test
    public void find_user_by_non_existent_email() throws Exception {
        Connection mockConnection = Mockito.mock(Connection.class);
        PreparedStatement mockStatement = Mockito.mock(PreparedStatement.class);
        ResultSet mockResultSet = Mockito.mock(ResultSet.class);

        Mockito.when(mockConnection.prepareStatement(Mockito.anyString())).thenReturn(mockStatement);
        Mockito.when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        Mockito.when(mockResultSet.next()).thenReturn(false);

        UserRepositoryJdbcImpl userRepository = new
                UserRepositoryJdbcImpl(mockConnection);

        User foundUser = userRepository.findByEmail("nonexistent@example.com");

        assertNull(foundUser);
    }

    // Updating a user with an invalid ID should not alter the database
    @Test
    public void update_user_with_invalid_id() throws Exception {
        Connection mockConnection = Mockito.mock(Connection.class);
        PreparedStatement mockStatement = Mockito.mock(PreparedStatement.class);

        Mockito.when(mockConnection.prepareStatement(Mockito.anyString())).thenReturn(mockStatement);

        UserRepositoryJdbcImpl userRepository = new
                UserRepositoryJdbcImpl(mockConnection);

        User updatedUser = userRepository.update(-1, "newPassword",
                "newReset", "New Name", Role.ADMIN, "false");

        assertNull(updatedUser.getId());
    }

    // Deleting a user with a non-existent ID should not throw an exception
    @Test
    public void delete_user_with_non_existent_id() throws Exception {
        Connection mockConnection = Mockito.mock(Connection.class);
        PreparedStatement mockStatement = Mockito.mock(PreparedStatement.class);

        Mockito.when(mockConnection.prepareStatement(Mockito.anyString())).thenReturn(mockStatement);

        UserRepositoryJdbcImpl userRepository = new
                UserRepositoryJdbcImpl(mockConnection);

        asser
    } */
}