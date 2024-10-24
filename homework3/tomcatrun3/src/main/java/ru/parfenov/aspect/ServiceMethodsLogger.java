package ru.parfenov.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;

import java.time.Duration;
import java.time.LocalTime;

/**
 * Класс позволяет замерить время выполнения каждого метода из блока SERVICE
 */

@Aspect
@Slf4j
public class ServiceMethodsLogger {

    /**
     * Метод определил, какие методы будут собраны для среза
     * (в данном случае - все из блока SERVICE)
     */
    @Pointcut("execution(public ru.parfenov.homework_3.service..*")
    public void pickMethods() {
    }

    /**
     * @return текущее время на момент начала выполнения метода
     */
    @Before("execution(* *(..))")
    public LocalTime freezeStart() {
        return LocalTime.now();
    }

    /**
     * @param joinPoint - точка выполнения метода.
     * Из неё получим название метода, в данные для лога
     */
    @After("execution(* *(..))")
    public void getPeriodOfMethod(JoinPoint joinPoint) {
        LocalTime timeOfFinish = LocalTime.now();
        String methodName = joinPoint.getSignature().getName();
        log.info("Period of method {} {}", methodName, Duration.between(timeOfFinish, freezeStart()));
    }
}