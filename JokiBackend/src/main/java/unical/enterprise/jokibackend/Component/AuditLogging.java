package unical.enterprise.jokibackend.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import unical.enterprise.jokibackend.Utility.CustomContextManager.UserContextHolder;

import java.util.Arrays;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

@Aspect
@Component
@Transactional
public class AuditLogging {

    private static final Logger logger = Logger.getLogger(AuditLogging.class.getName());
    private final ReentrantLock lock = new ReentrantLock();

    @Pointcut("execution(* unical.enterprise.jokibackend.Controller..*.*(..))")
    public void allControllerMethods() {}

    @Before("allControllerMethods()")
    public void logBefore(JoinPoint joinPoint) {
        lock.lock();
        try {
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

            logger.info("--------------------------------------------------");

            if(request.getUserPrincipal() == null) logger.info("User: Anonymous");
            else logger.info("User: " + UserContextHolder.getContext().toShortString());

            logger.info("Request Method: " + request.getMethod());
            logger.info("Endpoint: " + request.getRequestURI());

            logger.info("Arguments: " + Arrays.toString(joinPoint.getArgs()));
            logger.info("Signature: " + joinPoint.getSignature());
        } catch (Exception e) {
            logger.severe(e.getMessage());
        }
    }

    @AfterReturning(pointcut = "allControllerMethods()", returning = "result")
    public void logAfter(Object result) {
        try {
            logger.info("Response: " + result);
            logger.info("--------------------------------------------------");
        } catch (Exception e){
            logger.severe(e.getMessage());
        } finally {
            lock.unlock();
        }
    }
}