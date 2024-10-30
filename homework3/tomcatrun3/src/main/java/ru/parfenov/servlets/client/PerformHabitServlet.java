package ru.parfenov.servlets.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import ru.parfenov.model.Habit;
import ru.parfenov.model.User;
import ru.parfenov.service.HabitService;
import ru.parfenov.service.UserService;
import ru.parfenov.servlets.MethodsForServlets;
import ru.parfenov.utility.ServiceLoading;
import ru.parfenov.utility.Utility;

import java.io.IOException;
import java.util.Optional;

/**
 * Обработка запроса на выполнение привычки
 */
@Slf4j
@WebServlet(name = "PerformHabitServlet", urlPatterns = "/habits-perform")
public class PerformHabitServlet extends HttpServlet implements MethodsForServlets {
    private final UserService userService;
    private final HabitService habitService;

    public PerformHabitServlet() throws Exception {
        userService = ServiceLoading.loadUserService();
        habitService = ServiceLoading.loadHabitService();
    }

    public PerformHabitServlet(UserService userService, HabitService habitService) {
        this.userService = userService;
        this.habitService = habitService;
    }

    /**
     * Метод обработает HTTP запрос Get
     * Есть проверки юзера, что зарегистрирован
     *
     * @param request  запрос клиента
     * @param response ответ сервера
     * @throws IOException исключение при вводе-выводе
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Optional<User> userOptional = Utility.checkUserByEmailNPass(request, userService);
        int responseStatus;
        String habitJsonString;
        if (userOptional.isEmpty()) {
            responseStatus = 401;
            habitJsonString = "no registration!";
        } else {
            ObjectMapper objectMapper = new ObjectMapper();
            String habitId = request.getParameter("habitId");
            Optional<Habit> habit = habitService.perform(userOptional.get(), habitId);
            habitJsonString = habit.isPresent() ? objectMapper.writeValueAsString(habit.get().toString()) : "habit is not performed!";
            responseStatus = habit.isPresent() ? 200 : 404;
        }
        response.setStatus(responseStatus);
        finish(response, habitJsonString);
    }
}