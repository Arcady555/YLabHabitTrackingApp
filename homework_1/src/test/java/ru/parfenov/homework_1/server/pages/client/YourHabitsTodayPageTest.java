package ru.parfenov.homework_1.server.pages.client;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import ru.parfenov.homework_1.server.model.Habit;
import ru.parfenov.homework_1.server.model.User;
import ru.parfenov.homework_1.server.service.HabitService;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class YourHabitsTodayPageTest {

    @Test
    @DisplayName("Успешный вывод remind")
    public void test_run_calls_remind_for_each_habit() throws IOException, InterruptedException {
        User user = new User(1, "test@example.com", "password", "1", "Test User", null, false);
        HabitService habitService = mock(HabitService.class);
        Habit habit1 = new Habit();
        habit1.setId(1L);
        habit1.setUser(user);
        Habit habit2 = new Habit();
        habit2.setId(2L);
        habit2.setUser(user);
        List<Habit> habits = List.of(habit1, habit2);
        when(habitService.todayPerforms(user)).thenReturn(habits);
        YourHabitsTodayPage page = new YourHabitsTodayPage(user, habitService);
        page.run();
        verify(habitService).remind(1L);
        verify(habitService).remind(2L);
    }

    @Test
    @DisplayName("Успешный вывод разных привычек")
    public void test_run_handles_multiple_habits() throws IOException, InterruptedException {
        User user = new User(1, "test@example.com", "password", "1", "Test User", null, false);
        HabitService habitService = mock(HabitService.class);
        Habit habit1 = new Habit();
        habit1.setId(1L);
        habit1.setUser(user);
        Habit habit2 = new Habit();
        habit2.setId(2L);
        habit2.setUser(user);
        Habit habit3 = new Habit();
        habit3.setId(3L);
        habit3.setUser(user);
        List<Habit> habits = List.of(habit1, habit2, habit3);
        when(habitService.findByUser(user)).thenReturn(habits);
        YourHabitsTodayPage page = new YourHabitsTodayPage(user, habitService);
        page.run();
    }

    @Test
    @DisplayName("Корректный вывод всех деталей привычки")
    public void test_run_outputs_correct_habit_details() throws IOException, InterruptedException {
        User user = new User(1, "test@example.com", "password", "1", "Test User", null, false);
        HabitService habitService = mock(HabitService.class);
        Habit habit = new Habit();
        habit.setName("Exercise");
        habit.setUser(user);
        when(habitService.findByUser(user)).thenReturn(List.of(habit));
        YourHabitsTodayPage page = new YourHabitsTodayPage(user, habitService);
        page.run();
        assertEquals("Habit{id=0, user=test@example.com, usefulness=false, active=false, streaksAmount=0," +
                        " name='Exercise', description='null', dateOfCreate=null, plannedFirstPerform=null, " +
                        "plannedPrevPerform=null, plannedNextPerform=null, lastRealPerform=null, frequency=null, performsAmount=0}",
                habit.toString());
    }

    @Test
    @DisplayName("У пользователя нет привычек - корректный вывод")
    public void test_run_handles_no_habits_gracefully() throws IOException, InterruptedException {
        User user = new User(1, "test@example.com", "password", "1", "Test User", null, false);
        HabitService habitService = mock(HabitService.class);
        when(habitService.findByUser(user)).thenReturn(List.of());
        YourHabitsTodayPage page = new YourHabitsTodayPage(user, habitService);
        page.run();
    }
}