package ru.parfenov.aspect;

import lombok.RequiredArgsConstructor;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

/**
 * Класс позволяет замерить время выполнения каждого публичного метода в блоке habittracker
 */
@Aspect
@Component
@RequiredArgsConstructor
public class ServiceMethodsLogger {
    /**
     * Метод выводит в лог время выполнение вызываемого метода
     * Лог выводится на консоль и сохраняется в файл.
     *
     * @param joinPoint - точка выполнения метода.
     * Из неё получим название метода, в данные для лога
     */
    @Around("execution(public * ru.parfenov..*(..))")
    public Object getPeriodOfMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        Logger logger = LoggerFactory.getLogger("console and file logger");
        MethodSignature methodSignature = (MethodSignature) joinPoint.getSignature();
        String className = methodSignature.getDeclaringType().getSimpleName();
        String methodName = methodSignature.getName();
        final StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        Object result = joinPoint.proceed();
        stopWatch.stop();
        logger.info("Execution time of {}.{} :: {} ms", className, methodName, stopWatch.getTotalTimeMillis());
        return result;
    }
}