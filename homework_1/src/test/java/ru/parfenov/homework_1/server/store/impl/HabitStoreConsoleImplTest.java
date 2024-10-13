package ru.parfenov.homework_1.server.store.impl;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.parfenov.homework_1.server.model.Habit;
import ru.parfenov.homework_1.server.model.User;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class HabitStoreConsoleImplTest {

    @Test
    @DisplayName("Успешное удаление привычки")
    public void test_delete_habit_by_id() {
        HabitStoreConsoleImpl store = new HabitStoreConsoleImpl();
        Habit habit = new Habit();
        habit.setName("Exercise");
        store.create(habit);
        Habit deletedHabit = store.delete(1L);
        assertNotNull(deletedHabit);
        assertEquals(1L, deletedHabit.getId());
        assertNull(store.findById(1L));
    }

    @Test
    @DisplayName("Поиск привычек по юзеру")
    public void test_find_habits_by_user() {
        HabitStoreConsoleImpl store = new HabitStoreConsoleImpl();
        User user = new User(1, "user@example.com", "password",
                "User", null, false);
        Habit habit1 = new Habit();
        habit1.setUser(user);
        store.create(habit1);
        Habit habit2 = new Habit();
        habit2.setUser(user);
        store.create(habit2);
        List<Habit> habits = store.findByUser(user);
        assertEquals(2, habits.size());
    }

    @Test
    @DisplayName("Поиск привычки по несуществующему ID   ")
    public void test_delete_nonexistent_habit_id() {
        HabitStoreConsoleImpl store = new HabitStoreConsoleImpl();
        Habit deletedHabit = store.delete(999L);
        assertNull(deletedHabit);
    }

    @Test
    @DisplayName("Поиск привычек по юзеру без привычек")
    public void test_find_habits_for_user_with_no_habits() {
        HabitStoreConsoleImpl store = new HabitStoreConsoleImpl();
        User user = new User(1, "user@example.com", "password",
                "User", null, false);
        List<Habit> habits = store.findByUser(user);
        assertTrue(habits.isEmpty());
    }

    @Test
    @DisplayName("Поиск привычки по несуществующему ID")
    public void test_find_nonexistent_habit_id() {
        HabitStoreConsoleImpl store = new HabitStoreConsoleImpl();
        Habit foundHabit = store.findById(999L);
        assertNull(foundHabit);
    }
}