package ru.parfenov.aspect;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.time.LocalTime;

/**
 * Класс позволяет замерить время выполнения каждого метода из блока SERVICE
 */

@Aspect
@Slf4j
public class MethodsLogger {

    /**
     * Метод определил, какие методы будут собраны для среза
     * (в данном случае - все из блока tomcatrun3)
     */
    @Pointcut("execution(public ru.parfenov..*")
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
    public void getPeriodOfMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        Logger logger = LogManager.getLogger("console and file");
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        stopWatch.stop();
        logger.info("Execution time of {}.{} :: {} ms", className, methodName, stopWatch.getNanoTime());
    }
}