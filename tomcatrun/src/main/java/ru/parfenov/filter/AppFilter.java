package ru.parfenov.filter;

import org.postgresql.shaded.com.ongres.scram.common.bouncycastle.base64.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import ru.parfenov.enums.user.Role;
import ru.parfenov.service.UserService;
import ru.parfenov.utility.Utility;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * Класс служит для ограничения посещения страниц юзерам неавторизованным и с разными правами
 */
@Component
@Order(1)
public class AppFilter extends HttpFilter {
    private final UserService userService;

    @Autowired
    public AppFilter(UserService userService) {
        this.userService = userService;
    }

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String uri = request.getRequestURI();
        String email = Utility.getUserEmail(request);
        if (isAlwaysPermitted(uri)) {
            chain.doFilter(request, response);
            return;
        }
        if (email.isEmpty()) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }

        Role role = userService.findByEmail(email).get().getRole();

        if (isPermittedForAdmin(uri) && role.equals(Role.ADMIN)) {
            chain.doFilter(request, response);
        }
    }

    private boolean isAlwaysPermitted(String uri) {
        return "/sign-up".equals(uri);
    }

    private boolean isPermittedForAdmin(String uri) {
        return uri.startsWith("/users/user") ||
                uri.startsWith("/users/find-by_parameters") ||
                uri.startsWith("/users/update") ||
                uri.startsWith("/users/delete") ||
                uri.startsWith("/users/create") ||
                uri.startsWith("/users/all") ||
                uri.startsWith("/audit/find-by-parameters");
    }
}