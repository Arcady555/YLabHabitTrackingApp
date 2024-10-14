package ru.parfenov.homework_1.server.pages.client;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import ru.parfenov.homework_1.server.enums.user.Role;
import ru.parfenov.homework_1.server.model.User;
import ru.parfenov.homework_1.server.service.HabitService;

import java.io.BufferedReader;
import java.io.IOException;
import java.time.LocalDate;

import static org.mockito.Mockito.*;

public class CreateHabitPageTest {

    @Test
    @DisplayName("Ввод 0 -> привычка полезная")
    public void test_usefulness_input_zero() throws IOException {
        HabitService habitService = mock(HabitService.class);
        User user = new User(1, "test@example.com", "password", "1", "Test User", Role.CLIENT, false);
        CreateHabitPage createHabitPage = new CreateHabitPage(user, habitService);
        BufferedReader reader = mock(BufferedReader.class);
        createHabitPage.reader = reader;

        when(reader.readLine()).thenReturn("0", "Exercise", "Daily exercise", "2024-10-15", "30");

        createHabitPage.run();

        verify(habitService).create(eq(user), eq(true), anyString(),
                anyString(), any(), any(), any());
    }

    @Test
    @DisplayName("Ввод валидной даты для первого выполнения привычки")
    public void test_valid_future_date_for_first_performance() throws
            IOException {
        HabitService habitService = mock(HabitService.class);
        User user = new User(1, "test@example.com", "password", "1", "Test User", Role.CLIENT, false);
        CreateHabitPage createHabitPage = new CreateHabitPage(user, habitService);
        BufferedReader reader = mock(BufferedReader.class);
        createHabitPage.reader = reader;

        when(reader.readLine()).thenReturn("0", "Exercise", "Daily exercise", "2024-10-15", "30");

        createHabitPage.run();

        verify(habitService).create(any(), anyBoolean(), anyString(),
                anyString(), any(), eq(LocalDate.parse("2024-10-15")), any());
    }
}