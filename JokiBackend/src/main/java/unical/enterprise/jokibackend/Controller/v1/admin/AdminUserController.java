package unical.enterprise.jokibackend.Controller.v1.admin;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unical.enterprise.jokibackend.Data.Dto.UpdateUserDto;
import unical.enterprise.jokibackend.Data.Services.Interfaces.UserService;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/users")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AdminUserController {
    private final UserService userService;

    @GetMapping("")
    public ResponseEntity<String> getUsersList(){
        var out = userService.getAllUsers();
        return ResponseEntity.ok(new Gson().toJson(out));
    }

    @GetMapping("/{username}")
    public ResponseEntity<String> getUser(@PathVariable String username) {
        var out = userService.getUserByUsername(username);
        if (out == null) return ResponseEntity.notFound().build();
        else return ResponseEntity.ok(new Gson().toJson(out));
    }

    @DeleteMapping("/{username}")
    public ResponseEntity<String> deleteUser(@PathVariable String username) {
        userService.deleteByUsername(username);
        return ResponseEntity.ok(new Gson().toJson("User deleted"));
    }

    @PutMapping("/{username}")
    public ResponseEntity<String> updateUser(@PathVariable String username, @RequestBody UpdateUserDto userDto) {
        if(userService.updateUser(username, userDto)) return ResponseEntity.ok(new Gson().toJson("User updated"));
        else return ResponseEntity.notFound().build();
    }

    // libreria controller
    @GetMapping("/{username}/library")
    public ResponseEntity<String> getUserLibrary(@PathVariable String username) {
        var out = userService.getGamesByUsername(username);
        return ResponseEntity.ok(new Gson().toJson(out));
    }

    @PostMapping("/{username}/library/{gameId}")
    public ResponseEntity<String> addGameToUserLibrary(@PathVariable String username, @PathVariable UUID gameId) {
        if(userService.addGameToUserLibrary(username, gameId)) return ResponseEntity.ok(new Gson().toJson("Game added to library"));
        else return ResponseEntity.notFound().build();
    }

    @DeleteMapping("/{username}/library/{gameId}")
    public ResponseEntity<String> removeGameFromUserLibrary(@PathVariable String username, @PathVariable UUID gameId) {
        if(userService.removeGameFromUserLibrary(username, gameId)) return ResponseEntity.ok(new Gson().toJson("Game removed from library"));
        else return ResponseEntity.notFound().build();
    }
    // end libreria controller

    // cart controller
    @GetMapping("/{username}/cart")
    public ResponseEntity<String> getUserCart(@PathVariable String username) {
        var out = userService.getUserCart(username);
        return ResponseEntity.ok(new Gson().toJson(out));
    }

    @DeleteMapping("/{username}/cart")
    public ResponseEntity<String> clearUserCart(@PathVariable String username) {
        userService.clearUserCart(username);
        return ResponseEntity.ok(new Gson().toJson("Cart cleared"));
    }

    @DeleteMapping("/{username}/cart/{gameId}")
    public ResponseEntity<String> removeGameFromUserCart(@PathVariable String username, @PathVariable UUID gameId) {
        if(userService.removeGameFromUserCart(username, gameId)) return ResponseEntity.ok(new Gson().toJson("Game removed from cart"));
        else return ResponseEntity.notFound().build();
    }

    @PostMapping("/{username}/cart/{gameId}")
    public ResponseEntity<String> addGameToUserCart(@PathVariable String username, @PathVariable UUID gameId) {
        if(userService.addGameToUserCart(username, gameId)) return ResponseEntity.ok(new Gson().toJson("Game added to cart"));
        else return ResponseEntity.notFound().build();
    }
    // end cart controller
}
