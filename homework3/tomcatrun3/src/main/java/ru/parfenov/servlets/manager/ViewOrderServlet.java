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
import java.util.Optional;

/**
 * Страница просмотра карточки заказа
 */
@Slf4j
@WebServlet(name = "ViewOrderServlet", urlPatterns = "/order")
public class ViewOrderServlet extends HttpServlet implements MethodsForServlets {
    private final OrderService orderService;

    public ViewOrderServlet() throws Exception {
        orderService = Utility.loadOrderService();
    }

    public ViewOrderServlet(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Метод обработает HTTP запрос Get.
     * Есть проверки:
     *     что юзер открыл сессию,
     *     что зарегистрирован,
     *     что он менеджер или админ
     *     если он клиент, то может смотреть только если заказ его
     * @param request запрос клиента
     * @param response ответ сервера
     * @throws IOException исключение при вводе-выводе
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        HttpSession session = request.getSession();
        var user = (User) session.getAttribute("user");
        int responseStatus = user == null ? 401 : 403;
        String orderJsonString = "no rights or registration!";
        if (user != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            String orderIdStr = request.getParameter("id");
            Optional<Order> orderOptional = orderService.findById(orderIdStr);
            if (orderOptional.isPresent() && checkCorrelation(user, orderOptional.get())) {
                orderJsonString = objectMapper.writeValueAsString(orderOptional.get());
                responseStatus = 200;
            } else {
                orderJsonString = "order not found!";
                responseStatus = 404;
            }
        }
        response.setStatus(responseStatus);
        finish(response, orderJsonString);
    }

    private boolean checkCorrelation(User user, Order order) {
        boolean roleCheck = user.getRole() == UserRole.MANAGER || user.getRole() == UserRole.ADMIN;
        boolean authorCheck = orderService.isOwnOrder(user.getId(), order.getId());
        return roleCheck || authorCheck;
    }
}