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
import java.util.Optional;

/**
 * Страница вывода юзера по введённому id
 */
@Slf4j
@WebServlet(name = "ViewUserServlet", urlPatterns = "/user")
public class ViewUserServlet extends HttpServlet implements MethodsForServlets {
    private final UserService userService;

    public ViewUserServlet() throws Exception {
        userService = ServiceLoading.loadUserService();
    }

    public ViewUserServlet(UserService userService) {
        this.userService = userService;
    }

    /**
     * Метод обработает HTTP запрос Get.
     * Есть проверки по юзеру:,
     * что зарегистрирован,
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
        String clientJsonString = "no rights or registration!";
        if (userOptional.isPresent() && Role.ADMIN.equals(userOptional.get().getRole())) {
            ObjectMapper objectMapper = new ObjectMapper();
            String clientIdStr = request.getParameter("id");
            Optional<User> userResult = userService.findById(clientIdStr);
            clientJsonString = userResult.isPresent() ? objectMapper.writeValueAsString(userResult.get().toString()) : "user not found!";
            responseStatus = userResult.isPresent() ? 200 : 404;
        }
        response.setStatus(responseStatus);
        finish(response, clientJsonString);
    }
}