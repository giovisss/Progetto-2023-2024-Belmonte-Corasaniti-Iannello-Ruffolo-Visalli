package unical.enterprise.jokibackend.Component;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import jakarta.servlet.http.HttpServletRequest;

import java.util.Arrays;
import java.util.logging.Logger;

@Aspect
@Component
public class AuditLoggingAspect {

    Logger logger = Logger.getLogger(AuditLoggingAspect.class.getName());

    @Pointcut("execution(* unical.enterprise.jokibackend.Controller.*.*(..))")
    public void allControllerMethods() {}

    @Before("allControllerMethods()")
    public void logBefore(JoinPoint joinPoint) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        logger.info("--------------------------------------------------");
        logger.info("User: " + request.getUserPrincipal().getName());
        logger.info("Request Method: " + request.getMethod());
        logger.info("Endpoint: " + request.getRequestURI());
        logger.info("Arguments: " + Arrays.toString(joinPoint.getArgs()));
    }

    @AfterReturning(pointcut = "allControllerMethods()", returning = "result")
    public void logAfter(JoinPoint joinPoint, Object result) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        logger.info("Response: " + result);
        logger.info("--------------------------------------------------");
    }
}