package unical.enterprise.jokibackend.Controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/user-data")
    public String userData() {
        return "username/id: " + SecurityContextHolder.getContext().getAuthentication().getName();
    }
}
