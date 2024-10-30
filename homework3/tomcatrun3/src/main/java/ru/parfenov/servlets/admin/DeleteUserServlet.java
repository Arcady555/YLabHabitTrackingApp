package ru.parfenov.servlets.admin;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import ru.parfenov.enums.user.Role;
import ru.parfenov.model.User;
import ru.parfenov.service.HabitService;
import ru.parfenov.service.UserService;
import ru.parfenov.servlets.MethodsForServlets;
import ru.parfenov.utility.ServiceLoading;
import ru.parfenov.utility.Utility;

import java.io.IOException;
import java.util.Optional;

/**
 * Страница вывода юзера по введённому id
 */
@Slf4j
@WebServlet(name = "DeleteUserServlet", urlPatterns = "/delete-user")
public class DeleteUserServlet extends HttpServlet implements MethodsForServlets {
    private final UserService userService;
    private final HabitService habitService;

    public DeleteUserServlet() throws Exception {
        userService = ServiceLoading.loadUserService();
        habitService = ServiceLoading.loadHabitService();
    }

    public DeleteUserServlet(UserService userService, HabitService habitService) {
        this.userService = userService;
        this.habitService = habitService;
    }

    /**
     * Метод обработает HTTP запрос Get.
     * Есть проверки по юзеру:,
     * что зарегистрирован,
     * что обладает правами админа
     *
     * @param request  запрос клиента
     * @param response ответ сервера
     * @throws IOException исключение при вводе-выводе
     */
    @Override
    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        Optional<User> userOptional = Utility.checkUserByEmailNPass(request, userService);
        int responseStatus = userOptional.isEmpty() ? 401 : 403;
        String clientJsonString = "no rights or registration!";
        if (userOptional.isPresent() && Role.ADMIN.equals(userOptional.get().getRole())) {
            String clientIdStr = request.getParameter("id");
            if (habitService.deleteWithUser(clientIdStr)) {
                boolean result = userService.delete(clientIdStr);
                clientJsonString = result ? "user is deleted!" : "user not deleted!";
                responseStatus = result ? 200 : 404;
            } else {
                responseStatus = 404;
            }
        }
        response.setStatus(responseStatus);
        finish(response, clientJsonString);
    }
}