package ru.parfenov.servlets.admin;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.postgresql.shaded.com.ongres.scram.common.bouncycastle.base64.Base64;
import ru.parfenov.enums.user.Role;
import ru.parfenov.model.User;
import ru.parfenov.service.HabitService;
import ru.parfenov.service.UserService;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class DeleteUserServletTest {
    public static UserService userService;
    public static HabitService habitService;
    public static PrintWriter writer;
    public static DeleteUserServlet servlet;
    public static User user;

    @BeforeAll
    public static void init() {
        userService = mock(UserService.class);
        habitService = mock(HabitService.class);
        writer = new PrintWriter(new StringWriter());
        servlet = new DeleteUserServlet(userService, habitService);
        user = new User(1, "admin@mail.ru", "password", "111", "Name", Role.ADMIN, false);
    }

    @Test
    @DisplayName("ADMIN успешно удалил юзера")
    public void admin_delete_user() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        authentication(user, request);

        when(request.getParameter("id")).thenReturn("2");
        when(habitService.deleteWithUser("2")).thenReturn(true);
        when(userService.delete("2")).thenReturn(true);
        when(response.getWriter()).thenReturn(writer);

        servlet.doDelete(request, response);

        verify(response).setContentType("application/json");
        verify(response).setCharacterEncoding("UTF-8");
        verify(response).setStatus(200);
    }

    @Test
    @DisplayName("Если не получилось удалить все привычки юзера")
    public void test_status_code_404_when_habits_not_deleted() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        authentication(user, request);

        when(request.getParameter("id")).thenReturn("2");
        when(habitService.deleteWithUser("2")).thenReturn(false);
        when(response.getWriter()).thenReturn(writer);

        servlet.doDelete(request, response);

        verify(response).setContentType("application/json");
        verify(response).setCharacterEncoding("UTF-8");

        verify(response).setStatus(404);
    }

    @Test
    @DisplayName("Если привычки удалили, а юзера не получилось")
    public void test_status_code_404_when_user_not_deleted() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);

        authentication(user, request);

        when(request.getParameter("id")).thenReturn("2");
        when(habitService.deleteWithUser("2")).thenReturn(true);
        when(userService.delete("2")).thenReturn(false);
        when(response.getWriter()).thenReturn(writer);

        servlet.doDelete(request, response);

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