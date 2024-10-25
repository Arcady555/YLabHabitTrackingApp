package ru.parfenov.filter;
/*
import org.postgresql.shaded.com.ongres.scram.common.bouncycastle.base64.Base64;
import ru.parfenov.enums.user.Role;
import ru.parfenov.service.UserService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException; */

/**
 * Класс служит для ограничения посещения страниц юзерам неавторизованным и с разными правами
 */
public class AppFilter { // extends HttpFilter {
  /*  private final UserService userService;

    public AppFilter(UserService userService) {
        this.userService = userService;
    }

     private int getUserId(HttpServletRequest req) {
        int result = 0;
        String authHead = req.getHeader("Authorization");
        if (authHead != null) {
            byte[] e = Base64.decode(authHead.substring(6));
            String idNPass = new String(e);
            String idStr = idNPass.substring(0, idNPass.indexOf(":"));
            int userId = Integer.parseInt(idStr);
            String password = idNPass.substring(idNPass.indexOf(":") + 1);
            if (userService.findByIdAndPassword(userId, password).isPresent()) {
                result = userId;
            }
        }
        return result;
    }

    @Override
    protected void doFilter(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws IOException, ServletException {
        String uri = request.getRequestURI();

        if (isAlwaysPermitted(uri)) {
            chain.doFilter(request, response);
            return;
        }
        if (getUserId(request) == 0) {
            response.setStatus(HttpStatus.UNAUTHORIZED.value());
            return;
        }

        UserRole role = userService.findById(getUserId(request)).get().getRole();

        if (isPermittedForManagerOrAdmin(uri) && (role.equals(UserRole.MANAGER) || role.equals(UserRole.ADMIN))) {
            chain.doFilter(request, response);
            return;
        }
        if (isPermittedForAdmin(uri) && role.equals(UserRole.ADMIN)) {
            chain.doFilter(request, response);
        }
    }

    private boolean isAlwaysPermitted(String uri) {
        return "/sign-up".equals(uri);
    }

    private boolean isPermittedForManagerOrAdmin(String uri) {
        return uri.startsWith("orders/find-by_parameters") ||
                uri.startsWith("orders/close") ||
                uri.startsWith("orders/delete") ||
                uri.startsWith("orders/all") ||
                uri.startsWith("orders/order");
    }

    private boolean isPermittedForAdmin(String uri) {
        return uri.startsWith("/users/user") ||
                uri.startsWith("/users/find-by_parameters") ||
                uri.startsWith("/users/update") ||
                uri.startsWith("/users/delete") ||
                uri.startsWith("/users/create") ||
                uri.startsWith("/users/all") ||
                uri.startsWith("/audit/find-by-parameters");
    } */
}