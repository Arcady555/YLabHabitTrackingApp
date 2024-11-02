package ru.parfenov.servlets.client;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.postgresql.shaded.com.ongres.scram.common.bouncycastle.base64.Base64;
import ru.parfenov.dto.habit.HabitCreateDTO;
import ru.parfenov.dto.habit.HabitGeneralDTO;
import ru.parfenov.enums.user.Role;
import ru.parfenov.model.User;
import ru.parfenov.service.HabitService;
import ru.parfenov.service.UserService;
import ru.parfenov.servlets.DelegatingServletInputStream;

import java.io.ByteArrayInputStream;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.Mockito.*;

public class CreateHabitServletTest {
    public static UserService userService;
    public static PrintWriter writer;
    public static CreateHabitServlet servlet;
    public static User user;
    private static HabitService habitService;

    @BeforeAll
    public static void init() {
        userService = mock(UserService.class);
        habitService = mock(HabitService.class);
        writer = new PrintWriter(new StringWriter());
        servlet = new CreateHabitServlet(userService, habitService);
        user = new User(2, "name1@mail.ru", "password1", "1111", "Name1", Role.CLIENT, false);
    }

    @Test
    @DisplayName("CLIENT успешно создал привычку")
    public void client_create_habit() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        User userClient = new User(2, "name1@mail.ru", "password1", "1111", "Name1", Role.CLIENT, false);
        HabitCreateDTO habitCreateDTO = new HabitCreateDTO("true", "swim", "swim every week", "2024-10-28", 7);

        HabitGeneralDTO habitGeneralDTO = new HabitGeneralDTO(
                1, "name1@mail.ru", true, true, 1,
                "swim", "swim every week", LocalDate.now().toString(),
                LocalDate.now().plusDays(7L).toString(), null, 7, 0, "");

        authentication(user, request);

        when(request.getInputStream())
                .thenReturn(new DelegatingServletInputStream(new ByteArrayInputStream(
                        ("{\"usefulness\" : \"TRUE\",\"name\" : \"swim\",\"description\" :" +
                                " \"swim every week\",\"firstPerform\" : \"2024-10-28\", \"frequency\" : 7}")
                                .getBytes())));
        when(habitService.create(userClient, habitCreateDTO)).thenReturn(Optional.of(habitGeneralDTO));
        when(response.getWriter()).thenReturn(writer);

        servlet.doPost(request, response);

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