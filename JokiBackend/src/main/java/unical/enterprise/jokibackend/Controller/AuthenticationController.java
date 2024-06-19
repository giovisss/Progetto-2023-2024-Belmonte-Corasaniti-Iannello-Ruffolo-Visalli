package unical.enterprise.jokibackend.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AuthenticationController {

    @PostMapping("/login")
    public String login(@RequestBody String username, @RequestBody String password) {
        return "login";
    }

    @PostMapping("/register")
    public String register(@RequestBody String username, @RequestBody String password) {
        return "register";
    }
}