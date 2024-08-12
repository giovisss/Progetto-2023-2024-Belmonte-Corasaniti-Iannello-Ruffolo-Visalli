//package unical.enterprise.jokibackend.Controller;
//
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import lombok.RequiredArgsConstructor;
//import unical.enterprise.jokibackend.Data.Dto.KeycloakUserDTO;
//import unical.enterprise.jokibackend.Data.Services.Interfaces.KeyCloakService;
//import unical.enterprise.jokibackend.Utility.KeycloakManager;
//
//@RestController
//@RequestMapping("/api/auth")
//@CrossOrigin(origins = "*")
//@RequiredArgsConstructor
//public class AuthenticationController {
//   private final KeyCloakService service;
//
//   @PostMapping("/login")
//   public ResponseEntity<String> login(@RequestBody KeycloakUserDTO userDTO) {
//       return ResponseEntity.ok(KeycloakManager.getUserToken(userDTO.getUsername(), userDTO.getPassword()));
//   }
//
//   @PostMapping("/register")
//   public ResponseEntity<String> register(@RequestBody KeycloakUserDTO userDTO) {
//       if(service.addUser(userDTO) != null)
//           return ResponseEntity.ok(KeycloakManager.getUserToken(userDTO.getUsername(), userDTO.getPassword()));
//       return ResponseEntity.badRequest().body("Error while registering user");
//   }
//}