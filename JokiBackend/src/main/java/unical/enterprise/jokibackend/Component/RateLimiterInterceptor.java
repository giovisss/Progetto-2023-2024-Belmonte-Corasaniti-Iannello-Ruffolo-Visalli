package unical.enterprise.jokibackend.Component;

import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import com.google.common.util.concurrent.RateLimiter;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class RateLimiterInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(RateLimiterInterceptor.class);
    private final RateLimiter rateLimiter = RateLimiter.create(10.0); // massimo 10 richieste al secondo
    private final AtomicInteger requestCount = new AtomicInteger(0);
    private volatile long windowStart = System.currentTimeMillis();
    private static final long WINDOW_SIZE_MS = 1000;
    private static final long MAX_REQUEST_DURATION_MS = 10000;

    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        long currentTime = System.currentTimeMillis();
        
        // se tra una richiesta e l'altra passa più di 1 secondo, azzera il contatore delle richieste
        if (currentTime - windowStart >= WINDOW_SIZE_MS) {
            requestCount.set(0);
            windowStart = currentTime;
        }

        int currentCount = requestCount.incrementAndGet();
        
        // se le richieste sono più di 10 al secondo, restituisci un errore 429 (Too Many Requests)
        if (!rateLimiter.tryAcquire() || currentCount > 10) {
            setRateLimitExceededResponse(response);
            logger.warn("Rate limit exceeded for request: {} {} (Count: {})", 
                       request.getMethod(), 
                       request.getRequestURI(),
                       currentCount);
            requestCount.decrementAndGet();
            return false;
        }

        // interrompe la richeista se passano più di 10 secondi
        ScheduledFuture<?> timeoutTask = scheduler.schedule(() -> {
            if (!response.isCommitted()) {
                try {
                    setRequestTimeoutResponse(response);
                } catch (IOException e) {
                    logger.error("Error setting timeout response", e);
                }
            }
        }, MAX_REQUEST_DURATION_MS, TimeUnit.MILLISECONDS);

        logger.info("Request allowed: {} {} (Count: {})", 
                   request.getMethod(), 
                   request.getRequestURI(),
                   currentCount);

        timeoutTask.cancel(false);
        return true;
    }

    private void setRateLimitExceededResponse(HttpServletResponse response) throws IOException {
        response.setStatus(429); // Too Many Requests
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"Rate limit exceeded. Please try again later.\", \"status\": 429}");
        response.flushBuffer();
    }

    private void setRequestTimeoutResponse(HttpServletResponse response) throws IOException {
        response.setStatus(503); // Service Unavailable
        response.setContentType("application/json");
        response.getWriter().write("{\"error\": \"Request timed out. Please try again later.\", \"status\": 503}");
        response.flushBuffer();
        logger.warn("Request timed out after exceeding 10 seconds");
    }
}
