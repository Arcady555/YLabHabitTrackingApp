package ru.parfenov.servlets.admin;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import ru.parfenov.homework_3.dto.UserAllParamDTO;
import ru.parfenov.homework_3.enums.UserRole;
import ru.parfenov.homework_3.model.User;
import ru.parfenov.homework_3.service.UserService;
import ru.parfenov.homework_3.servlets.MethodsForServlets;
import ru.parfenov.homework_3.utility.Utility;

import java.io.IOException;
import java.util.Optional;

/**
 * Страница, где админ может сам создать любого юзера и с нужным профилем
 */
@Slf4j
@WebServlet(name = "CreateUserServlet", urlPatterns = "/create-user")
public class CreateUserServlet extends HttpServlet implements MethodsForServlets {
    private final UserService userService;

    public CreateUserServlet() throws Exception {
        userService = Utility.loadUserservice();
    }

    public CreateUserServlet(UserService userService) {
        this.userService = userService;
    }

    /**
     * Метод обработает HTTP запрос Post
     * Есть проверки:
     * что юзер открыл сессию,
     * что зарегистрирован
     * что обладает правами админа
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
        String userJsonString = "no rights or registration!";
        if (user != null && user.getRole() == UserRole.ADMIN) {
            String userJson = getStringJson(request);
            ObjectMapper objectMapper = new ObjectMapper();
            UserAllParamDTO userDTO = objectMapper.readValue(userJson, UserAllParamDTO.class);
            Optional<User> userOptional = userService.createByAdmin(
                    0,
                    userDTO.getRole(),
                    userDTO.getName(),
                    userDTO.getPassword(),
                    userDTO.getContactInfo(),
                    userDTO.getBuysAmount()
            );
            userJsonString = userOptional.isPresent() ?
                    objectMapper.writeValueAsString(userOptional.get()) :
                    "user is not created!";
            responseStatus = "user is not created!".equals(userJsonString) ? 404 : 200;
        }
        response.setStatus(responseStatus);
        finish(response, userJsonString);
    }
}