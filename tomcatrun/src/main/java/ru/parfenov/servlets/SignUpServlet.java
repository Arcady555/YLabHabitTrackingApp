package ru.parfenov.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import ru.parfenov.dto.user.UserSignUpDTO;
import ru.parfenov.model.User;
import ru.parfenov.service.UserService;
import ru.parfenov.utility.ServiceLoading;

import java.io.IOException;
import java.util.Optional;

/**
 * Обработка запроса регистрации
 */
@Slf4j
@WebServlet(name = "SignUpServlet", urlPatterns = "/sign-up")
public class SignUpServlet extends HttpServlet implements MethodsForServlets {
    private final UserService userService;

    public SignUpServlet() throws Exception {
        this.userService = ServiceLoading.loadUserService();
    }

    public SignUpServlet(UserService userService) {
        this.userService = userService;
    }

    /**
     * Метод обработает HTTP запрос Post.
     * Пользователь вводит свои данные(емайл, придуманный пароль, имя(по желанию)) и регистрируется в БД
     *
     * @param request  запрос клиента
     * @param response ответ сервера
     * @throws IOException исключение при вводе-выводе
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String userJson = getStringJson(request);
        ObjectMapper objectMapper = new ObjectMapper();
        UserSignUpDTO userDTO = objectMapper.readValue(userJson, UserSignUpDTO.class);
        Optional<User> userOptional =
                userService.createByReg(userDTO.getEmail(), userDTO.getPassword(), userDTO.getName());
        String userJsonString = userOptional.isPresent() ?
                objectMapper.writeValueAsString(userOptional.get().toString()) :
                "user is not created!";
        response.setStatus("user is not created!".equals(userJsonString) ? 404 : 200);
        finish(response, userJsonString);
    }
}