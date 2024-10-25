package ru.parfenov.utility;

import ru.parfenov.enums.user.Role;

public class JdbcRequests {

    private JdbcRequests() {
    }

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
    public static String updateHabitViaPerform = "UPDATE ht_schema.habits SET streaks_amount = ? ," +
            " planned_prev_perform = ? , planned_next_perform = ? , last_real_perform = ? , performs_amount = ? WHERE id = ?";

    public static String updateUser(String newPassword, String newResetPassword, String newName, Role newUserRole, String newBlocked) {
        return "UPDATE ht_schema.users SET " + getRequestForUserUpdate(newPassword, newResetPassword, newName, newUserRole, newBlocked) + " WHERE id = ?";
    }

    public static String findUsersByParameters(Role role, String name, String block) {
        return "SELECT * FROM cs_schema.users WHERE " + getRequestForFindUsersByParam(role, name, block);
    }

    public static String updateHabitByUser(String newUsefulness, String newActive, String newName, String newDescription, int newFrequency) {
        return "UPDATE ht_schema.habits SET " + getRequestForUpdateByUser(newUsefulness, newActive, newName, newDescription, newFrequency) + " WHERE id = ?";
    }

    public static String findHabitsByParameters(String usefulness, String active, String name, String description, String dateOfCreate, int frequency) {
        return "SELECT * FROM cs_schema.users WHERE " + getRequestForFindByParam(usefulness, active, name, description, dateOfCreate, frequency);
    }

    private static String getRequestForUserUpdate(String newPassword, String newResetPassword, String newName, Role newUserRole, String newBlocked) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(!newPassword.isEmpty() ? "password = ? ," : "")
                .append(!newResetPassword.isEmpty() ? "reset_password = ? ," : "")
                .append(!newName.isEmpty() ? " name = ? ," : "")
                .append(newUserRole != null ? " user_role = ? ," : "")
                .append(newBlocked.isEmpty() ? " blocked = ? " : "");
        if (stringBuilder.toString().endsWith(",")) stringBuilder.setLength(stringBuilder.length() - 1);

        return stringBuilder.toString();
    }

    private static String getRequestForFindUsersByParam(Role role,
                                                        String name,
                                                        String block) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append(role != null ? " user_role = ? and" : "")
                .append(!name.isEmpty() ? " name = ? and" : "")
                .append(!block.isEmpty() ? " blocked = ?" : "");
        if (stringBuilder.toString().endsWith("and")) stringBuilder.setLength(stringBuilder.length() - 3);

        return stringBuilder.toString();
    }

    private static String getRequestForUpdateByUser(String newUsefulness, String newActive, String newName, String newDescription, int newFrequency) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append(!newUsefulness.isEmpty() ? "usefulness = ? ," : "")
                .append(newActive.isEmpty() ? " active = ? , " : "")
                .append(!newName.isEmpty() ? " name = ? ," : "")
                .append(!newDescription.isEmpty() ? " description = ? ," : "")
                .append(newFrequency != 0 ? " frequency = ? " : "");
        if (stringBuilder.toString().endsWith(",")) stringBuilder.setLength(stringBuilder.length() - 1);

        return stringBuilder.toString();
    }

    private static String getRequestForFindByParam(String usefulness, String active, String name, String description, String dateOfCreate, int frequency) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("user_id = ?")
                .append(!usefulness.isEmpty() ? " usefulness = ? and" : "")
                .append(!active.isEmpty() ? " active = ?" : "")
                .append(!name.isEmpty() ? " name = ? and" : "")
                .append(!description.isEmpty() ? " description = ?" : "")
                .append(!dateOfCreate.isEmpty() ? " date_of_create = ? and" : "")
                .append(frequency != 0 ? " frequency = ?" : "");
        if (stringBuilder.toString().endsWith("and")) stringBuilder.setLength(stringBuilder.length() - 3);

        return stringBuilder.toString();
    }
}