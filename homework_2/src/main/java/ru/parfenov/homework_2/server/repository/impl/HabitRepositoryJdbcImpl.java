package ru.parfenov.homework_2.server.repository.impl;

import lombok.extern.slf4j.Slf4j;
import ru.parfenov.homework_2.server.model.Habit;
import ru.parfenov.homework_2.server.model.User;
import ru.parfenov.homework_2.server.repository.HabitRepository;
import ru.parfenov.homework_2.server.repository.UserRepository;
import ru.parfenov.homework_2.server.utility.JdbcRequests;
import ru.parfenov.homework_2.server.utility.Utility;

import java.io.IOException;
import java.sql.*;
import java.time.Period;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class HabitRepositoryJdbcImpl implements HabitRepository {
    private final Connection connection;
    private final UserRepository userRepository;

    public HabitRepositoryJdbcImpl(UserRepository userRepository) throws Exception {
        this.userRepository = userRepository;
        this.connection = Utility.loadConnection();
    }

    @Override
    public Habit create(Habit habit) {
        try (PreparedStatement statement = connection.prepareStatement(
                JdbcRequests.createHabit,
                Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setInt(1, habit.getUser().getId());
            statement.setBoolean(2, habit.isUseful());
            statement.setBoolean(3, habit.isActive());
            statement.setInt(4, habit.getStreaksAmount());
            statement.setString(5, habit.getName());
            statement.setString(6, habit.getDescription());
            statement.setDate(7, Date.valueOf(habit.getDateOfCreate()));
            statement.setDate(8, Date.valueOf(habit.getPlannedFirstPerform()));
            statement.setDate(9, Date.valueOf(habit.getPlannedPrevPerform()));
            statement.setDate(10, Date.valueOf(habit.getPlannedNextPerform()));
            statement.setDate(11, Date.valueOf(habit.getLastRealPerform()));
            statement.setInt(12, (habit.getFrequency().getDays()));
            statement.setInt(13, habit.getPerformsAmount());
            statement.execute();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    habit.setId(generatedKeys.getInt(1));
                }
            }
        } catch (Exception e) {
            log.error("Exception in CarStoreJdbcImpl.create(). ", e);
        }
        return findById(habit.getId());
    }

    @Override
    public void delete(long habitId) {
        try (PreparedStatement statement = connection.prepareStatement(JdbcRequests.deleteHabit)) {
            statement.setLong(1, habitId);
            statement.execute();
        } catch (Exception e) {
            log.error("Exception in HabitRepositoryJdbcImpl.delete(). ", e);
        }
    }

    @Override
    public void deleteWithUser(long habitId) {
        try (PreparedStatement statement = connection.prepareStatement(JdbcRequests.deleteHabitsWithUser)) {
            statement.setLong(1, habitId);
            statement.execute();
        } catch (Exception e) {
            log.error("Exception in HabitRepositoryJdbcImpl.deleteWithUser(). ", e);
        }
    }

    @Override
    public List<Habit> findByUser(User user) {
        List<Habit> habits = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(JdbcRequests.findByUser)) {
            statement.setInt(1, user.getId());
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Habit habit = returnHabit(resultSet, user);
                    habits.add(habit);
                }
            }
        } catch (Exception e) {
            log.error("Exception in UserStoreJdbcImpl.findAll(). ", e);
        }
        return habits;
    }

    @Override
    public List<Habit> findByUserForToday(User user) {
        List<Habit> habits = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(JdbcRequests.findByUserForToday)) {
            statement.setInt(1, user.getId());
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Habit habit = returnHabit(resultSet, user);
                    habits.add(habit);
                }
            }
        } catch (Exception e) {
            log.error("Exception in UserStoreJdbcImpl.findAll(). ", e);
        }
        return habits;
    }

    @Override
    public Habit findById(long habitId) {
        Habit habit = null;
        try (PreparedStatement statement = connection.prepareStatement(JdbcRequests.findHabitById)) {
            statement.setLong(1, habitId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    User user = userRepository.findById(resultSet.getInt("user_id"));
                    habit = returnHabit(resultSet, user);
                }
            }
        } catch (Exception e) {
            log.error("Exception in HabitRepositoryJdbcImpl.findById(). ", e);
        }
        return habit;
    }

    @Override
    public Habit updateByUser(long habitId, String newUsefulness, String newActive, String newName, String newDescription, String newFrequency) {
        String request = getRequestForUpdateByUser(newUsefulness, newActive, newName, newDescription, newFrequency);
        try (PreparedStatement statement = connection.prepareStatement(
                "UPDATE ht_schema.habits SET " + request +
                        " WHERE id = ?")
        ) {
            int i = generateStatementSets(statement, 0, newUsefulness, newActive, newName, newDescription, "", newFrequency);
            i++;
            statement.setLong(i, habitId);
            statement.execute();
        } catch (Exception e) {
            log.error("Exception in UserRepositoryJdbcImpl.update(). ", e);
        }
        return findById(habitId);
    }

    @Override
    public Habit updateViaPerform(Habit habit) {
        return null;
    }

    @Override
    public Habit delete(Habit habit) {
        return null;
    }

    @Override
    public List<Habit> findByParameters(User user, String usefulness, String active, String name, String description, String dateOfCreate, String frequency) {
        if (usefulness.isEmpty() && active.isEmpty() && name.isEmpty() && description.isEmpty() && dateOfCreate.isEmpty() && frequency.isEmpty()) {
            return findByUser(user);
        }
        String request = getRequestForFindByParam(usefulness, active, name, description, dateOfCreate, frequency);
        List<Habit> habits = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(
                "SELECT * FROM cs_schema.users WHERE " + request)
        ) {
            generateStatementSets(statement, user.getId(), usefulness, active, name, description, dateOfCreate, frequency);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Habit habit = returnHabit(resultSet, user);
                    habits.add(habit);
                }
            }
        } catch (Exception e) {
            log.error("Exception in UserStoreJdbcImpl.findByParameters(). ", e);
        }
        return habits;
    }

    private Habit returnHabit(ResultSet resultSet, User user) throws SQLException {
        int daysAmount = resultSet.getInt("frequency");
        return new Habit(
                resultSet.getLong("id"),
                user,
                resultSet.getBoolean("useful"),
                resultSet.getBoolean("active"),
                resultSet.getInt("streaks_amount"),
                resultSet.getString("name"),
                resultSet.getString("description"),
                resultSet.getDate("date_of_create").toLocalDate(),
                resultSet.getDate("planned_first_perform").toLocalDate(),
                resultSet.getDate("planned_prev_perform").toLocalDate(),
                resultSet.getDate("planned_next_perform").toLocalDate(),
                resultSet.getDate("last_real_perform").toLocalDate(),
                Period.of(0, daysAmount / 30, daysAmount % 30),
                resultSet.getInt("performs_amount")
        );
    }

    private String getRequestForUpdateByUser(String newUsefulness, String newActive, String newName, String newDescription, String newFrequency) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append(!newUsefulness.isEmpty() ? "usefulness = ? ," : "")
                .append(newActive.isEmpty() ? " active = ? , " : "")
                .append(!newName.isEmpty() ? " name = ? ," : "")
                .append(!newDescription.isEmpty() ? " description = ? ," : "")
                .append(!newFrequency.isEmpty() ? " frequency = ? " : "");
        if (stringBuilder.toString().endsWith(",")) stringBuilder.setLength(stringBuilder.length() - 1);

        return stringBuilder.toString();
    }

    private String getRequestForFindByParam(String usefulness, String active, String name, String description, String dateOfCreate, String frequency) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder
                .append("user_id = ?")
                .append(!usefulness.isEmpty() ? " usefulness = ? and" : "")
                .append(!active.isEmpty() ? " active = ?" : "")
                .append(!name.isEmpty() ? " name = ? and" : "")
                .append(!description.isEmpty() ? " description = ?" : "")
                .append(!dateOfCreate.isEmpty() ? " date_of_create = ? and" : "")
                .append(!frequency.isEmpty() ? " frequency = ?" : "");
        if (stringBuilder.toString().endsWith("and")) stringBuilder.setLength(stringBuilder.length() - 3);

        return stringBuilder.toString();
    }

    private int generateStatementSets(PreparedStatement statement,
                                      int userId,
                                      String newUsefulness,
                                      String newActive,
                                      String newName,
                                      String newDescription,
                                      String dateOfCreate,
                                      String newFrequency) throws SQLException, IOException {
        int result = 0;
        if (userId != 0) {
            result++;
            statement.setInt(result, userId);
        }
        if (!newUsefulness.isEmpty()) {
            result++;
            statement.setBoolean(result, newUsefulness.equals("true"));
        }
        if (!newActive.isEmpty()) {
            result++;
            statement.setBoolean(result, newActive.equals("true"));
        }
        if (!newName.isEmpty()) {
            result++;
            statement.setString(result, newName);
        }
        if (!newDescription.isEmpty()) {
            result++;
            statement.setString(result, newDescription);
        }
        if (!dateOfCreate.isEmpty()) {
            result++;
            statement.setString(result, dateOfCreate);
        }
        if (!newFrequency.isEmpty()) {
            result++;
            statement.setInt(result, Integer.parseInt(newFrequency));
        }
        return result;
    }
}
