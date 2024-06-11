package unical.enterprise.jokibackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication
public class JokiBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(JokiBackendApplication.class, args);
    }

}
