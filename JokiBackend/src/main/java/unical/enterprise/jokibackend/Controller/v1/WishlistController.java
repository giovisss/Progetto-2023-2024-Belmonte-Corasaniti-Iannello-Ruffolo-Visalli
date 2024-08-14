package unical.enterprise.jokibackend.Controller.v1;

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

    Logger logger = Logger.getLogger(WishlistController.class.getName());

    @GetMapping(value = "/{username}", produces = "application/json")
    public ResponseEntity<String> getWishlist(@PathVariable String username){
        Gson gson = new Gson();
        var out = wishlistService.getByUserUsername(username);
        return ResponseEntity.ok(gson.toJson(out));
    }

    @PostMapping(value = "/add", produces = "application/json")
    public ResponseEntity<String> addWishlist(@RequestBody String wishlistName){
        wishlistService.addWishlist(wishlistName);
        return ResponseEntity.ok("Wishlist added");
    }

    @PostMapping(value = "/{wishlistName}", produces = "application/json")
    public ResponseEntity<String> addGameToWishlist(@RequestBody GameDto gameDto, @PathVariable String wishlistName){
        wishlistService.addGameToWishlist(gameDto, wishlistName);
        return ResponseEntity.ok("Game added to wishlist");
    }
}
