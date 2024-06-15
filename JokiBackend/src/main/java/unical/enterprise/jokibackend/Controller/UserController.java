package unical.enterprise.jokibackend.Controller;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import unical.enterprise.jokibackend.Service.KeycloakAdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class UserController {

//    @Autowired
//    private KeycloakAdminService keycloakAdminService;
//
//    @GetMapping("/{userId}/roles")
//    public List<String> getUserRoles(@PathVariable String userId) {
//        System.out.println("userId: " + userId);
//        keycloakAdminService.getUserRoles(userId).forEach(System.out::println);
//        return keycloakAdminService.getUserRoles(userId);
//    }

    @GetMapping("/diocane")
    @PreAuthorize("hasRole('client_user')")
    public String diocane() {
        return "diocane";
    }

    @GetMapping("/diocane-2")
    @PreAuthorize("hasRole('client_admin')")
    public String diocane2() {
        return "diocane 2";
    }
}
