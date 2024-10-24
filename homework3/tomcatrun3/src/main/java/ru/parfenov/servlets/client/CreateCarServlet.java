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
import ru.parfenov.homework_3.model.Car;
import ru.parfenov.homework_3.model.User;
import ru.parfenov.homework_3.service.CarService;
import ru.parfenov.homework_3.servlets.MethodsForServlets;
import ru.parfenov.homework_3.utility.Utility;

import java.io.IOException;
import java.util.Optional;

/**
 * Страница, где пользователь может ввести машину в базу данных
 */
@Slf4j
@WebServlet(name = "CreateCarServlet", urlPatterns = "/create-car")
public class CreateCarServlet extends HttpServlet implements MethodsForServlets {
    private final CarService carService;

    public CreateCarServlet() throws Exception {
        carService = Utility.loadCarService();
    }

    public CreateCarServlet(CarService carService) {
        this.carService = carService;
    }

    /**
     * Метод обработает HTTP запрос Post.
     * Есть проверки:
     * что юзер открыл сессию,
     * что зарегистрирован,
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
        String carJsonString = "no rights or registration!";
        if (user != null && (user.getRole() != null)) {
            String userJson = getStringJson(request);
            ObjectMapper objectMapper = new ObjectMapper();
            CarDTO carDTO = objectMapper.readValue(userJson, CarDTO.class);
            Optional<Car> carOptional = carService.create(
                    user.getId(),
                    carDTO.getBrand(),
                    carDTO.getModel(),
                    carDTO.getYearOfProd(),
                    carDTO.getPrice(),
                    carDTO.getCondition()
            );
            carJsonString = carOptional.isPresent() ?
                    objectMapper.writeValueAsString(carOptional.get()) :
                    "car is not created!";
            responseStatus = "car is not created!".equals(carJsonString) ? 404 : 200;
        }
        response.setStatus(responseStatus);
        finish(response, carJsonString);
    }
}