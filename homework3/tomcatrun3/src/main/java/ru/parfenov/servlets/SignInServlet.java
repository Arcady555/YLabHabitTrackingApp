package ru.parfenov.servlets;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import ru.parfenov.homework_3.dto.UserDTOMapper;
import ru.parfenov.homework_3.dto.UserDTOMapperImpl;
import ru.parfenov.homework_3.dto.UserIdPassDTO;
import ru.parfenov.homework_3.model.User;
import ru.parfenov.homework_3.service.UserService;
import ru.parfenov.homework_3.utility.Utility;

import java.io.IOException;
import java.util.Optional;

/**
 * Страница входа в систему через id b пароль
 */
@Slf4j
@WebServlet(name = "SignInServlet", urlPatterns = "/sign-in")
public class SignInServlet extends HttpServlet implements MethodsForServlets {
    private final UserService userService;

    public SignInServlet() throws Exception {
        userService = Utility.loadUserservice();
    }

    public SignInServlet(UserService userService) {
        this.userService = userService;
    }

    /**
     * Метод обработает HTTP запрос Post.
     * Проверяется, что юзер с таким ID и паролем есть в БД
     *
     * @param request  запрос клиента
     * @param response ответ сервера
     * @throws IOException исключение при вводе-выводе
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        UserDTOMapper mapper = new UserDTOMapperImpl();
        String userJson = getStringJson(request);
        ObjectMapper objectMapper = new ObjectMapper();
        UserIdPassDTO userDTO = objectMapper.readValue(userJson, UserIdPassDTO.class);
        Optional<User> userOptional =
                userService.findByIdAndPassword(userDTO.getId(), userDTO.getPassword());
        String userJsonString;
        int responseStatus;
        if (userOptional.isPresent()) {
            userJsonString = objectMapper.writeValueAsString(mapper.toUserIdNameRoleDTO(userOptional.get()));
            responseStatus = 200;
            var session = request.getSession();
            session.setAttribute("user", userOptional.get());
        } else {
            userJsonString = "user is not found!";
            responseStatus = 404;
        }
        response.setStatus(responseStatus);
        finish(response, userJsonString);
    }
}