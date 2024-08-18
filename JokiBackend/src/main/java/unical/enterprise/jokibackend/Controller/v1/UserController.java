package unical.enterprise.jokibackend.Controller.v1;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import unical.enterprise.jokibackend.Data.Dto.GameDto;
import unical.enterprise.jokibackend.Data.Dto.UpdateUserDto;
import unical.enterprise.jokibackend.Data.Services.KeycloakServiceImpl;
import unical.enterprise.jokibackend.Data.Services.UserServiceImpl;
import unical.enterprise.jokibackend.Utility.CustomContextManager.UserContextHolder;

import javax.ws.rs.Produces;
import java.util.Collection;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class UserController {
    private final KeycloakServiceImpl keycloakService;
    private final UserServiceImpl userService;

    @GetMapping("/user")
    @Produces("application/json")
    public ResponseEntity<String> getCurrentUser() {
        var out = userService.getUserByUsername(UserContextHolder.getContext().getPreferredUsername());
        return ResponseEntity.ok(new Gson().toJson(out));
    }

    @PutMapping("/user")
    @Produces("application/json")
    public ResponseEntity<String> updateCurrentUser(@RequestBody UpdateUserDto userDto) {
        if(keycloakService.updateUser(UserContextHolder.getContext().getPreferredUsername(), userDto)) return ResponseEntity.ok("User updated");
        else return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/user")
    @Produces("application/json")
    public ResponseEntity<String> deleteCurrentUser() {
        keycloakService.deleteUser(UserContextHolder.getContext().getPreferredUsername());
        return ResponseEntity.ok("User deleted");
    }

    @GetMapping("/{username}")
    @PreAuthorize("@userPermissionEvaluator.canVisulize(#username)")
    @Produces("application/json")
    public ResponseEntity<String> getUser(@PathVariable String username) {
       var out = userService.getUserByUsername(username);

       if (out == null) return ResponseEntity.notFound().build();
       else return ResponseEntity.ok(new Gson().toJson(out));
    }

    // Controller libreria
    @GetMapping(value = "/user/library", produces = "application/json")
    public ResponseEntity<String> getUserGameList() {
    //        var out = userService.getUsernameGames(SecurityContextHolder.getContext().getAuthentication().getName());
        var out = userService.getGamesByUsername(UserContextHolder.getContext().getPreferredUsername());
        return ResponseEntity.ok(new Gson().toJson(out));
    }

    // TODO: correggere logica pagamento
    @PostMapping(value = "/user/library/{gameId}", produces = "application/json")
    public ResponseEntity<String> addGameToLibrary(@PathVariable UUID gameId) {
        boolean added = userService.addGameToUserLibrary(UserContextHolder.getContext().getPreferredUsername(), gameId);
        if (added) {
            return ResponseEntity.ok("{\"message\": \"Game added to library successfully\", \"gameId\": \"" + gameId + "\"}");
        } else {
            return ResponseEntity.badRequest().body("{\"mesage\": \"Failed to add game to library\"}");
        }
    }
    // Fine controller libreria

    // Controller carrello
    @GetMapping(value = "/user/cart", produces = "application/json")
    public ResponseEntity<String> getUserCart() {
        Collection<GameDto> cartGames = userService.getUserCart(UserContextHolder.getContext().getPreferredUsername());
        return ResponseEntity.ok(new Gson().toJson(cartGames));
    }

    @DeleteMapping(value = "/user/cart", produces = "application/json")
    public ResponseEntity<String> clearUserCart() {
        userService.clearUserCart(UserContextHolder.getContext().getPreferredUsername());
        return ResponseEntity.ok("{\"message\": \"Cart cleared\"}");
    }

    @PostMapping(value = "/user/cart/{gameId}", produces = "application/json")
    public ResponseEntity<String> addGameToCart(@PathVariable UUID gameId) {
        boolean added = userService.addGameToUserCart(UserContextHolder.getContext().getPreferredUsername(), gameId);
        if (added) {
            return ResponseEntity.ok("{\"message\": \"Game added to cart successfully\", \"gameId\": \"" + gameId + "\"}");
        } else {
            return ResponseEntity.badRequest().body("{\"message\": \"Failed to add game to cart\"}");
        }
    }

    @DeleteMapping(value = "/user/cart/{gameId}", produces = "application/json")
    public ResponseEntity<String> removeGameFromCart(@PathVariable UUID gameId) {
        boolean removed = userService.removeGameFromUserCart(UserContextHolder.getContext().getPreferredUsername(), gameId);
        if (removed) {
            return ResponseEntity.ok("{\"message\": \"Game removed from cart successfully\", \"gameId\": \"" + gameId + "\"}");
        } else {
            return ResponseEntity.badRequest().body("{\"message\": \"Failed to remove game from cart\"}");
        }
    }
    // Fine controller carrello
}