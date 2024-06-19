package unical.enterprise.jokibackend.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unical.enterprise.jokibackend.Config.KeycloakConfig;
import unical.enterprise.jokibackend.Data.Services.KeycloakService;
import unical.enterprise.jokibackend.Dto.KeycloakUserDTO;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AuthenticationController {
    private final KeycloakService service;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody KeycloakUserDTO userDTO) {
        return ResponseEntity.ok(KeycloakConfig.getUserToken(userDTO.getUsername(), userDTO.getPassword()));
    }

    @PostMapping("/register")
    public String register(@RequestBody KeycloakUserDTO userDTO) {
        service.addUser(userDTO);
        return "register";
    }
}