package ru.parfenov.repository.impl;

import lombok.extern.slf4j.Slf4j;
import ru.parfenov.model.Habit;
import ru.parfenov.model.User;
import ru.parfenov.repository.HabitRepository;
import ru.parfenov.repository.UserRepository;
import ru.parfenov.utility.JdbcRequests;
import ru.parfenov.utility.Utility;

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

    public HabitRepositoryJdbcImpl(Connection connection, UserRepository userRepository) {
        this.userRepository = userRepository;
        this.connection = connection;
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
            statement.setDate(9, null);
            statement.setDate(10, Date.valueOf(habit.getPlannedNextPerform()));
            statement.setDate(11, null);
            statement.setInt(12, (habit.getFrequency().getDays()));
            statement.setInt(13, habit.getPerformsAmount());
            statement.execute();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    habit.setId(generatedKeys.getInt(1));
                }
            }
        } catch (Exception e) {
            log.error("Exception in HabitRepositoryJdbcImpl.create(). ", e);
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
    public void deleteWithUser(int userId) {
        try (PreparedStatement statement = connection.prepareStatement(JdbcRequests.deleteHabitsWithUser)) {
            statement.setInt(1, userId);
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
            log.error("Exception in HabitRepositoryJdbcImpl.findByUser(). ", e);
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
            log.error("Exception in HabitRepositoryJdbcImpl.findByUserForToday(). ", e);
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

    /**
     * Любой запрос в БД - дорогое удовольствие. Попытаемся уменьшить его вес за счет дополнительной
     * суеты в стеке, ведь некоторые параметры для замены могут не заполнить.
     *
     * @param habitId id привычки, которую надо обновить
     * @param newUsefulness полезность
     * @param newActive активность
     * @param newName название
     * @param newDescription описание
     * @param newFrequency частота
     * @return привычка с обновлёнными полями
     */
    @Override
    public Habit updateByUser(long habitId, String newUsefulness, String newActive, String newName, String newDescription, int newFrequency) {
        try (PreparedStatement statement = connection
                .prepareStatement(JdbcRequests.updateHabitByUser(newUsefulness, newActive, newName, newDescription, newFrequency))) {
            int i = generateStatementSets(statement, 0, newUsefulness, newActive, newName, newDescription, "", newFrequency);
            i++;
            statement.setLong(i, habitId);
            statement.execute();
        } catch (Exception e) {
            log.error("Exception in HabitRepositoryJdbcImpl.updateByUser(). ", e);
        }
        return findById(habitId);
    }

    @Override
    public Habit updateViaPerform(Habit habit) {
        try (PreparedStatement statement = connection.prepareStatement(JdbcRequests.updateHabitViaPerform)) {
            statement.setInt(1, habit.getStreaksAmount());
            statement.setDate(2, Date.valueOf(habit.getPlannedPrevPerform()));
            statement.setDate(3, Date.valueOf(habit.getPlannedNextPerform()));
            statement.setDate(4, Date.valueOf(habit.getLastRealPerform()));
            statement.setInt(5, habit.getPerformsAmount());
            statement.setLong(6, habit.getId());
            statement.execute();
        } catch (Exception e) {
            log.error("Exception in HabitRepositoryJdbcImpl.updateViaPerform(). ", e);
        }
        return findById(habit.getId());
    }

    /**
     * Любой запрос в БД - дорогое удовольствие. Попытаемся уменьшить его вес за счет дополнительной
     * суеты в стеке, ведь некоторые параметры поиска могут не заполнить.
     *
     * @param user Модель -user
     * @param usefulness полезность
     * @param active активность
     * @param name название
     * @param description описание
     * @param dateOfCreate дата создания
     * @param frequencyStr частота выполнения
     * @return список привычек юзера по заданным параметрам
     */
    @Override
    public List<Habit> findByParameters(
            User user, String usefulness, String active, String name, String description, String dateOfCreate, String frequencyStr
    ) {
        if (
                usefulness.isEmpty() &&
                        active.isEmpty() &&
                        name.isEmpty() &&
                        description.isEmpty()
                        && dateOfCreate.isEmpty()
                        && frequencyStr.isEmpty()
        ) {
            return findByUser(user);
        }
        List<Habit> habits = new ArrayList<>();
        int frequency = Utility.getIntFromString(frequencyStr);
        try (
                PreparedStatement statement =
                        connection
                                .prepareStatement(
                                        JdbcRequests.findHabitsByParameters(usefulness, active, name, description, dateOfCreate, frequency)
                                )
        ) {
            generateStatementSets(statement, user.getId(), usefulness, active, name, description, dateOfCreate, frequency);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Habit habit = returnHabit(resultSet, user);
                    habits.add(habit);
                }
            }
        } catch (Exception e) {
            log.error("Exception in HabitRepositoryJdbcImpl.findByParameters(). ", e);
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
                resultSet.getDate("planned_prev_perform") != null ? resultSet.getDate("planned_prev_perform").toLocalDate() : null,
                resultSet.getDate("planned_next_perform").toLocalDate(),
                resultSet.getDate("last_real_perform") != null ? resultSet.getDate("last_real_perform").toLocalDate() : null,
                Period.of(0, daysAmount / 30, daysAmount % 30),
                resultSet.getInt("performs_amount")
        );
    }

    private int generateStatementSets(PreparedStatement statement,
                                      int userId,
                                      String newUsefulness,
                                      String newActive,
                                      String newName,
                                      String newDescription,
                                      String dateOfCreate,
                                      int newFrequency) throws SQLException {
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
        if (newFrequency != 0) {
            result++;
            statement.setInt(result, newFrequency);
        }
        return result;
    }
}