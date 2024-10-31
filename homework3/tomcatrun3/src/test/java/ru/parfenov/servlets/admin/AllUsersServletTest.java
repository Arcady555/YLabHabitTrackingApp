package ru.parfenov.servlets.admin;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.postgresql.shaded.com.ongres.scram.common.bouncycastle.base64.Base64;
import ru.parfenov.enums.user.Role;
import ru.parfenov.model.User;
import ru.parfenov.service.UserService;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class AllUsersServletTest {

    public static UserService userService;
    public static PrintWriter writer;
    public static AllUsersServlet servlet;
    public static User user;

    @BeforeAll
    public static void init() {
        userService = mock(UserService.class);
        writer = new PrintWriter(new StringWriter());
        servlet = new AllUsersServlet(userService);
        user = new User(1, "admin@mail.ru", "password", "111", "Name", Role.ADMIN, false);
    }
/*
    @Test
    @DisplayName("ADMIN успешно получил лист и json")
    public void admin_user_requests_all_users() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        User userClient = new User(2, "name1@mail.ru", "password1", "1111", "Name1", Role.CLIENT, false);
        List<User> users = List.of(userClient);

        authentication(user, request);

        when(userService.findAll()).thenReturn(users);
        when(response.getWriter()).thenReturn(writer);

        servlet.doGet(request, response);

        verify(response).setContentType("application/json");
        verify(response).setCharacterEncoding("UTF-8");
        verify(response).setStatus(200);
    } */


    @Test
    @DisplayName("Если не прошел аутентификацию")
    public void no_user_receives_401_status() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(response.getWriter()).thenReturn(writer);

        servlet.doGet(request, response);

        verify(response).setStatus(401);
    }

    @Test
    @DisplayName("Если не админ")
    public void non_admin_user_receives_403_status() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        User userClient = new User(2, "name1@mail.ru", "password1", "1111", "Name1", Role.CLIENT, false);

        authentication(userClient, request);

        when(response.getWriter()).thenReturn(writer);

        servlet.doGet(request, response);

        verify(response).setStatus(403);
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