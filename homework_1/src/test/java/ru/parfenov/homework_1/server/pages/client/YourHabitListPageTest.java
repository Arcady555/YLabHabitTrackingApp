package ru.parfenov.homework_1.server.pages.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.parfenov.homework_1.server.model.Habit;
import ru.parfenov.homework_1.server.model.User;
import ru.parfenov.homework_1.server.service.HabitService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

public class YourHabitListPageTest {

    @Test
    @DisplayName("Успешный вывод всех привычек")
    public void test_retrieve_and_print_habits() throws IOException, InterruptedException {
        User user = new User(1, "test@example.com", "password", "1", "Test User", null, false);
        HabitService habitService = mock(HabitService.class);
        Habit habit1 = new Habit();
        habit1.setId(1L);
        habit1.setUser(user);
        Habit habit2 = new Habit();
        habit2.setId(2L);
        habit2.setUser(user);
        List<Habit> habits = List.of(habit1, habit2);
        when(habitService.findByUser(user)).thenReturn(habits);
        YourHabitListPage page = new YourHabitListPage(user, habitService);
        page.run();
        verify(habitService, times(1)).findByUser(user);
    }

    @Test
    @DisplayName("Успешный вывод remind")
    public void test_remind_method_called_for_each_habit() throws
            IOException, InterruptedException {
        User user = new User(1, "test@example.com", "password", "1", "Test User", null, false);
        HabitService habitService = mock(HabitService.class);
        Habit habit1 = new Habit();
        habit1.setId(1L);
        habit1.setUser(user);
        Habit habit2 = new Habit();
        habit2.setId(2L);
        habit2.setUser(user);
        List<Habit> habits = List.of(habit1, habit2);
        when(habitService.findByUser(user)).thenReturn(habits);
        when(habitService.remind(anyLong())).thenReturn("Reminder");
        YourHabitListPage page = new YourHabitListPage(user, habitService);
        page.run();
        verify(habitService, times(1)).remind(1L);
        verify(habitService, times(1)).remind(2L);
    }

    @Test
    @DisplayName("Успешный вывод юзера без привычек")
    public void test_no_habits_for_user() throws IOException,
            InterruptedException {
        User user = new User(1, "test@example.com", "password", "1", "Test User", null, false);
        HabitService habitService = mock(HabitService.class);
        when(habitService.findByUser(user)).thenReturn(Collections.emptyList());
        YourHabitListPage page = new YourHabitListPage(user, habitService);
        page.run();
        verify(habitService, times(1)).findByUser(user);
    }

    @Test
    @DisplayName("Успешный вывод юзера с большим списком привычек")
    public void test_large_number_of_habits() throws IOException,
            InterruptedException {
        User user = new User(1, "test@example.com", "password", "1", "Test User", null, false);
        HabitService habitService = mock(HabitService.class);
        List<Habit> habits = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            Habit habit = new Habit();
            habit.setId(i);
            habit.setUser(user);
            habits.add(habit);
        }
        when(habitService.findByUser(user)).thenReturn(habits);
        YourHabitListPage page = new YourHabitListPage(user, habitService);
        page.run();
        verify(habitService, times(100)).remind(anyLong());
    }

    @Test
    @DisplayName("Успешный вывод пустого remind")
    public void test_remind_returns_null_or_empty_string() throws
            IOException, InterruptedException {
        User user = new User(1, "test@example.com", "password", "1", "Test User", null, false);
        HabitService habitService = mock(HabitService.class);
        Habit habit = new Habit();
        habit.setId(1L);
        habit.setUser(user);
        List<Habit> habits = List.of(habit);
        when(habitService.findByUser(user)).thenReturn(habits);
        when(habitService.remind(anyLong())).thenReturn(null);
        YourHabitListPage page = new YourHabitListPage(user, habitService);
        page.run();
        verify(habitService, times(1)).remind(1L);
    }
}