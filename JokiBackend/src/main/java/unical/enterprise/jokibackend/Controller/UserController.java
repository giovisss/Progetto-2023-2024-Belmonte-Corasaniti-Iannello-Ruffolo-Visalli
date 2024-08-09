package unical.enterprise.jokibackend.Controller;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import unical.enterprise.jokibackend.Data.Dto.UpdateUserDto;
import unical.enterprise.jokibackend.Data.Services.KeycloakServiceImpl;
import unical.enterprise.jokibackend.Data.Services.UserServiceImpl;
import unical.enterprise.jokibackend.Utility.CustomContextManager.UserContextHolder;

import javax.ws.rs.Produces;

import java.util.logging.Logger;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class UserController {
   Logger logger = Logger.getLogger(UserController.class.getName());

   private final KeycloakServiceImpl keycloakService;
   private final UserServiceImpl userService;

    // lista degli utenti
   @GetMapping("")
   @PreAuthorize("hasRole('client_admin')")
   @Produces("application/json")
   public ResponseEntity<String> getUsersList(){
       try {
           Gson gson = new Gson();
           var out = userService.getAllUsers();
           return ResponseEntity.ok(gson.toJson(out));
       }
       catch (Exception e) {
           logger.warning(e.getMessage());
           return ResponseEntity.badRequest().body("An error occurred");
       }
   }

   @GetMapping("/{username}")
   @PreAuthorize("hasRole('client_admin') or #username == authentication.name")
   @Produces("application/json")
   public ResponseEntity<String> getUser(@PathVariable String username) {
       try {
           Gson gson = new Gson();
           var out = userService.getUserByUsername(username);

           if (out == null) return ResponseEntity.notFound().build();
           else return ResponseEntity.ok(gson.toJson(out));
       }
       catch (Exception e) {
           logger.warning(e.getMessage());
           return ResponseEntity.badRequest().body("An error occurred");
       }
   }

    // modifica un utente
   @PutMapping("/{username}")
   @PreAuthorize("hasRole('client_admin') or #username == authentication.name")
   @Produces("application/json")
   public ResponseEntity<String> updateUser(@PathVariable String username, @RequestBody UpdateUserDto userDto) {
       try {
           if(keycloakService.updateUser(username, userDto)) return ResponseEntity.ok("User updated");
           else return ResponseEntity.notFound().build();
       }
       catch (Exception e) {
           logger.warning(e.getMessage());
           return ResponseEntity.badRequest().body("An error occurred");
       }
       //return ResponseEntity.internalServerError().body("Not implemented");
   }

//    @DeleteMapping("/{username}")
//    @PreAuthorize("hasRole('client_admin') or #username == authentication.name")
//    @Produces("plain/text")
//    public ResponseEntity<String> deleteUser(@PathVariable String username) {
//        try {
//            userService.deleteByUsername(username);
//            return ResponseEntity.ok("User deleted");
//        }
//        catch (Exception e) {
//            logger.warning(e.getMessage());
//            return ResponseEntity.badRequest().body("An error occurred");
//        }
//    }

    // elimina un utente
    @DeleteMapping("/{username}")
    @PreAuthorize("hasRole('client_admin') or #username == authentication.name")
    @Produces("plain/text")
    public ResponseEntity<String> deleteUser(@PathVariable String username) {
       try {
           keycloakService.deleteUser(username);
           return ResponseEntity.ok("User deleted");
       }
       catch (Exception e) {
           logger.warning(e.getMessage());
           return ResponseEntity.badRequest().body("An error occurred");
       }
        //return ResponseEntity.internalServerError().body("Not implemented");
    }

    @GetMapping(value = "/games", produces = "application/json")
    @PreAuthorize("hasRole('client_user')")
    public ResponseEntity<String> getUserGameList() {
        try {
            Gson gson = new Gson();
            // var out = userService.getUsernameGames(SecurityContextHolder.getContext().getAuthentication().getName());
            var out = userService.getUsernameGames(UserContextHolder.getContext().getPreferredUsername());
            return ResponseEntity.ok(gson.toJson(out));
        }
        catch (Exception e) {
            logger.warning(e.getMessage());
            return ResponseEntity.badRequest().body("An error occurred");
        }
    }
}