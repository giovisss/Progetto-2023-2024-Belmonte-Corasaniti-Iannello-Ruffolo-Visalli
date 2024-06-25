package unical.enterprise.jokibackend.Controller;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unical.enterprise.jokibackend.Data.Dto.GameDto;
import unical.enterprise.jokibackend.Data.Services.Interfaces.WishlistService;

import java.util.logging.Logger;

@RestController
@RequestMapping("/api/wishlist")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor

public class WishlistController {

    private final WishlistService wishlistService;

    Logger logger = Logger.getLogger(UserController.class.getName());

    @GetMapping(value = "/{username}", produces = "application/json")
    public ResponseEntity<String> getWishlist(@PathVariable String username){
        try {
            Gson gson = new Gson();
            var out = wishlistService.getByUserUsername(username);
            return ResponseEntity.ok(gson.toJson(out));
        }
        catch (Exception e) {
            logger.warning(e.getMessage());
            return ResponseEntity.badRequest().body("An error occurred");
        }
    }

    @PostMapping(value = "/add", produces = "application/json")
    public ResponseEntity<String> addWishlist(@RequestBody String wishlistName){
        try {
            wishlistService.addWishlist(wishlistName);
            return ResponseEntity.ok("Wishlist added");
        }
        catch (Exception e) {
            logger.warning(e.getMessage());
            return ResponseEntity.badRequest().body("An error occurred");
        }
    }

    @PostMapping(value = "/{wishlistName}", produces = "application/json")
    public ResponseEntity<String> addGameToWishlist(@RequestBody GameDto gameDto, @PathVariable String wishlistName){
        try {
            wishlistService.addGameToWishlist(gameDto, wishlistName);
            return ResponseEntity.ok("Game added to wishlist");
        }
        catch (Exception e) {
            logger.warning(e.getMessage());
            return ResponseEntity.badRequest().body("An error occurred");
        }
    }
}
