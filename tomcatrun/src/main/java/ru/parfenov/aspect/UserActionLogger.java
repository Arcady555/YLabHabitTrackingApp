package ru.parfenov.aspect;

import javax.servlet.http.HttpServletRequest;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import ru.parfenov.utility.Utility;

import java.time.LocalDateTime;

/**
 * Класс записывает в лог действия юзеров (вызванные с их подачи методы классов блока CONTROLLER)
 */
@Aspect
@Component
public class UserActionLogger {
    /**
     * Метод принимает информацию(время события, ID юзера и название его действия)
     * и отправляет её в БД.
     *
     * @param joinPoint точка выполнения метода.
     * @param request   запрос в прошиваемом контроллере
     */
    @AfterReturning(pointcut = "execution(public * ru.homework4.controller..*(..)) && args(request)", returning = "result")
    public void logUserActions(JoinPoint joinPoint, HttpServletRequest request) {
        Logger logger = LoggerFactory.getLogger("data base logger");
        LocalDateTime dateTime = LocalDateTime.now();
        String email = Utility.getUserEmail(request);
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String action = methodSignature.getDeclaringType().getSimpleName() + "." +
                methodSignature.getName();
        logger.info(email + ", date time : " + dateTime + ", action : " + action);
    }
}