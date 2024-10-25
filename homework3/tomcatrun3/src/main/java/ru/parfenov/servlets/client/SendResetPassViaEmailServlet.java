package ru.parfenov.servlets.client;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import ru.parfenov.emailsend.cases.ResetPasswordViaEmail;
import ru.parfenov.model.User;
import ru.parfenov.service.UserService;
import ru.parfenov.servlets.MethodsForServlets;
import ru.parfenov.utility.ServiceLoading;
import ru.parfenov.utility.Utility;

import javax.mail.MessagingException;
import java.io.IOException;
import java.util.Optional;

/**
 * Обработка запроса чтобы прислали на емайл сброс пароля
 */
@Slf4j
@WebServlet(name = "PerformHabitServlet", urlPatterns = "/send-reset-pass-via-email")
public class SendResetPassViaEmailServlet extends HttpServlet implements MethodsForServlets {
    private final UserService userService;

    public SendResetPassViaEmailServlet() throws Exception {
        userService = ServiceLoading.loadUserService();
    }

    public SendResetPassViaEmailServlet(UserService userService) {
        this.userService = userService;
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
        String jsonString;
        if (userOptional.isEmpty()) {
            responseStatus = 401;
            jsonString = "no registration!";
        } else {
            User user = userOptional.get();
            ResetPasswordViaEmail resetPasswordViaEmail = new ResetPasswordViaEmail(user);
            try {
                resetPasswordViaEmail.run();
                responseStatus = 200;
                jsonString = "check your mail";
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        }
        response.setStatus(responseStatus);
        finish(response, jsonString);
    }
}