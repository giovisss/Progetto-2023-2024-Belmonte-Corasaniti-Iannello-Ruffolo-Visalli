package unical.enterprise.jokibackend.Component;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.*;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import unical.enterprise.jokibackend.Utility.CustomContextManager.UserContextHolder;

import java.util.Arrays;
import java.util.logging.Logger;

@Aspect
@Component
@Scope(WebApplicationContext.SCOPE_REQUEST)
@Transactional
public class AuditLogging {

    private static final Logger logger = Logger.getLogger(AuditLogging.class.getName());
    private static final ThreadLocal<StringBuilder> logBuilder = ThreadLocal.withInitial(StringBuilder::new);

    @Pointcut("execution(* unical.enterprise.jokibackend.Controller..*.*(..))")
    public void allControllerMethods() {}

    @Before("allControllerMethods()")
    public void logBefore(JoinPoint joinPoint) {
        StringBuilder builder = logBuilder.get();
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        builder.append("\n----------------------------------------------------------------------------------------------------\n");

        if(request.getUserPrincipal() == null) builder.append("User: Anonymous\n");
        else builder.append("User: {").append(UserContextHolder.getContext().toShortString()).append("}\n");

        builder.append("Request Method: ").append(request.getMethod()).append("\n");
        builder.append("Endpoint: ").append(request.getRequestURI()).append("\n");
        builder.append("Arguments: ").append(Arrays.toString(joinPoint.getArgs())).append("\n");
        builder.append("Signature: ").append(joinPoint.getSignature()).append("\n");
    }

    @AfterReturning(pointcut = "allControllerMethods()", returning = "result")
    public void logAfter(Object result) {
        StringBuilder builder = logBuilder.get();
        builder.append("Response: ").append(result).append("\n");
        builder.append("----------------------------------------------------------------------------------------------------\n");
        logger.info(builder.toString());
        logBuilder.remove();
    }

    @AfterThrowing(pointcut = "allControllerMethods()", throwing = "error")
    public void logAfterThrowing(JoinPoint joinPoint, Throwable error) {
        StringBuilder builder = logBuilder.get();
        builder.append("****************************************************************************************************\n");
        builder.append("AN EXCEPTION HAS BEEN THROWN\n");
        builder.append("Cause : ").append(error).append("\n");
        builder.append("****************************************************************************************************\n");
        builder.append("----------------------------------------------------------------------------------------------------");
        logger.info(builder.toString());
        logBuilder.remove();
    }
}