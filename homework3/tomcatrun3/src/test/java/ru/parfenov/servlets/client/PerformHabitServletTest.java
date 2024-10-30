package ru.parfenov.servlets.client;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.postgresql.shaded.com.ongres.scram.common.bouncycastle.base64.Base64;
import ru.parfenov.enums.user.Role;
import ru.parfenov.model.Habit;
import ru.parfenov.model.User;
import ru.parfenov.service.HabitService;
import ru.parfenov.service.UserService;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDate;
import java.time.Period;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class PerformHabitServletTest {
    public static UserService userService;
    public static PrintWriter writer;
    public static PerformHabitServlet servlet;
    public static User user;
    private static HabitService habitService;

    @BeforeAll
    public static void init() {
        userService = mock(UserService.class);
        habitService = mock(HabitService.class);
        writer = new PrintWriter(new StringWriter());
        servlet = new PerformHabitServlet(userService, habitService);
        user = new User(2, "name1@mail.ru", "password1", "1111", "Name1", Role.CLIENT, false);
    }

    @Test
    @DisplayName("CLIENT успешно выполнил привычку")
    public void client_perform_habit() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        Period frequency = Period.of(0, 0, 10);
        Habit performedHabit = new Habit(
                1L, user, true, true, 1, "run", "run everyday", LocalDate.now().minusDays(frequency.getDays()), LocalDate.now(),
                LocalDate.now(), LocalDate.now().plusDays(frequency.getDays()), LocalDate.now(), frequency, 0);

        authentication(user, request);

        when(request.getParameter("habitId")).thenReturn("1");
        when(habitService.perform("1")).thenReturn(Optional.of(performedHabit));
        when(response.getWriter()).thenReturn(writer);

        servlet.doGet(request, response);

        verify(response).setContentType("application/json");
        verify(response).setCharacterEncoding("UTF-8");
        verify(response).setStatus(200);
    }

    private void authentication(User user, HttpServletRequest request) {
        when(request.getHeader("Authorization")).thenReturn("Basic QWxhZGRpbjpPcGVuU2VzYW1l");
        byte[] e = Base64.decode(request.getHeader("Authorization").substring(6));
        String emailNPass = new String(e);
        String email = emailNPass.substring(0, emailNPass.indexOf(":"));
        String password = emailNPass.substring(emailNPass.indexOf(":") + 1);
        when(userService.findByEmailAndPassword(email, password)).thenReturn(Optional.of(user));
    }
}