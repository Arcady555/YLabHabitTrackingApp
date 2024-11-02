package ru.parfenov.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import ru.parfenov.dto.user.UserSignUpDTO;
import ru.parfenov.enums.user.Role;
import ru.parfenov.model.User;
import ru.parfenov.service.UserService;

import java.io.ByteArrayInputStream;
import java.io.PrintWriter;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class SignUpServletTest {
    UserService userService = mock(UserService.class);
    SignUpServlet signUpServlet = new SignUpServlet(userService);
    HttpServletRequest request = mock(HttpServletRequest.class);
    HttpServletResponse response = mock(HttpServletResponse.class);
    PrintWriter writer = mock(PrintWriter.class);

    public SignUpServletTest() throws Exception {
    }

    @Test
    @DisplayName("Статус 200 если успех")
    public void test_status_code_success() throws Exception {
        String jsonInput = "{\"email\":\"john@example.com\",\"password\":\"password123\",\"name\":\"John\"}";
        when(request.getInputStream())
                .thenReturn(new DelegatingServletInputStream(new ByteArrayInputStream(jsonInput.getBytes())));
        when(response.getWriter()).thenReturn(writer);
        User user = new User(0, "john@example.com", "password123", "111", "John", Role.CLIENT, false);
        when(userService.createByReg("john@example.com", "password123", "John")).thenReturn(Optional.of(user));

        signUpServlet.doPost(request, response);

        verify(response).setStatus(200);
    }

    @Test
    @DisplayName("Вывод кода 404")
    public void test_status_code_failure() throws Exception {
        String jsonInput = "{\"email\":\"john@example.com\",\"password\":\"password123\",\"name\":\"John\"}";
        when(request.getInputStream())
                .thenReturn(new DelegatingServletInputStream(new ByteArrayInputStream(jsonInput.getBytes())));
        when(response.getWriter()).thenReturn(writer);
        when(userService.createByReg("John", "password123", "john@example.com")).thenReturn(Optional.empty());

        signUpServlet.doPost(request, response);

        verify(response).setStatus(404);
    }

    @Test
    @DisplayName("404 если пропущены поля")
    public void test_missing_required_fields() throws Exception {
        String jsonInput = "{\"email\":\"john@example.com\"}";
        when(request.getInputStream())
                .thenReturn(new DelegatingServletInputStream(new ByteArrayInputStream(jsonInput.getBytes())));
        when(response.getWriter()).thenReturn(writer);

        signUpServlet.doPost(request, response);

        verify(response).setStatus(404);
    }

    @Test
    @DisplayName("Корректный вызов сервиса")
    public void test_conversion_and_persistence_of_user() throws Exception {
        String jsonInput = "{\"email\":\"john@example.com\",\"password\":\"password123\",\"name\":\"John\"}";
        when(request.getInputStream())
                .thenReturn(new DelegatingServletInputStream(new ByteArrayInputStream(jsonInput.getBytes())));
        when(response.getWriter()).thenReturn(writer);
        User user = new User(0, "john@example.com", "password123", "111", "John", Role.CLIENT, false);
        when(userService.createByReg("john@example.com", "password123", "John")).thenReturn(Optional.of(user));
        signUpServlet.doPost(request, response);
        verify(userService).createByReg("john@example.com", "password123", "John");
    }

    @Test
    @DisplayName("Создание DTO")
    public void test_json_parsing_to_user_signup_dto() throws Exception {
        ObjectMapper objectMapper = new ObjectMapper();
        String jsonInput = "{\"email\":\"test@example.com\",\"password\":\"password\",\"name\":\"Test User\"}";
        UserSignUpDTO userDTO = objectMapper.readValue(jsonInput,
                UserSignUpDTO.class);
        assertEquals("test@example.com", userDTO.getEmail());
        assertEquals("password", userDTO.getPassword());
        assertEquals("Test User", userDTO.getName());
    }
}