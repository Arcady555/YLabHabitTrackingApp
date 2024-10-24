package ru.parfenov.servlets.client;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import ru.parfenov.homework_3.dto.OrderDTO;
import ru.parfenov.homework_3.enums.OrderType;
import ru.parfenov.homework_3.model.Order;
import ru.parfenov.homework_3.model.User;
import ru.parfenov.homework_3.service.CarService;
import ru.parfenov.homework_3.service.OrderService;
import ru.parfenov.homework_3.service.UserService;
import ru.parfenov.homework_3.servlets.MethodsForServlets;
import ru.parfenov.homework_3.utility.Utility;

import java.io.IOException;
import java.util.Optional;

/**
 * Страница создания заказа на покупку или сервис машины.
 */
@Slf4j
@WebServlet(name = "CreateOrderServlet", urlPatterns = "/create-order")
public class CreateOrderServlet extends HttpServlet implements MethodsForServlets {
    private final OrderService orderService;
    private final CarService carService;
    private final UserService userService;

    public CreateOrderServlet() throws Exception {
        carService = Utility.loadCarService();
        orderService = Utility.loadOrderService();
        userService = Utility.loadUserservice();
    }

    public CreateOrderServlet(OrderService orderService, CarService carService, UserService userService) {
        this.orderService = orderService;
        this.carService = carService;
        this.userService = userService;
    }

    /**
     * Метод обработает HTTP запрос Post.
     * Есть проверки:
     * что юзер открыл сессию,
     * что зарегистрирован.
     * Заказ на покупку юзер может создать только если машина не его
     * А заказ на сервис - если машина его
     * После покупки машины его buysAmount++
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
        String orderJsonString = "no rights or registration!";
        if (user != null) {
            String orderJson = getStringJson(request);
            ObjectMapper objectMapper = new ObjectMapper();
            OrderDTO orderDTO = objectMapper.readValue(orderJson, OrderDTO.class);
            if (checkCorrelation(user, orderDTO)) {
                Optional<Order> orderOptional = orderService.create(
                        orderDTO.getAuthorId(),
                        orderDTO.getCarId(),
                        orderDTO.getType());
                orderOptional.ifPresent(order -> buysAmountPlus(order, user));
                orderJsonString = orderOptional.isPresent() ?
                        objectMapper.writeValueAsString(orderOptional.get()) :
                        "order is not created!";
                responseStatus = "order is not created!".equals(orderJsonString) ? 404 : 200;
            }
        }
        response.setStatus(responseStatus);
        finish(response, orderJsonString);
    }

    private boolean checkCorrelation(User user, OrderDTO order) {
        boolean firstCheck = carService.isOwnCar(user.getId(), order.getCarId()) &&
                order.getType().equals("SERVICE");
        boolean secondCheck = !carService.isOwnCar(user.getId(), order.getCarId())
                && order.getType().equals("BUY");
        return firstCheck || secondCheck;
    }

    private void buysAmountPlus(Order order, User user) {
        if (order.getType() == OrderType.BUY) {
            int buysAmount = user.getBuysAmount();
            buysAmount++;
            userService.update(
                    user.getId(), "", "", "", "", buysAmount
            );
        }
    }
}