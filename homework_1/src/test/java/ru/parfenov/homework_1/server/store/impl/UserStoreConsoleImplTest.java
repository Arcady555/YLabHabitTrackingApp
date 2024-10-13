package ru.parfenov.homework_1.server.store.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.parfenov.homework_1.server.enums.user.Role;
import ru.parfenov.homework_1.server.model.User;
import ru.parfenov.homework_1.server.utility.Utility;

import static org.junit.jupiter.api.Assertions.*;

public class UserStoreConsoleImplTest {

    @Test
    @DisplayName("Успешное создание юзера")
    public void test_create_user_adds_to_user_map_with_unique_id() {
        UserStoreConsoleImpl userStore = new UserStoreConsoleImpl();
        User newUser = new User(0, "test@example.com", "password",
                "Test User", Role.CLIENT, false);
        userStore.create(newUser);
        assertNotNull(userStore.findById(newUser.getId()));
    }

    @Test
    @DisplayName("Успешный поиск юзера по емайл")
    public void test_find_by_email_returns_correct_user() {
        UserStoreConsoleImpl userStore = new UserStoreConsoleImpl();
        User newUser = new User(0, "test@example.com", "password",
                "Test User", Role.CLIENT, false);
        userStore.create(newUser);
        User foundUser = userStore.findByEmail("test@example.com");
        assertEquals(newUser.getEmail(), foundUser.getEmail());
    }

    @Test
    @DisplayName("Успешный поиск юзера по ID")
    public void test_find_by_id_returns_correct_user() {
        UserStoreConsoleImpl userStore = new UserStoreConsoleImpl();
        User newUser = new User(0, "test@example.com", "password",
                "Test User", Role.CLIENT, false);
        userStore.create(newUser);
        User foundUser = userStore.findById(newUser.getId());
        assertEquals(newUser.getId(), foundUser.getId());
    }

    @Test
    @DisplayName("Успешный поиск админа по емайл")
    public void test_find_user_by_email() {
        UserStoreConsoleImpl userStore = new UserStoreConsoleImpl();
        User user = userStore.findByEmail(Utility.adminEmail);
        assertNotNull(user);
        assertEquals(Utility.adminEmail, user.getEmail());
    }

    @Test
    @DisplayName("Успешное обновление юзера")
    public void test_update_user_replaces_existing_data() {
        UserStoreConsoleImpl userStore = new UserStoreConsoleImpl();
        User newUser = new User(0, "test@example.com", "password",
                "Test User", Role.CLIENT, false);
        userStore.create(newUser);
        newUser.setName("Updated Name");
        userStore.update(newUser);
        assertEquals("Updated Name",
                userStore.findById(newUser.getId()).getName());
    }

    @Test
    @DisplayName("Поиск юзера по несуществующему емайл")
    public void test_find_by_non_existent_email_returns_null() {
        UserStoreConsoleImpl userStore = new UserStoreConsoleImpl();
        assertNull(userStore.findByEmail("nonexistent@example.com"));
    }

    @Test
    @DisplayName("Поиск юзера по несуществующему ID")
    public void test_find_by_non_existent_id_returns_null() {
        UserStoreConsoleImpl userStore = new UserStoreConsoleImpl();
        assertNull(userStore.findById(999));
    }

    @Test
    @DisplayName("Обновление несуществующего юзера")
    public void test_update_non_existent_user_returns_null() {
        UserStoreConsoleImpl userStore = new UserStoreConsoleImpl();
        User nonExistentUser = new User(999,
                "nonexistent@example.com", "password", "Nonexistent", Role.CLIENT,
                false);
        assertNull(userStore.update(nonExistentUser));
    }
}