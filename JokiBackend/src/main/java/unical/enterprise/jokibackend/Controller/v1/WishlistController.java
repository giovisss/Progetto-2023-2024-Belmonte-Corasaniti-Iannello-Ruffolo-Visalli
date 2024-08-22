package unical.enterprise.jokibackend.Controller.v1;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unical.enterprise.jokibackend.Data.Dto.WishlistDto;
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
        wishlistService.deleteById(wishlistService.getByWishlistName(wishlistName).getId());
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

    // TODO: IMPLEMENTARE LOGICA AMICIZIA

    @GetMapping(value = "/other/{username}", produces = "application/json")
    public ResponseEntity<String> getWishlistByUsername(@PathVariable String username){
        Collection<WishlistDto> out=wishlistService.getOthersWishlists(username);
//        if(out.size()>0) System.out.println(out.iterator().next().toString());
//        return ResponseEntity.ok("vediamo");


//        System.out.println("vjdsrbvghjrfedbvjhdbnvknmfvblhjsdfbvbdfvnkdfnvkndfbslkjdbsklbj");
//        out.forEach(System.out::println);
//        Gson g = new Gson();
//        System.out.println("vjdsrbvghjrfedbvjhdbnvknmfvblhjsdfbvbdfvnkdfnvkndfbslkjdbsklbj");
//        var tmp = g.toJson(out.toArray()[0]);
//        System.out.println(tmp);
//        System.out.println("vjdsrbvghjrfedbvjhdbnvknmfvblhjsdfbvbdfvnkdfnvkndfbslkjdbsklbj");
//        System.out.println(covert(out));

//        var u=new UserDto(UUID.fromString("ab049c2a-d9f1-44c7-a64c-579d8a72f434"),"aaa","aaa","aaa","aaa", new Date(),null,null,null);


//        var u = userService.getUserByUsername("aaa");
//        System.out.println(new Gson().toJson(u));
//        System.out.println(u);
//
//        String cazzi = out.toArray()[0].toString();
////        cazzi=cazzi.substring(12,cazzi.length()-1);
//        System.out.println(new Gson().toJson(cazzi));
//        System.out.println(cazzi);


        if(out.isEmpty()) return ResponseEntity.notFound().build();
        else return ResponseEntity.ok(CollectionToJson.covert(out));
    }
}
