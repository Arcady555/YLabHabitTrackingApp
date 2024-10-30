package ru.parfenov.servlets.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import ru.parfenov.enums.user.Role;
import ru.parfenov.model.User;
import ru.parfenov.service.UserService;
import ru.parfenov.servlets.MethodsForServlets;
import ru.parfenov.utility.ServiceLoading;
import ru.parfenov.utility.Utility;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Страница вывода списков всех юзеров
 */
@Slf4j
@WebServlet(name = "AllUsersServlet", urlPatterns = "/all-users")
public class AllUsersServlet extends HttpServlet implements MethodsForServlets {
    private final UserService userService;

    public AllUsersServlet() throws Exception {
        userService = ServiceLoading.loadUserService();
    }

    public AllUsersServlet(UserService userService) {
        this.userService = userService;
    }

    /**
     * Метод обработает HTTP запрос Get
     * Есть проверки юзера:
     * что зарегистрирован
     * что обладает правами админа
     *
     * @param request  запрос клиента
     * @param response ответ сервера
     * @throws IOException исключение при вводе-выводе
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Optional<User> userOptional = Utility.checkUserByEmailNPass(request, userService);
        int responseStatus = userOptional.isEmpty() ? 401 : 403;
        String userListJsonString = "no rights or registration!";
        if (userOptional.isPresent() && Role.ADMIN.equals(userOptional.get().getRole())) {
            ObjectMapper objectMapper = new ObjectMapper();
            List<User> userList = userService.findAll();
            userListJsonString = !userList.isEmpty() ? objectMapper.writeValueAsString(userList) : "no users!";
            responseStatus = !userList.isEmpty() ? 404 : 200;
        }
        response.setStatus(responseStatus);
        finish(response, userListJsonString);
    }
}