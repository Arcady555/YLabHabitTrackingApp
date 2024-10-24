package ru.parfenov.servlets.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import ru.parfenov.homework_3.enums.UserRole;
import ru.parfenov.homework_3.model.User;
import ru.parfenov.homework_3.service.UserService;
import ru.parfenov.homework_3.servlets.MethodsForServlets;
import ru.parfenov.homework_3.utility.Utility;

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
        userService = Utility.loadUserservice();
    }

    public ViewUserServlet(UserService userService) {
        this.userService = userService;
    }

    /**
     * Метод обработает HTTP запрос Get.
     * Есть проверки:
     * что юзер открыл сессию,
     * что зарегистрирован,
     * что обладает правами админа
     *
     * @param request  запрос клиента
     * @param response ответ сервера
     * @throws IOException исключение при вводе-выводе
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        var user = (User) session.getAttribute("user");
        int responseStatus = user == null ? 401 : 403;
        String userJsonString = "no rights or registration!";
        if (user != null && user.getRole() == UserRole.ADMIN) {
            ObjectMapper objectMapper = new ObjectMapper();
            String userIdStr = request.getParameter("id");
            Optional<User> userOptional = userService.findById(userIdStr);
            userJsonString = userOptional.isPresent() ? objectMapper.writeValueAsString(userOptional.get()) : "user not found!";
            responseStatus = "user not found!".equals(userJsonString) ? 404 : 200;
        }
        response.setStatus(responseStatus);
        finish(response, userJsonString);
    }
}