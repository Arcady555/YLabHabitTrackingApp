package ru.parfenov.servlets.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import ru.parfenov.dto.user.UserUpdatePassDTO;
import ru.parfenov.enums.user.Role;
import ru.parfenov.model.User;
import ru.parfenov.service.UserService;
import ru.parfenov.servlets.MethodsForServlets;
import ru.parfenov.utility.ServiceLoading;
import ru.parfenov.utility.Utility;

import java.io.IOException;
import java.util.Optional;

/**
 * Обработка запроса на сброс пароля
 */
@Slf4j
@WebServlet(name = "ResetPasswordServlet", urlPatterns = "/user-reset-password")
public class ResetPasswordServlet extends HttpServlet implements MethodsForServlets {
    private final UserService userService;

    public ResetPasswordServlet() throws Exception {
        userService = ServiceLoading.loadUserService();
    }

    public ResetPasswordServlet(UserService userService) {
        this.userService = userService;
    }

    /**
     * Метод обработает HTTP запрос Post.
     * Есть проверки юзера:
     * что зарегистрирован,
     * что обладает правами админа
     *
     * @param request  запрос клиента
     * @param response ответ сервера
     * @throws IOException исключение при вводе-выводе
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Optional<User> userOptional = Utility.checkUserByEmailNPass(request, userService);
        int responseStatus = userOptional.isEmpty() ? 401 : 403;
        String jsonString = "no rights or registration!";
        if (userOptional.isPresent() && Role.ADMIN.equals(userOptional.get().getRole())) {
            String userJson = getStringJson(request);
            ObjectMapper objectMapper = new ObjectMapper();
            UserUpdatePassDTO userDTO = objectMapper.readValue(userJson, UserUpdatePassDTO.class);
            Optional<User> updateUser = userService.updatePass(userOptional.get().getId(), userDTO.getPassword(), userDTO.getResetPassword());
            jsonString = updateUser.isPresent() ? updateUser.get().toString() : "password is not updated!";
            responseStatus = updateUser.isPresent() ? 200 : 404;
        }
        response.setStatus(responseStatus);
        finish(response, jsonString);
    }
}