package ru.parfenov.server.pages.admin;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.parfenov.server.enums.user.Role;
import ru.parfenov.server.model.Habit;
import ru.parfenov.server.model.User;
import ru.parfenov.server.service.HabitService;
import ru.parfenov.server.service.UserService;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class HabitsOfUserPageTest {

    @Test
    @DisplayName("Найден юзер по емайл и вывод его списка привычек")
    public void test_user_found_and_habits_listed() throws
            IOException, InterruptedException {
        UserService userService = mock(UserService.class);
        HabitService habitService = mock(HabitService.class);
        BufferedReader reader = mock(BufferedReader.class);
        HabitsOfUserPage page = new HabitsOfUserPage(userService, habitService);
        page.reader = reader;

        User user = new User(1, "test@example.com", "password", "1", "Test User", null, false);
        Habit habit = new Habit(1, user, true, true, 0, "Test Habit", "Description", LocalDate.now(), LocalDate.now(), LocalDate.now(), LocalDate.now(), LocalDate.now(), Period.ofDays(1), 0);

        when(reader.readLine()).thenReturn("test@example.com");
        when(userService.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(habitService.findByUser(user)).thenReturn(List.of(habit));

        page.run();

        verify(habitService).findByUser(user);
    }

    @Test
    @DisplayName("Удаление 1ой привычки у юзера")
    public void test_user_deletes_habit_successfully() throws
            IOException, InterruptedException {
        UserService userService = mock(UserService.class);
        HabitService habitService = mock(HabitService.class);
        BufferedReader reader = mock(BufferedReader.class);
        HabitsOfUserPage page = new HabitsOfUserPage(userService, habitService);
        page.reader = reader;

        User user = new User(1, "test@example.com", "password", "1", "Test User", null, false);

        when(reader.readLine()).thenReturn("test@example.com", "0", "1");
        when(userService.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(habitService.delete(1L)).thenReturn(true);

        page.run();

        verify(habitService).delete(1L);
    }

    @Test
    @DisplayName("Юзер ввел валидный ID привычки для удаления.")
    public void test_valid_habit_id_for_deletion() throws IOException,
            InterruptedException {
        UserService userService = mock(UserService.class);
        HabitService habitService = mock(HabitService.class);
        BufferedReader reader = mock(BufferedReader.class);
        HabitsOfUserPage page = new HabitsOfUserPage(userService, habitService);
        page.reader = reader;

        User user = new User(1, "test@example.com", "password", "1", "Test User", null, false);

        when(reader.readLine()).thenReturn("test@example.com", "0", "2");
        when(userService.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(habitService.delete(2L)).thenReturn(true);

        page.run();

        verify(habitService).delete(2L);
    }

    @Test
    @DisplayName(" Емайл не найден")
    public void test_user_email_not_found() throws IOException,
            InterruptedException {
        UserService userService = mock(UserService.class);
        HabitService habitService = mock(HabitService.class);
        BufferedReader reader = mock(BufferedReader.class);
        HabitsOfUserPage page = new HabitsOfUserPage(userService, habitService);
        page.reader = reader;

        when(reader.readLine()).thenReturn("unknown@example.com");
        when(userService.findByEmail("unknown@example.com")).thenReturn(Optional.empty());

        page.run();

        verify(habitService, never()).findByUser(any());
    }

    @Test
    @DisplayName("Ввод пустой строки вместо  ID привычки")
    public void test_empty_habit_id_input() throws IOException,
            InterruptedException {
        UserService userService = mock(UserService.class);
        HabitService habitService = mock(HabitService.class);
        BufferedReader reader = mock(BufferedReader.class);
        HabitsOfUserPage page = new HabitsOfUserPage(userService, habitService);
        page.reader = reader;
        User user = new User(1, "test@example.com", "password", "1", "Test User", null, false);

        when(reader.readLine()).thenReturn("test@example.com", "0", "");
        when(userService.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        page.run();

        verify(habitService, never()).delete(anyLong());
    }

    @Test
    @DisplayName("Отказ удалить привычку")
    public void test_no_habits_deleted_continues_operation() throws
            IOException, InterruptedException {
        UserService userService = mock(UserService.class);
        HabitService habitService = mock(HabitService.class);
        BufferedReader reader = mock(BufferedReader.class);
        HabitsOfUserPage page = new HabitsOfUserPage(userService, habitService);
        page.reader = reader;

        User user = new User(1, "test@example.com", "password",
                "reset", "Test User", Role.CLIENT, false);

        when(reader.readLine()).thenReturn("test@example.com", "1");
        when(userService.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        page.run();

        verify(habitService, never()).delete(anyLong());
    }

    @Test
    @DisplayName("Согласие удалить привычку")
    public void test_handles_user_input_and_prompts_for_deletion()
            throws IOException, InterruptedException {
        UserService userService = mock(UserService.class);
        HabitService habitService = mock(HabitService.class);
        BufferedReader reader = mock(BufferedReader.class);
        HabitsOfUserPage page = new HabitsOfUserPage(userService, habitService);
        page.reader = reader;

        User user = new User(1, "test@example.com", "password",
                "reset", "Test User", Role.CLIENT, false);

        when(reader.readLine()).thenReturn("test@example.com", "0");
        when(userService.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        page.run();

        verify(reader, times(3)).readLine();
    }

    @Test
    @DisplayName("Ввод несуществующего емайл")
    public void test_non_existent_user_email_handling() throws
            IOException, InterruptedException {
        UserService userService = mock(UserService.class);
        HabitService habitService = mock(HabitService.class);
        BufferedReader reader = mock(BufferedReader.class);
        HabitsOfUserPage page = new HabitsOfUserPage(userService, habitService);
        page.reader = reader;

        when(reader.readLine()).thenReturn("nonexistent@example.com");
        when(userService.findByEmail("nonexistent@example.com")).thenReturn(Optional.empty());

        page.run();

        verify(habitService, never()).findByUser(any());
    }

    @Test
    @DisplayName("Ввод недопустимого ID")
    public void test_invalid_habit_id_input_handling() throws
            IOException, InterruptedException {
        UserService userService = mock(UserService.class);
        HabitService habitService = mock(HabitService.class);
        BufferedReader reader = mock(BufferedReader.class);
        HabitsOfUserPage page = new HabitsOfUserPage(userService, habitService);
        page.reader = reader;

        User user = new User(1, "test@example.com", "password",
                "reset", "Test User", Role.CLIENT, false);

        when(reader.readLine()).thenReturn("test@example.com", "0", "invalid");
        when(userService.findByEmail("test@example.com")).thenReturn(Optional.of(user));

        page.run();

        verify(habitService, never()).delete(anyLong());
    }
}