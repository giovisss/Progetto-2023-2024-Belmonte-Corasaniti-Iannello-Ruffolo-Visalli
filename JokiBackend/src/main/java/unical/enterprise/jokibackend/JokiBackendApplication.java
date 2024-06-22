package unical.enterprise.jokibackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication
@EnableAspectJAutoProxy
public class JokiBackendApplication {
    public static void main(String[] args) {
        SpringApplication.run(JokiBackendApplication.class, args);
    }
}
