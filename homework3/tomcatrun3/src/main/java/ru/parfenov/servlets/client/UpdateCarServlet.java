package ru.parfenov.servlets.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import ru.parfenov.homework_3.dto.CarDTO;
import ru.parfenov.homework_3.enums.UserRole;
import ru.parfenov.homework_3.model.User;
import ru.parfenov.homework_3.service.CarService;
import ru.parfenov.homework_3.servlets.MethodsForServlets;
import ru.parfenov.homework_3.utility.Utility;

import java.io.IOException;

/**
 * Страница обновления данных по машине
 */
@Slf4j
@WebServlet(name = "UpdateCarServlet", urlPatterns = "/update-car")
public class UpdateCarServlet extends HttpServlet implements MethodsForServlets {
    private final CarService carService;

    public UpdateCarServlet() throws Exception {
        carService = Utility.loadCarService();
    }

    public UpdateCarServlet(CarService carService) {
        this.carService = carService;
    }

    /**
     * Метод обработает HTTP запрос Post.
     * Есть проверки:
     * что юзер открыл сессию,
     * что зарегистрирован.
     * Если юзер не админ и не менеджер, то он может обновить только свою машину
     *
     * @param request  запрос клиента
     * @param response ответ сервера
     * @throws IOException исключение при вводе-выводе
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException {
        HttpSession session = request.getSession();
        var user = (User) session.getAttribute("user");
        int responseStatus = user == null ? 401 : 403;
        String jsonString = "no rights or registration!";
        if (user != null && (user.getRole() != null)) {
            String orderJson = getStringJson(request);
            ObjectMapper objectMapper = new ObjectMapper();
            CarDTO car = objectMapper.readValue(orderJson, CarDTO.class);
            if (carService.isOwnCar(user.getId(), car.getId()) ||
                    user.getRole() == UserRole.ADMIN || user.getRole() == UserRole.MANAGER) {
                boolean updateCar = carService.update(
                        car.getId(),
                        car.getOwnerId(),
                        car.getBrand(),
                        car.getModel(),
                        car.getYearOfProd(),
                        car.getPrice(),
                        car.getCondition()
                );
                jsonString = updateCar ? "car is updated" : "car is not updated!";
                responseStatus = "car is not updated!".equals(jsonString) ? 404 : 200;
            }
        }
        response.setStatus(responseStatus);
        finish(response, jsonString);
    }
}