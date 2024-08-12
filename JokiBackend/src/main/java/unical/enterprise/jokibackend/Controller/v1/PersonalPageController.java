package unical.enterprise.jokibackend.Controller.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.ws.rs.Produces;

@RestController
@RequestMapping("/tmp/v1")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class PersonalPageController {

    @GetMapping("/user/personal")
    public ResponseEntity<String> getUserPersonalPage() {
        return ResponseEntity.ok("User Personal Page");
    }

    @GetMapping("/admin/personal")
    @Produces("text/plain")
    public ResponseEntity<String> getAdminPersonalPage() {
        System.out.println(SecurityContextHolder.getContext().getAuthentication().getName());
        return ResponseEntity.ok("Admin Personal Page");
    }
}
