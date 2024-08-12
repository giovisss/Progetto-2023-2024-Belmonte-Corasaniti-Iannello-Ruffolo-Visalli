package unical.enterprise.jokibackend.Controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import unical.enterprise.jokibackend.Utility.KeycloakManager;

@RestController
public class PersonalPageController {

    @GetMapping("/api/user/personal")
    public ResponseEntity<String> getUserPersonalPage() {
        return ResponseEntity.ok("User Personal Page");
    }

    @GetMapping("/api/admin/personal")
    public ResponseEntity<String> getAdminPersonalPage() {
        return ResponseEntity.ok("Admin Personal Page");
    }
}
