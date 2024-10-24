package ru.parfenov.servlets.manager;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import ru.parfenov.homework_3.enums.UserRole;
import ru.parfenov.homework_3.model.User;
import ru.parfenov.homework_3.service.OrderService;
import ru.parfenov.homework_3.servlets.MethodsForServlets;
import ru.parfenov.homework_3.utility.Utility;

import java.io.IOException;

/**
 * Страница перевода заказа в статус "закрыт"
 */
@Slf4j
@WebServlet(name = "CloseOrderServlet", urlPatterns = "/close-order")
public class CloseOrderServlet extends HttpServlet implements MethodsForServlets {
    private final OrderService orderService;

    public CloseOrderServlet() throws Exception {
        orderService = Utility.loadOrderService();
    }

    public CloseOrderServlet(OrderService orderService) {
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
        String jsonString = "no rights or registration!";
        if (user != null && (user.getRole() == UserRole.MANAGER || user.getRole() == UserRole.ADMIN)) {
            String orderIdStr = request.getParameter("id");
            orderService.close(orderIdStr);
            jsonString = orderService.close(orderIdStr) ? "order is closed" : "order is not closed!";
            responseStatus = "order is not closed!".equals(jsonString) ? 404 : 200;
        }
        response.setStatus(responseStatus);
        finish(response, jsonString);
    }
}