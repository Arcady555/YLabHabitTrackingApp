package ru.parfenov.servlets.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import ru.parfenov.homework_3.model.Car;
import ru.parfenov.homework_3.model.User;
import ru.parfenov.homework_3.service.CarService;
import ru.parfenov.homework_3.servlets.MethodsForServlets;
import ru.parfenov.homework_3.utility.Utility;

import java.io.IOException;
import java.util.List;

/**
 * Страница вывода списка всех машин
 */
@Slf4j
@WebServlet(name = "AllCarsServlet", urlPatterns = "/all-cars")
public class AllCarsServlet extends HttpServlet implements MethodsForServlets {
    private final CarService carService;

    public AllCarsServlet() throws Exception {
        carService = Utility.loadCarService();
    }

    public AllCarsServlet(CarService carService) {
        this.carService = carService;
    }

    /**
     * Метод обработает HTTP запрос Get.
     * Есть проверки:
     * что юзер открыл сессию,
     * что зарегистрирован,
     *
     * @param request  запрос клиента
     * @param response ответ сервера
     * @throws IOException исключение при вводе-выводе
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        var user = (User) session.getAttribute("user");
        int responseStatus = user == null ? 401 : 403;
        String carListJsonString = "no rights or registration!";
        if (user != null && (user.getRole() != null)) {
            ObjectMapper objectMapper = new ObjectMapper();
            List<Car> carList = carService.findAll();
            carListJsonString = !carList.isEmpty() ? objectMapper.writeValueAsString(carList) : "no cars!";
            responseStatus = "no cars!".equals(carListJsonString) ? 404 : 200;
        }
        response.setStatus(responseStatus);
        finish(response, carListJsonString);
    }
}