package ru.parfenov.servlets.client;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.postgresql.shaded.com.ongres.scram.common.bouncycastle.base64.Base64;
import ru.parfenov.dto.habit.HabitDTOMapper;
import ru.parfenov.dto.habit.HabitGeneralDTO;
import ru.parfenov.enums.user.Role;
import ru.parfenov.model.Habit;
import ru.parfenov.model.User;
import ru.parfenov.service.HabitService;
import ru.parfenov.service.UserService;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class YourHabitListByParametersServletTest {
    public static UserService userService;
    public static PrintWriter writer;
    public static YourHabitListByParametersServlet servlet;
    public static User user;
    private static HabitService habitService;

    @BeforeAll
    public static void init() {
        userService = mock(UserService.class);
        habitService = mock(HabitService.class);
        writer = new PrintWriter(new StringWriter());
        servlet = new YourHabitListByParametersServlet(userService, habitService);
        user = new User(2, "name1@mail.ru", "password1", "1111", "Name1", Role.CLIENT, false);
    }

    @Test
    @DisplayName("юзер успешно получил лист и json")
    public void today_perform() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        Period frequency = Period.of(0, 0, 10);
        Habit habit = new Habit(
                1L, user, true, true, 1, "run", "run everyday", LocalDate.now(), LocalDate.now().plusDays(frequency.getDays()),
                LocalDate.now().plusDays(1L), LocalDate.now().plus(frequency), null, frequency, 0);
        HabitGeneralDTO habitGeneralDTO = HabitDTOMapper.toHabitGeneralDTO(habit);

        authentication(user, request);

        when(request.getParameter("usefulness")).thenReturn("TRUE");
        when(request.getParameter("active")).thenReturn("");
        when(request.getParameter("name")).thenReturn("");
        when(request.getParameter("description")).thenReturn("ever");
        when(request.getParameter("dateOfCreate")).thenReturn("");
        when(request.getParameter("frequency")).thenReturn("");

        when(habitService.findByParameters(user, "TRUE",
                "", "", "ever", "", "")).thenReturn(List.of(habitGeneralDTO));

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