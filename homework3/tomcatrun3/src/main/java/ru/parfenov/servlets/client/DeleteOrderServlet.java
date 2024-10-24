package ru.parfenov.servlets.client;

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
 * Страница удаления заказа
 */
@Slf4j
@WebServlet(name = "DeleteOrderServlet", urlPatterns = "/delete-order")
public class DeleteOrderServlet extends HttpServlet implements MethodsForServlets {
    private final OrderService orderService;

    public DeleteOrderServlet() throws Exception {
        orderService = Utility.loadOrderService();
    }

    public DeleteOrderServlet(OrderService orderService) {
        this.orderService = orderService;
    }

    /**
     * Метод обработает HTTP запрос Delete.
     * Есть проверки:
     * что юзер открыл сессию,
     * что зарегистрирован.
     * Если юзер не админ и не менеджер, то он может удалить только свой заказ
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
            String orderIdStr = request.getParameter("id");
            if (orderService.isOwnOrder(user.getId(), orderIdStr) ||
                    user.getRole() == UserRole.ADMIN || user.getRole() == UserRole.MANAGER) {
                jsonString = orderService.delete(orderIdStr) ? "order is deleted" : "order is not deleted!";
                responseStatus = "order is not deleted!".equals(jsonString) ? 404 : 200;
            }
        }
        response.setStatus(responseStatus);
        finish(response, jsonString);
    }
}