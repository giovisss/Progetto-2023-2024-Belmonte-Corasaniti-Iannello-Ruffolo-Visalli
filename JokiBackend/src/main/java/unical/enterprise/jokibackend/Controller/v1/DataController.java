package unical.enterprise.jokibackend.Controller.v1;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DataController {
    @GetMapping("/api/data")
    public String getData() {
        return "Protected Data";
    }
}