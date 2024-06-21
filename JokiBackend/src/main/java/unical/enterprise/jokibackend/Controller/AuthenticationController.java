package unical.enterprise.jokibackend.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unical.enterprise.jokibackend.Utility.KeycloakManager;
import unical.enterprise.jokibackend.Data.Services.KeycloakServiceImpl;
import unical.enterprise.jokibackend.Data.Dto.KeycloakUserDTO;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AuthenticationController {
    private final KeycloakServiceImpl service;

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody KeycloakUserDTO userDTO) {
        return ResponseEntity.ok(KeycloakManager.getUserToken(userDTO.getUsername(), userDTO.getPassword()));
    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody KeycloakUserDTO userDTO) {
        if(service.addUser(userDTO) != null)
            return ResponseEntity.ok(KeycloakManager.getUserToken(userDTO.getUsername(), userDTO.getPassword()));

        return ResponseEntity.badRequest().body("Error while registering user");
    }
}