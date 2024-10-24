package ru.parfenov.servlets.client;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import ru.parfenov.homework_3.enums.UserRole;
import ru.parfenov.homework_3.model.User;
import ru.parfenov.homework_3.service.CarService;
import ru.parfenov.homework_3.servlets.MethodsForServlets;
import ru.parfenov.homework_3.utility.Utility;

import java.io.IOException;

/**
 * Страница удаления карточки машины
 */
@Slf4j
@WebServlet(name = "DeleteCarServlet", urlPatterns = "/delete-car")
public class DeleteCarServlet extends HttpServlet implements MethodsForServlets {
    private final CarService carService;

    public DeleteCarServlet() throws Exception {
        carService = Utility.loadCarService();
    }

    public DeleteCarServlet(CarService carService) {
        this.carService = carService;
    }

    /**
     * Метод обработает HTTP запрос Delete.
     * Есть проверки:
     * что юзер открыл сессию,
     * что зарегистрирован.
     * Если юзер не админ и не менеджер, то он может удалить только свою машину
     *
     * @param request  запрос клиента
     * @param response ответ сервера
     * @throws IOException исключение при вводе-выводе
     */
    @Override
    public void doDelete(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        var user = (User) session.getAttribute("user");
        int responseStatus = user == null ? 401 : 403;
        String jsonString = "no rights or registration!";
        if (user != null && (user.getRole() != null)) {
            String carIdStr = request.getParameter("id");
            if (carService.isOwnCar(user.getId(), carIdStr) ||
                    user.getRole() == UserRole.ADMIN || user.getRole() == UserRole.MANAGER) {
                jsonString = carService.delete(carIdStr) ? "the car is deleted" : "the car is not deleted!";
                responseStatus = "the car is not deleted!".equals(jsonString) ? 404 : 200;
            }
        }
        response.setStatus(responseStatus);
        finish(response, jsonString);
    }
}