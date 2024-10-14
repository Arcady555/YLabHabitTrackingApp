package ru.parfenov.homework_1.server.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.parfenov.homework_1.server.model.Habit;
import ru.parfenov.homework_1.server.model.User;
import ru.parfenov.homework_1.server.store.HabitStore;

import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;

public class HabitServiceConsoleImplTest {

    @Test
    @DisplayName("Успешное создание привычки")
    public void test_create_habit_success() {
        HabitStore habitStore = mock(HabitStore.class);
        HabitServiceConsoleImpl service = new
                HabitServiceConsoleImpl(habitStore);
        User user = new User(1, "test@example.com", "password", "1", "Test User", null, false);
                LocalDate dateOfCreate = LocalDate.now();
        LocalDate firstPerform = dateOfCreate.plusDays(1);
        Period frequency = Period.ofDays(1);
        Habit habit = new Habit(0L, user, true, true, 1, "Test Habit",
                "Description", dateOfCreate, firstPerform, null, firstPerform, null,
                frequency, 0);

        when(habitStore.create(any(Habit.class))).thenReturn(habit);

        Habit createdHabit = service.create(user, true, "Test Habit",
                "Description", dateOfCreate, firstPerform, frequency);

        assertNotNull(createdHabit);
        assertEquals("Test Habit", createdHabit.getName());
        verify(habitStore).create(any(Habit.class));
    }

    @Test
    @DisplayName("Успешное удаление привычки")
    public void test_delete_habit_success() {
        HabitStore habitStore = mock(HabitStore.class);
        HabitServiceConsoleImpl service = new
                HabitServiceConsoleImpl(habitStore);
        long habitId = 1L;

        when(habitStore.delete(habitId)).thenReturn(new Habit());

        boolean result = service.delete(habitId);

        assertTrue(result);
        verify(habitStore).delete(habitId);
    }

    @Test
    @DisplayName("Найти привычку по  ID")
    public void test_find_habit_by_id_success() {
        HabitStore habitStore = mock(HabitStore.class);
        HabitServiceConsoleImpl service = new
                HabitServiceConsoleImpl(habitStore);
        long habitId = 1L;
        Habit habit = new Habit();
        habit.setId(habitId);

        when(habitStore.findById(habitId)).thenReturn(habit);

        Optional<Habit> foundHabit = service.findById(habitId);

        assertTrue(foundHabit.isPresent());
        assertEquals(habitId, foundHabit.get().getId());
        verify(habitStore).findById(habitId);
    }

    @Test
    @DisplayName("Выброс исключения при удалении несуществующей привычки")
    public void test_delete_non_existent_habit() {
        HabitStore habitStore = mock(HabitStore.class);
        HabitServiceConsoleImpl service = new
                HabitServiceConsoleImpl(habitStore);
        long habitId = 999L;

        when(habitStore.delete(habitId)).thenReturn(null);

        boolean result = service.delete(habitId);

        assertFalse(result);
        verify(habitStore).delete(habitId);
    }
}