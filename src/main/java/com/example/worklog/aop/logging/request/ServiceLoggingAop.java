package com.example.worklog.aop.logging.request;

import com.example.worklog.aop.logging.RequestLogger;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

@Aspect
@Slf4j
@Component
@RequiredArgsConstructor
public class ServiceLoggingAop {
    private final RequestLogger logger;
    @Pointcut("execution(* com.example.worklog.service..*.*(..))")
    private void cut(){}

    @Before("cut()")
    public void beforeParameterLog(JoinPoint joinPoint) {
        // 메서드 정보 받아오기
        String methodName = getMethodName(joinPoint);
        log.info("{}===== METHOD CALL : {} =====", logger, methodName);

        // 파라미터 받아오기
        Object[] args = joinPoint.getArgs();
        if (args.length <= 0) log.info("{}PARAMETER = {}", logger, "no parameter");
        for (Object arg : args) {
            log.info("{}PARAMETER = {}", logger, arg);
        }
    }

    @AfterReturning(value = "cut()", returning = "returnObj")
    public void afterReturnLog(JoinPoint joinPoint, Object returnObj) {

        String methodName = getMethodName(joinPoint);
        log.info("{}===== METHOD RETURN : {} =====", logger, methodName);
        if (returnObj != null) {
            log.info("{}PARAMETER = {}", logger, returnObj);
        }
    }

    private String getMethodName(JoinPoint joinPoint) {
        String classNameWithDirectory = joinPoint.getTarget().getClass().getName();
        String className = classNameWithDirectory.substring(classNameWithDirectory.lastIndexOf('.') + 1);
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getMethod().getName();
        return String.format("%s.%s()", className, methodName);
    }
}
