package ru.parfenov.servlets.admin;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.postgresql.shaded.com.ongres.scram.common.bouncycastle.base64.Base64;
import ru.parfenov.dto.habit.HabitGeneralDTO;
import ru.parfenov.enums.user.Role;
import ru.parfenov.model.User;
import ru.parfenov.service.HabitService;
import ru.parfenov.service.UserService;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class HabitsOfUserServletTest {
    public static UserService userService;
    public static HabitService habitService;
    public static PrintWriter writer;
    public static HabitsOfUserServlet servlet;
    public static User user;

    @BeforeAll
    public static void init() {
        userService = mock(UserService.class);
        habitService = mock(HabitService.class);
        writer = new PrintWriter(new StringWriter());
        servlet = new HabitsOfUserServlet(userService, habitService);
        user = new User(1, "admin@mail.ru", "password", "111", "Name", Role.ADMIN, false);
    }

    @Test
    @DisplayName("ADMIN успешно вывел список привычек юзера")
    public void admin_view_habits_of_user() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        User userClient = new User(2, "name1@mail.ru", "password1", "1111", "Name1", Role.CLIENT, false);

        authentication(user, request);

        when(request.getParameter("email")).thenReturn("name1@mail.ru");
        when(userService.findByEmail("name1@mail.ru")).thenReturn(Optional.of(userClient));
        when(habitService.findByUser(userClient)).thenReturn(List.of(new
                HabitGeneralDTO()));
        when(response.getWriter()).thenReturn(writer);


        servlet.doGet(request, response);

        verify(response).setContentType("application/json");
        verify(response).setCharacterEncoding("UTF-8");
        verify(response).setStatus(200);
    }

    @Test
    @DisplayName("юзера с таким емайл не существует")
    public void no_user() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        authentication(user, request);

        when(request.getParameter("email")).thenReturn("name1@mail.ru");
        when(userService.findByEmail("name1@mail.ru")).thenReturn(Optional.empty());
        when(response.getWriter()).thenReturn(writer);


        servlet.doGet(request, response);

        verify(response).setContentType("application/json");
        verify(response).setCharacterEncoding("UTF-8");
        verify(response).setStatus(404);
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