package unical.enterprise.jokibackend.Controller.v1;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unical.enterprise.jokibackend.Data.Dto.WishlistDto;
import unical.enterprise.jokibackend.Data.Entities.Wishlist;
import unical.enterprise.jokibackend.Data.Services.Interfaces.GameService;
import unical.enterprise.jokibackend.Data.Services.Interfaces.UserService;
import unical.enterprise.jokibackend.Data.Services.Interfaces.WishlistService;
import unical.enterprise.jokibackend.Utility.CollectionToJson;
import unical.enterprise.jokibackend.Utility.CustomContextManager.UserContextHolder;

import java.util.Collection;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/wishlist")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class WishlistController {
    private final WishlistService wishlistService;
    private final UserService userService;
    private final GameService gameService;
    private final ModelMapper modelMapper;

    @GetMapping(value = "", produces = "application/json")
    public ResponseEntity<String> getWishlist(){
        var out = wishlistService.getByUserUsername(UserContextHolder.getContext().getPreferredUsername());
        return ResponseEntity.ok(new Gson().toJson(out));
    }

    @PostMapping(value = "", produces = "application/json")
    public ResponseEntity<String> addWishlist(@RequestBody String wishlistName){
        wishlistService.addWishlist(wishlistName);
        return ResponseEntity.ok("Wishlist added");
    }

    @GetMapping(value = "/{wishlistName}", produces = "application/json")
    public ResponseEntity<String> getWishlist(@PathVariable String wishlistName){
        var out = wishlistService.getByWishlistName(wishlistName);
        return ResponseEntity.ok(new Gson().toJson(out));
    }

    @DeleteMapping(value = "/{wishlistName}", produces = "application/json")
    public ResponseEntity<String> deleteWishlist(@PathVariable String wishlistName){
        wishlistService.delete(modelMapper.map(wishlistService.getByWishlistName(wishlistName), Wishlist.class));
        return ResponseEntity.ok("Wishlist deleted");
    }

    @PostMapping(value = "/{wishlistName}/{gameId}", produces = "application/json")
    public ResponseEntity<String> addGameToWishlist(@PathVariable UUID gameId, @PathVariable String wishlistName){
        boolean added = wishlistService.addGameToWishlist(gameService.getGameById(gameId), wishlistName);

        if (added) return ResponseEntity.ok("Game added to wishlist");
        else return ResponseEntity.badRequest().body("Game already in wishlist");
    }

    @DeleteMapping(value = "/{wishlistName}/{gameId}", produces = "application/json")
    public ResponseEntity<String> removeGameFromWishlist(@PathVariable UUID gameId, @PathVariable String wishlistName){
        wishlistService.removeGameFromWishlist(gameService.getGameById(gameId), wishlistName);
        return ResponseEntity.ok("Game removed from wishlist");
    }

    @GetMapping(value = "/other/{username}", produces = "application/json")
    public ResponseEntity<String> getOtherWishlistsByUsername(@PathVariable String username){
        Collection<WishlistDto> out=wishlistService.getOtherWishlists(username);

        if(out.isEmpty()) return ResponseEntity.notFound().build();
        else return ResponseEntity.ok(CollectionToJson.covert(out));
    }

    // TODO: testare dopo hateoas e vedere se va in overflow
    @GetMapping(value = "/other/{username}/{wishlistName}", produces = "application/json")
    public ResponseEntity<String> getOtherWishlist(@PathVariable String username, @PathVariable String wishlistName){
        var out = wishlistService.getOtherWishlistByWishlistName(username, wishlistName);

        if(out == null) return ResponseEntity.notFound().build();
        else return ResponseEntity.ok(new Gson().toJson(out));
    }
}
