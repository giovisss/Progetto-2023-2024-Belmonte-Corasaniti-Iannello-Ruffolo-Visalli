package unical.enterprise.jokibackend.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import unical.enterprise.jokibackend.Dto.KeycloakUserDTO;
import unical.enterprise.jokibackend.KeycloakService;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
public class TestUserController {
    @Autowired
    KeycloakService service;

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

    @GetMapping("/privato/{username}")
    @PreAuthorize("#username == authentication.name")
    public String privato(@PathVariable String username) {
        return "HA ACCESSO";
    }

    @GetMapping("/amici/{username}")
    @PreAuthorize("@userPermissionEvaluator.isFriend(authentication, #username)")
    public String amici(@PathVariable String username) {
        return "Sono amici";
    }

    @PostMapping("/user-data")
    public String userData() {
        return "username/id: " + SecurityContextHolder.getContext().getAuthentication().getName();
    }

    @PostMapping("/register")
    public String register(@RequestBody KeycloakUserDTO userDTO){
        System.out.println(userDTO.getUserName());
        System.out.println(userDTO.getPassword());
        System.out.println(userDTO.getEmailId());
        System.out.println(userDTO.getLastName());
        System.out.println(userDTO.getFirstname());

        service.addUser(userDTO);
        return "User Added Successfully.";
    }
}
