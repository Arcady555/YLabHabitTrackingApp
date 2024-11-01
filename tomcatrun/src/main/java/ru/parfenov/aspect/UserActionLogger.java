package ru.parfenov.aspect;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import ru.parfenov.service.UserService;
import ru.parfenov.utility.ServiceLoading;
import ru.parfenov.utility.Utility;

/**
 * Класс аспекта. В случае успешного выполнения метода в слое клиентских сервлетов
 * выводит запись лога в БД. С емайлом юзера и названием сервлета, который он использовал
 */
@Aspect
public class UserActionLogger {
  private final UserService userService = ServiceLoading.loadUserService();

  public UserActionLogger() throws Exception {
  }

  @Pointcut("execution(* ru.parfenov.servlets.client..*")
  public void pickMethods() {
  }

  @After("execution(* *(..)) && args(request, response)")
  public void logUserAction(JoinPoint joinPoint, HttpServletRequest request, HttpServletResponse response) throws Exception {
    if (response.getStatus() == 200) {
      String userEmail = Utility.checkUserByEmailNPass(request, userService).get().getEmail();
      String action = joinPoint.getSourceLocation().getWithinType().getName();
      Logger logger = LogManager.getLogger("db logger");
      logger.info("user : {}, action : {}", userEmail, action);
    }
  }
}