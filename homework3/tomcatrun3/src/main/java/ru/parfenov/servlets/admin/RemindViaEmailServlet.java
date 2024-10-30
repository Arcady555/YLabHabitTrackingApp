package ru.parfenov.servlets.admin;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import ru.parfenov.emailsend.cases.RemindViaEmail;
import ru.parfenov.enums.user.Role;
import ru.parfenov.model.User;
import ru.parfenov.service.HabitService;
import ru.parfenov.service.UserService;
import ru.parfenov.servlets.MethodsForServlets;
import ru.parfenov.utility.ServiceLoading;
import ru.parfenov.utility.Utility;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Optional;

@Slf4j
@WebServlet(name = "RemindViaEmailServlet", urlPatterns = "/remind-via-email")
public class RemindViaEmailServlet extends HttpServlet implements MethodsForServlets {
    private final UserService userService;
    private final HabitService habitService;

    public RemindViaEmailServlet() throws Exception {
        userService = ServiceLoading.loadUserService();
        habitService = ServiceLoading.loadHabitService();
    }

    public RemindViaEmailServlet(UserService userService, HabitService habitService) {
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
        String jsonString = "no rights or registration!";
        if (userOptional.isPresent() && Role.ADMIN.equals(userOptional.get().getRole())) {
            RemindViaEmail remindViaEmail = new RemindViaEmail(userService, habitService);
            try {
                remindViaEmail.run();
                responseStatus = 200;
                jsonString = "was send";
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        }
        response.setStatus(responseStatus);
        finish(response, jsonString);
    }
}