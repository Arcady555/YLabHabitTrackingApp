package ru.parfenov.aspect;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

import static ru.parfenov.utility.Utility.getUserEmail;

/**
 * Класс записывает в лог действия юзеров (вызванные с их подачи методы классов блока CONTROLLER)
 */
@Aspect
@Component
@RequiredArgsConstructor
public class UserActionLogger {
    /**
     * Метод принимает информацию(время события, ID юзера и название его действия)
     * и отправляет её в БД.
     * Действует в тех методах (классов блока habittracker), которые помечены аннотацией @EnableParfenovCustomAspect
     *
     * @param joinPoint точка выполнения метода.
     */
    @AfterReturning(value = "execution(@ru.parfenov.anotation.EnableParfenovCustomAspect * *(..))")
    public void enableParfenovCustomAspect(JoinPoint joinPoint) {
        Logger logger = LoggerFactory.getLogger("data base logger");
        LocalDateTime dateTime = LocalDateTime.now();
        String userEmail = getUserEmail();
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String action = methodSignature.getDeclaringType().getSimpleName() + "." +
                methodSignature.getName();
        logger.info(userEmail + ", date time : " + dateTime + ", action : " + action);
    }
}