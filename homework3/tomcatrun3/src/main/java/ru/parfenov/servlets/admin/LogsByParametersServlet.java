package ru.parfenov.servlets.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import ru.parfenov.homework_3.enums.UserRole;
import ru.parfenov.homework_3.model.LineInLog;
import ru.parfenov.homework_3.model.User;
import ru.parfenov.homework_3.service.LogService;
import ru.parfenov.homework_3.servlets.MethodsForServlets;
import ru.parfenov.homework_3.utility.Utility;

import java.io.IOException;
import java.util.List;

/**
 * Страница, где админ может найти в логе инф по клиенту, по дате и по виду операции
 */
@Slf4j
@WebServlet(name = "LogsByParametersServlet", urlPatterns = "/logs-by-parameters")
public class LogsByParametersServlet extends HttpServlet implements MethodsForServlets {
    private final LogService service;

    public LogsByParametersServlet() throws Exception {
        service = Utility.loadLogService();
    }

    public LogsByParametersServlet(LogService service) {
        this.service = service;
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
        String logJsonString = "no rights or registration!";
        if (user != null && user.getRole() == UserRole.ADMIN) {
            ObjectMapper objectMapper = new ObjectMapper();
            String userId = request.getParameter("userId");
            String action = request.getParameter("action");
            String dateTimeFom = request.getParameter("dateTimeFom");
            String dateTimeTo = request.getParameter("dateTimeTo");
            List<LineInLog> userList = service.findByParameters(userId, action, dateTimeFom, dateTimeTo);
            logJsonString = !userList.isEmpty() ? objectMapper.writeValueAsString(userList) : "no users with these parameters!";
            responseStatus = "no log with these parameters!".equals(logJsonString) ? 404 : 200;
        }
        response.setStatus(responseStatus);
        finish(response, logJsonString);
    }
}