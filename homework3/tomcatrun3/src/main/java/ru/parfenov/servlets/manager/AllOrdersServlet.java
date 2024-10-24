package ru.parfenov.servlets.manager;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import ru.parfenov.homework_3.enums.UserRole;
import ru.parfenov.homework_3.model.Order;
import ru.parfenov.homework_3.model.User;
import ru.parfenov.homework_3.service.OrderService;
import ru.parfenov.homework_3.servlets.MethodsForServlets;
import ru.parfenov.homework_3.utility.Utility;

import java.io.IOException;
import java.util.List;

/**
 * Страница вывода всех заказов
 */
@Slf4j
@WebServlet(name = "AllOrdersServlet", urlPatterns = "/all-orders")
public class AllOrdersServlet extends HttpServlet implements MethodsForServlets {
    private final OrderService orderService;

    public AllOrdersServlet() throws Exception {
        orderService = Utility.loadOrderService();
    }

    public AllOrdersServlet(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Метод обработает HTTP запрос Get.
     * Есть проверки:
     *     что юзер открыл сессию,
     *     что зарегистрирован,
     *     что он менеджер или админ
     * @param request запрос клиента
     * @param response ответ сервера
     * @throws IOException исключение при вводе-выводе
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        var user = (User) session.getAttribute("user");
        int responseStatus = user == null ? 401 : 403;
        String orderListJsonString = "no rights or registration!";
        if (user != null && (user.getRole() == UserRole.MANAGER || user.getRole() == UserRole.ADMIN)) {
            ObjectMapper objectMapper = new ObjectMapper();
            List<Order> orderList = orderService.findAll();
            orderListJsonString = !orderList.isEmpty() ? objectMapper.writeValueAsString(orderList) : "no orders!";
            responseStatus = "no orders!".equals(orderListJsonString) ? 404 : 200;
        }
        response.setStatus(responseStatus);
        finish(response, orderListJsonString);
    }
}
