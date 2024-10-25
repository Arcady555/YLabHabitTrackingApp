package ru.parfenov.servlets.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import ru.parfenov.dto.habit.HabitGeneralDTO;
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
 * Обработка запроса на вывод всех привычек, созданных юзером
 */
@Slf4j
@WebServlet(name = "YourHabitListServlet", urlPatterns = "/habits-your-list")
public class YourHabitListServlet extends HttpServlet implements MethodsForServlets {
    private final UserService userService;
    private final HabitService habitService;

    public YourHabitListServlet() throws Exception {
        userService = ServiceLoading.loadUserService();
        habitService = ServiceLoading.loadHabitService();
    }

    public YourHabitListServlet(UserService userService, HabitService habitService) {
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
            List<HabitGeneralDTO> habitList =
                    habitService.findByUser(userOptional.get());
            habitListJsonString = !habitList.isEmpty() ? objectMapper.writeValueAsString(habitList) : "no habits!";
            responseStatus = "no habits!".equals(habitListJsonString) ? 404 : 200;
        }
        response.setStatus(responseStatus);
        finish(response, habitListJsonString);
    }
}
