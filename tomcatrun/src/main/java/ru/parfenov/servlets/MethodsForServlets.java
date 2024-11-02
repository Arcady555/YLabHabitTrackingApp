package ru.parfenov.servlets;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public interface MethodsForServlets {
    default String getStringJson(HttpServletRequest request) throws IOException {
        Scanner scanner = new Scanner(request.getInputStream());
        String result = scanner.useDelimiter("\\A").next();
        scanner.close();
        return result;
    }

    default void finish(HttpServletResponse response, String str) throws IOException {
        PrintWriter out = response.getWriter();
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        out.print(str);
        out.flush();
    }
}