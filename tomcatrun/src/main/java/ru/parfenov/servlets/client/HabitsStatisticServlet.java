package ru.parfenov.servlets.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import ru.parfenov.dto.habit.HabitStatisticDTO;
import ru.parfenov.model.User;
import ru.parfenov.service.HabitService;
import ru.parfenov.service.UserService;
import ru.parfenov.servlets.MethodsForServlets;
import ru.parfenov.utility.ServiceLoading;
import ru.parfenov.utility.Utility;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

/**
 * Обработка запроса на вывод привычек юзера и статистики по ним за кокой-либо период
 */
@Slf4j
@WebServlet(name = "HabitsStatisticServlet", urlPatterns = "/habits-statistic")
public class HabitsStatisticServlet extends HttpServlet implements MethodsForServlets {
    private final UserService userService;
    private final HabitService habitService;

    public HabitsStatisticServlet() throws Exception {
        userService = ServiceLoading.loadUserService();
        habitService = ServiceLoading.loadHabitService();
    }

    public HabitsStatisticServlet(UserService userService, HabitService habitService) {
        this.userService = userService;
        this.habitService = habitService;
    }

    /**
     * Метод обработает HTTP запрос Get
     * Есть проверка юзера, что зарегистрирован
     *
     * @param request  запрос клиента
     * @param response ответ сервера
     * @throws IOException исключение при вводе-выводе
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Optional<User> userOptional = Utility.checkUserByEmailNPass(request, userService);
        int responseStatus;
        String habitListJsonString;
        if (userOptional.isEmpty()) {
            responseStatus = 401;
            habitListJsonString = "no registration!";
        } else {
            ObjectMapper objectMapper = new ObjectMapper();
            String dateFrom = request.getParameter("dateFrom");
            String dateTo = request.getParameter("dateTo");
            List<HabitStatisticDTO> habitList = habitService.statisticForUser(userOptional.get(), dateFrom, dateTo);
            habitListJsonString = !habitList.isEmpty() ? objectMapper.writeValueAsString(habitList) : "no habits, no statistic!";
            responseStatus = habitList.isEmpty() ? 404 : 200;
        }
        response.setStatus(responseStatus);
        finish(response, habitListJsonString);
    }
}