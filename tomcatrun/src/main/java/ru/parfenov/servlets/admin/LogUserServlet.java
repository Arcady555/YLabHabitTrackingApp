package ru.parfenov.servlets.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import ru.parfenov.enums.user.Role;
import ru.parfenov.model.LogRecord;
import ru.parfenov.model.User;
import ru.parfenov.service.LogService;
import ru.parfenov.service.UserService;
import ru.parfenov.servlets.MethodsForServlets;
import ru.parfenov.utility.ServiceLoading;
import ru.parfenov.utility.Utility;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Страница вывода зафиксированных действий юзеров за определенный период
 */
@Slf4j
@WebServlet(name = "LogUserServlet", urlPatterns = "/users_logs")
public class LogUserServlet extends HttpServlet implements MethodsForServlets {

    private final UserService userService;
    private final LogService logService;

    public LogUserServlet(UserService userService) throws Exception {
        this.userService = userService;
        logService = ServiceLoading.loadLogService();
    }

    public LogUserServlet(UserService userService, LogService logService) {
        this.userService = userService;
        this.logService = logService;
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
            String dateTimeFrom = request.getParameter("dateTimeFrom");
            String dateTimeTo = request.getParameter("dateTimeTo");
            ObjectMapper objectMapper = new ObjectMapper();
            List<LogRecord> userList = logService.findByDateTime(dateTimeFrom, dateTimeTo);
            userListJsonString = !userList.isEmpty() ? objectMapper.writeValueAsString(userList) : "no logs!";
            responseStatus = !userList.isEmpty() ? 404 : 200;
        }
        response.setStatus(responseStatus);
        finish(response, userListJsonString);
    }
}