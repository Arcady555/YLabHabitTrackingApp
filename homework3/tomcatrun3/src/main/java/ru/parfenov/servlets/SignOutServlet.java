package ru.parfenov.servlets;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * Страница выхода из системы
 */
@Slf4j
@WebServlet(name = "SignOutServlet", urlPatterns = "/sign-out")
public class SignOutServlet extends HttpServlet {

    public SignOutServlet() throws Exception {
    }

    /**
     * Метод обработает HTTP запрос Get.
     * Юзер выходит, сессия закрывается.
     *
     * @param request  запрос клиента
     * @param response ответ сервера
     * @throws IOException исключение при вводе-выводе
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        var session = request.getSession();
        session.invalidate();
        response.setStatus(200);
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.print("user out!");
        out.flush();
    }
}