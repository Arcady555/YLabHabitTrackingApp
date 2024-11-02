package ru.parfenov.servlets.admin;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.postgresql.shaded.com.ongres.scram.common.bouncycastle.base64.Base64;
import ru.parfenov.dto.user.UserUpdateDTO;
import ru.parfenov.enums.user.Role;
import ru.parfenov.model.User;
import ru.parfenov.service.UserService;
import ru.parfenov.servlets.DelegatingServletInputStream;

import java.io.ByteArrayInputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class UpdateUserServletTest {
    public static UserService userService;
    public static PrintWriter writer;
    public static UpdateUserServlet servlet;
    public static User user;

    @BeforeAll
    public static void init() {
        userService = mock(UserService.class);
        writer = new PrintWriter(new StringWriter());
        servlet = new UpdateUserServlet(userService);
        user = new User(1, "admin@mail.ru", "password", "111", "Name", Role.ADMIN, false);
    }

    @Test
    @DisplayName("ADMIN успешно обновил юзера")
    public void admin_update_user() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        User userClient = new User(2, "name1@mail.ru", "password1", "1111", "Name1", Role.CLIENT, false);
        UserUpdateDTO userDTO = new UserUpdateDTO(1, null, "New Name",
                null, null);

        authentication(user, request);

        when(request.getInputStream()).thenReturn(new DelegatingServletInputStream(new
                ByteArrayInputStream("{\"id\":1,\"name\":\"New Name\"}".getBytes())));
        when(userService.update(userDTO,
                "")).thenReturn(Optional.of(userClient));
        when(response.getWriter()).thenReturn(writer);

        servlet.doPost(request, response);

        verify(response).setContentType("application/json");
        verify(response).setCharacterEncoding("UTF-8");
        verify(response).setStatus(200);
    }

    @Test
    @DisplayName("Не зашёл как ADMIN")
    public void test_returns_401_if_user_not_registered() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        when(response.getWriter()).thenReturn(writer);

        servlet.doPost(request, response);
        verify(response).setStatus(401);
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