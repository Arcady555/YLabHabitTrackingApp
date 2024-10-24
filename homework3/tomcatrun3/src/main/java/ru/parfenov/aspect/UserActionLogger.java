package ru.parfenov.aspect;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import ru.parfenov.homework_3.model.User;
import ru.parfenov.homework_3.service.LogService;
import ru.parfenov.homework_3.utility.Utility;

import java.time.LocalDateTime;

@Aspect
@Slf4j
public class UserActionLogger {
    private final LogService service = Utility.loadLogService();

    public UserActionLogger() throws Exception {
    }

    @Pointcut("execution(* ru.parfenov.homework_3.servlets..*")
    public void pickMethods() {
    }

    @After("execution(* *(..)) && args(request, response)")
    public void logUserAction(JoinPoint joinPoint, HttpServletRequest request, HttpServletResponse response) throws Exception {
        if (response.getStatus() == 200) {
            LocalDateTime dateTime = LocalDateTime.now();
            int userId = ((User) request.getSession().getAttribute("user")).getId();
            String action = joinPoint.getSourceLocation().getWithinType().getName();
            log.info("date time : {}, user id : {}, action : {}", dateTime, userId, action);
            service.saveLineInLog(dateTime, userId, action);
        }
    }
}
