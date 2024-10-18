package ru.parfenov.homework_2.server.utility;

public class JdbcRequests {
    public static String createUser = "INSERT INTO ht_schema.users(" +
            "email," +
            " password," +
            " reset_password," +
            " name," +
            " user_role," +
            " blocked" +
            ")" +
            " VALUES (?, ?, ?, ?, ?, ?)";
    public static String findUserById = "SELECT * FROM ht_schema.users WHERE id = ?";
    public static String findUserByEmail = "SELECT * FROM ht_schema.users WHERE email = ?";
    public static String findUserByEmailAndPassword = "SELECT * FROM ht_schema.users WHERE email = ? AND password = ?";
    public static String deleteUser = "DELETE FROM ht_schema.users WHERE id = ?";
    public static String findAllUsers = "SELECT * FROM ht_schema.users";

    public static String createHabit = "INSERT INTO ht_schema.habits(" +
            "user_id," +
            " useful," +
            " active," +
            " streaks_amount," +
            " name," +
            " description," +
            " date_of_create," +
            " planned_first_perform," +
            " planned_prev_perform," +
            " planned_next_perform," +
            " last_real_perform," +
            " frequency," +
            " performs_amount" +
            ")" +
            " VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
    public static String findHabitById = "SELECT * FROM ht_schema.habits WHERE id = ?";
    public static String findByUser = "SELECT * FROM ht_schema.habits WHERE user_id = ?";
    public static String findByUserForToday = "SELECT * FROM ht_schema.habits WHERE user_id = ? AND active = true AND planned_next_perform = today";
    public static String deleteHabit = "DELETE FROM ht_schema.habits WHERE id = ?";
    public static String deleteHabitsWithUser = "DELETE FROM ht_schema.habits WHERE user_id = ?";
}