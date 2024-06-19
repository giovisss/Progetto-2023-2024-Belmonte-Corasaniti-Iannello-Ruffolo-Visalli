package unical.enterprise.jokibackend.Controller;

import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import unical.enterprise.jokibackend.Data.Dao.UserDao;
import unical.enterprise.jokibackend.Data.Entities.User;
import unical.enterprise.jokibackend.Data.Services.KeycloakService;
import unical.enterprise.jokibackend.Dto.KeycloakUserDTO;
import unical.enterprise.jokibackend.Dto.UserDto;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class TestUserController {

    private final KeycloakService service;
    private final UserDao userDao;
    private final ModelMapper modelMapper;

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
    public ResponseEntity<String> register(@RequestBody KeycloakUserDTO userDTO){
        try {
            service.addUser(userDTO);
            createLocalUser(userDTO);
            return ResponseEntity.ok("User added successfully");
        }
        catch (Exception e) {
            return ResponseEntity.badRequest().body("Error adding user");
        }
    }

    private User createLocalUser(KeycloakUserDTO userDTO) {
        // per mappare direttamente dovremmo avere lo stesso nome degli attributi
        // return userDao.save(modelMapper.map(userDTO, User.class));
        UserDto user = new UserDto();
        user.setUsername(userDTO.getUserName());
        user.setEmail(userDTO.getEmailId());
        user.setName(userDTO.getFirstname());
        user.setSurname(userDTO.getLastName());
        user.setPassword(userDTO.getPassword());
        User userEntity = modelMapper.map(user, User.class);
        return userDao.save(userEntity);
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<KeycloakUserDTO> getUser(@PathVariable String username){
        try {
            var tmp = service.getUser(username).get(0);

            KeycloakUserDTO userDTO = new KeycloakUserDTO();
            userDTO.setUserName(tmp.getUsername());
            userDTO.setEmailId(tmp.getEmail());
            userDTO.setFirstname(tmp.getFirstName());
            userDTO.setLastName(tmp.getLastName());

            return ResponseEntity.ok(userDTO);
        }
        catch (Exception e) {
            return ResponseEntity.notFound().build();
        }

    }
}
