package ru.parfenov.servlets.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import ru.parfenov.dto.habit.HabitGeneralDTO;
import ru.parfenov.dto.habit.HabitUpdateDTO;
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
 * Обработка запроса на обновление привычки
 */
@Slf4j
@WebServlet(name = "UpdateHabitServlet ", urlPatterns = "/habits-update")
public class UpdateHabitServlet extends HttpServlet implements MethodsForServlets {
    private final UserService userService;
    private final HabitService habitService;

    public UpdateHabitServlet() throws Exception {
        userService = ServiceLoading.loadUserService();
        habitService = ServiceLoading.loadHabitService();
    }

    public UpdateHabitServlet(UserService userService, HabitService habitService) {
        this.userService = userService;
        this.habitService = habitService;
    }

    /**
     * Метод обработает HTTP запрос Post.
     * Есть проверки юзера, что зарегистрирован
     *
     * @param request  запрос клиента
     * @param response ответ сервера
     * @throws IOException исключение при вводе-выводе
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Optional<User> userOptional = Utility.checkUserByEmailNPass(request, userService);
        int responseStatus;
        String jsonString;
        if (userOptional.isEmpty()) {
            responseStatus = 401;
            jsonString = "no registration!";
        } else {
            String habitJson = getStringJson(request);
            ObjectMapper objectMapper = new ObjectMapper();
            HabitUpdateDTO habitDTO = objectMapper.readValue(habitJson, HabitUpdateDTO.class);
            Optional<HabitGeneralDTO> habit = habitService.updateByUser(userOptional.get(), habitDTO);
            jsonString = habit.isPresent() ? habit.get().toString() : "habit is not updated!";
            responseStatus = habit.isPresent() ? 200 : 404;
        }
        response.setStatus(responseStatus);
        finish(response, jsonString);
    }
}