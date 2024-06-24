package unical.enterprise.jokibackend.Controller;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.service.GenericResponseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unical.enterprise.jokibackend.Data.Dto.CartDto;
import unical.enterprise.jokibackend.Data.Dto.UserDto;
import unical.enterprise.jokibackend.Data.Services.Interfaces.CartService;
import unical.enterprise.jokibackend.Data.Services.Interfaces.GameService;
import unical.enterprise.jokibackend.Data.Services.Interfaces.UserService;

import javax.ws.rs.Produces;
import java.util.UUID;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/carts")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;
    private final UserService userService;
    private final GameService gameService;
    private final GenericResponseService responseBuilder;

    Logger logger = Logger.getLogger(CartController.class.getName());

    @GetMapping(value = "/{username}", produces = "application/json")
    public ResponseEntity<String> getCartByUsername(@PathVariable String username){
        try {
            UserDto user = userService.getUserByUsername(username);
            CartDto userCart = cartService.getCartByUserid(user.getId());
            return ResponseEntity.ok(new Gson().toJson(userCart));
        } catch (Exception e) {
            logger.severe(e.getMessage());
            return ResponseEntity.badRequest().body("Error Cart not found");
        }
    }

    @PostMapping("/{username}")
    @Produces("application/json")
    public ResponseEntity<String> updateCart(@PathVariable String username, @RequestBody String gameId){
        try {
            UserDto user = userService.getUserByUsername(username);
            CartDto userCart = cartService.getCartByUserid(user.getId());
            if (!userCart.getGame().contains(gameService.getGameById(UUID.fromString(gameId)))) return ResponseEntity.badRequest().body("Game already in cart");
            userCart.getGame().add(gameService.getGameById(UUID.fromString(gameId)));
            cartService.update(UUID.fromString(userCart.getId()), userCart);
            return ResponseEntity.ok(new Gson().toJson(userCart));

        } catch (Exception e) {
            logger.severe(e.getMessage());
            return ResponseEntity.badRequest().body("Cart Update Error");
        }
    }

    @DeleteMapping("/{username}")
    @Produces("application/json")
    public ResponseEntity<String> deleteGameFromCart(@PathVariable String username, @RequestBody String gameId){
        try {
            UserDto user = userService.getUserByUsername(username);
            CartDto userCart = cartService.getCartByUserid(user.getId());
            if (!userCart.getGame().contains(gameService.getGameById(UUID.fromString(gameId)))) return ResponseEntity.badRequest().body("Game not in cart");

            userCart.getGame().remove(gameService.getGameById(UUID.fromString(gameId)));
            cartService.update(UUID.fromString(userCart.getId()), userCart);
            return ResponseEntity.ok(new Gson().toJson(userCart));

        } catch (Exception e) {
            logger.severe(e.getMessage());
            return ResponseEntity.badRequest().body("Cart Update Error");
        }
    }

    @DeleteMapping("/{username}/clear")
    @Produces("application/json")
    public ResponseEntity<String> clearCart(@PathVariable String username){
        try {
            UserDto user = userService.getUserByUsername(username);
            CartDto userCart = cartService.getCartByUserid(user.getId());
            userCart.getGame().clear();
            cartService.update(UUID.fromString(userCart.getId()), userCart);
            return ResponseEntity.ok(new Gson().toJson(userCart));

        } catch (Exception e) {
            logger.severe(e.getMessage());
            return ResponseEntity.badRequest().body("Cart clearing Error");
        }
    }


}
