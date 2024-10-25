package ru.parfenov.server.repository.impl;

import lombok.extern.slf4j.Slf4j;
import ru.parfenov.server.enums.user.Role;
import ru.parfenov.server.model.User;
import ru.parfenov.server.repository.UserRepository;
import ru.parfenov.server.utility.JdbcRequests;
import ru.parfenov.server.utility.Utility;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
public class UserRepositoryJdbcImpl implements UserRepository {
    private final Connection connection;

    public UserRepositoryJdbcImpl() throws Exception {
        this.connection = Utility.loadConnection();
    }

    public UserRepositoryJdbcImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public User create(User user) {
        try (PreparedStatement statement = connection.prepareStatement(
                JdbcRequests.createUser,
                Statement.RETURN_GENERATED_KEYS)
        ) {
            statement.setString(1, user.getEmail());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getResetPassword());
            statement.setString(4, user.getName());
            statement.setString(5, user.getRole().toString());
            statement.setBoolean(6, user.isBlocked());
            statement.execute();
            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getInt(1));
                }
            }
        } catch (Exception e) {
            log.error("Exception in UserRepositoryJdbcImpl.create(). ", e);
        }
        return user;
    }

    @Override
    public User findByEmail(String email) {
        User user = null;
        try (PreparedStatement statement = connection.prepareStatement(JdbcRequests.findUserByEmail)) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    user = returnUser(resultSet);
                }
            }
        } catch (Exception e) {
            log.error("Exception in UserRepositoryJdbcImpl.findByEmail(). ", e);
        }
        return user;
    }

    @Override
    public User findById(int userId) {
        User user = null;
        try (PreparedStatement statement = connection.prepareStatement(JdbcRequests.findUserById)) {
            statement.setInt(1, userId);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    user = returnUser(resultSet);
                }
            }
        } catch (Exception e) {
            log.error("Exception in UserRepositoryJdbcImpl.findById(). ", e);
        }
        return user;
    }

    @Override
    public User findByEmailAndPassword(String email, String password) {
        User user = null;
        try (PreparedStatement statement = connection.prepareStatement(JdbcRequests.findUserByEmailAndPassword)) {
            statement.setString(1, email);
            statement.setString(2, password);
            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    user = returnUser(resultSet);
                }
            }
        } catch (Exception e) {
            log.error("Exception in UserRepositoryJdbcImpl.findById(). ", e);
        }
        return user;
    }

    /**
     * Любой запрос в БД - дорогое удовольствие. Пусть вес его уменьшится, если некоторые поля запроса будут пустыми
     *
     * @param userId      ID юзера, вокруг которого весь процесс
     * @param newPassword новый пароль, может быть пустая строка, если не запросили его изменение
     * @param newName     новое имя, может быть пустая строка, если не запросили его изменение
     * @param newUserRole новая роль, может быть null, если не запросили её изменение
     * @param blocked     блокировка, может быть пустая строка, если не запросили её изменение
     * @return юзер с обновлёнными данными
     */
    @Override
    public User update(int userId, String newPassword, String newResetPassword, String newName, Role newUserRole, String blocked) {
        try (PreparedStatement statement = connection.prepareStatement(
                JdbcRequests.updateUser(newPassword, newResetPassword, newName, newUserRole, blocked)
        )
        ) {
            int i = generateStatementSets(statement, newPassword, newResetPassword, newName, newUserRole, blocked);
            i++;
            statement.setInt(i, userId);
            statement.execute();
        } catch (Exception e) {
            log.error("Exception in UserRepositoryJdbcImpl.update(). ", e);
        }
        return findById(userId);
    }

    @Override
    public void delete(int userId) {
        try (PreparedStatement statement = connection.prepareStatement(JdbcRequests.deleteUser)) {
            statement.setInt(1, userId);
            statement.execute();
        } catch (Exception e) {
            log.error("Exception in UserRepositoryJdbcImpl.delete(). ", e);
        }
    }

    @Override
    public List<User> findAll() {
        List<User> users = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(JdbcRequests.findAllUsers)) {
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    User user = returnUser(resultSet);
                    users.add(user);
                }
            }
        } catch (Exception e) {
            log.error("Exception in UserRepositoryJdbcImpl.findAll(). ", e);
        }
        return users;
    }

    /**
     * Любой запрос в БД - дорогое удовольствие. Пусть вес его уменьшится, если некоторые поля запроса будут пустыми
     *
     * @param roleStr роль, может быть пустая строка, если не запросили поиск по такому параметру
     * @param name    имя, может быть пустая строка, если не запросили поиск по такому параметру
     * @param block   блокировка, может быть пустая строка, если не запросили поиск по такому параметру
     * @return список юзеров по переданным параметрам
     */
    @Override
    public List<User> findByParameters(String roleStr, String name, String block) {
        if (roleStr.isEmpty() && name.isEmpty() && block.isEmpty()) {
            return findAll();
        }
        List<User> users = new ArrayList<>();
        Role role = getUserRoleFromString(roleStr);
        try (PreparedStatement statement = connection.prepareStatement(
                JdbcRequests.findUsersByParameters(role, name, block))
        ) {
            generateStatementSets(statement, "", "", name, role, block);
            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    User user = returnUser(resultSet);
                    users.add(user);
                }
            }
        } catch (Exception e) {
            log.error("Exception in UserRepositoryJdbcImpl.findByParameters(). ", e);
        }
        return users;
    }

    private User returnUser(ResultSet resultSet) throws SQLException {
        return new User(
                resultSet.getInt("id"),
                resultSet.getString("email"),
                resultSet.getString("password"),
                resultSet.getString("reset_password"),
                resultSet.getString("name"),
                getUserRoleFromString(resultSet.getString("user_role")),
                resultSet.getBoolean("blocked")
        );
    }

    private Role getUserRoleFromString(String str) {
        return ("ADMIN".equals(str)) ? Role.ADMIN : Role.CLIENT;
    }

    private int generateStatementSets(PreparedStatement statement,
                                      String newPassword,
                                      String newResetPassword,
                                      String newName,
                                      Role newUserRole,
                                      String newBlocked) throws SQLException {
        int result = 0;
        if (!newPassword.isEmpty()) {
            result++;
            statement.setString(result, newPassword);
        }
        if (!newResetPassword.isEmpty()) {
            result++;
            statement.setString(result, newResetPassword);
        }
        if (!newName.isEmpty()) {
            result++;
            statement.setString(result, newName);
        }
        if (newUserRole != null) {
            result++;
            statement.setString(result, newUserRole.toString());
        }
        if (!newBlocked.isEmpty()) {
            result++;
            statement.setBoolean(result, newBlocked.equals("true"));
        }
        return result;
    }
}