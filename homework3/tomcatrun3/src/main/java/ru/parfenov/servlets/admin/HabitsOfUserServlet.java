package ru.parfenov.servlets.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import ru.parfenov.dto.habit.HabitGeneralDTO;
import ru.parfenov.enums.user.Role;
import ru.parfenov.model.Habit;
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
 * Страница вывода списков всех юзеров
 */
@Slf4j
@WebServlet(name = "HabitsOfUserServlet", urlPatterns = "/habits-of-user")
public class HabitsOfUserServlet extends HttpServlet implements MethodsForServlets {
    private final UserService userService;
    private final HabitService habitService;

    public HabitsOfUserServlet() throws Exception {
        userService = ServiceLoading.loadUserService();
        habitService = ServiceLoading.loadHabitService();
    }

    public HabitsOfUserServlet(UserService userService, HabitService habitService) {
        this.userService = userService;
        this.habitService = habitService;
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
        String habitListJsonString = "no rights or registration!";
        if (userOptional.isPresent() && Role.ADMIN.equals(userOptional.get().getRole())) {
            ObjectMapper objectMapper = new ObjectMapper();
            String email = request.getParameter("email");
            Optional<User> optionalClient = userService.findByEmail(email);
            if (optionalClient.isPresent()) {
                List<HabitGeneralDTO> habitList = habitService.findByUser(optionalClient.get());
                habitListJsonString = !habitList.isEmpty() ? objectMapper.writeValueAsString(habitList) : "no habits!";
                responseStatus = 200;
            } else {
                habitListJsonString = "no user with the email!";
                responseStatus = 404;
            }
        }
        response.setStatus(responseStatus);
        finish(response, habitListJsonString);
    }
}