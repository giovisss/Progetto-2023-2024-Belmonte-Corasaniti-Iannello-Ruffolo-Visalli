package unical.enterprise.jokibackend.Controller;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import unical.enterprise.jokibackend.Data.Dto.GameDto;
import unical.enterprise.jokibackend.Data.Entities.User;
import unical.enterprise.jokibackend.Data.Services.Interfaces.GameService;
import unical.enterprise.jokibackend.Data.Services.Interfaces.UserService;

import java.util.Collection;
import java.util.UUID;
import java.util.logging.Logger;

@RestController
@RequestMapping("/api/games")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class GameController {

   private final GameService gameService;
   private final UserService userService;

   Logger logger = Logger.getLogger(GameController.class.getName());

    @GetMapping(value = "", produces = "application/json")
    public ResponseEntity<String> getGamesList(){
        try {
            Collection<GameDto> games = gameService.findAll();
            return ResponseEntity.ok(new Gson().toJson(games));
        }
        catch (Exception e) {
            logger.warning(e.getMessage());
            return ResponseEntity.badRequest().body("An error occurred");
        }
    }






    @GetMapping(value = "/start", produces = "application/json")
    public ResponseEntity<String> start(){
        try {
            for (int i = 0; i < 10; i++) {
                GameDto game = new GameDto();
                game.setTitle("Game " + i);
                gameService.save(game);
            }

            for (int i = 0; i < 3; i++) {
                User user = new User();
                user.setId(UUID.randomUUID());
                user.setUsername("User" + i);
                user.setEmail("user" + i + "@gmail.com");
                userService.save(user);
            }


            return ResponseEntity.ok("Server setup successfully");
        }
        catch (Exception e) {
            logger.warning(e.getMessage());
            return ResponseEntity.badRequest().body("An error occurred");
        }
    }


    @GetMapping("friends/{username}")
    @PreAuthorize("@userPermissionEvaluator.isFriend(authentication, #username)")
    public ResponseEntity<String> getFriends(@PathVariable String username) {
        return ResponseEntity.ok("You are friends");
    }











   @GetMapping(value = "/{id}", produces = "application/json")
   public ResponseEntity<String> getGameById(@PathVariable UUID id) {
       try {
           GameDto game = gameService.getGameById(id);
           if (game != null) {
               return ResponseEntity.ok(new Gson().toJson(game));
           } else {
               throw new Exception("Game not found");
           }
       }
       catch (Exception e) {
           logger.warning(e.getMessage());
           return ResponseEntity.badRequest().body("An error occurred");
       }
   }

   @PostMapping("")
   @PreAuthorize("hasRole('client_admin')")
   public ResponseEntity<String> addGame(@RequestBody GameDto gameDto) {
       try {
           if(gameService.save(gameDto) != null) {
               return ResponseEntity.ok("Successfully added game");
           } else {
               throw new Exception("Game save returned null");
           }
       }
       catch (Exception e) {
           logger.warning(e.getMessage());
           return ResponseEntity.badRequest().body("An error occurred");
       }
   }

   @PutMapping("/{id}")
   @PreAuthorize("hasRole('client_admin')")
   public ResponseEntity<String> updateGame(@PathVariable UUID id, @RequestBody GameDto gameDto) {
       try {
           if(gameService.update(id, gameDto) != null) {
               return ResponseEntity.ok("Successfully updated game");
           } else {
               throw new Exception("Game update returned null");
           }
       }
       catch (Exception e) {
           logger.warning(e.getMessage());
           return ResponseEntity.badRequest().body("An error occurred");
       }
   }

   @DeleteMapping("/{id}")
   @PreAuthorize("hasRole('client_admin')")
   public ResponseEntity<String> deleteGame(@PathVariable UUID id) {
       try {
           gameService.delete(id);
           return ResponseEntity.ok("Game deleted");
       }
       catch (Exception e) {
           logger.warning(e.getMessage());
           return ResponseEntity.badRequest().body("An error occurred");
       }
   }

   // @GetMapping("/by-title/{title}")
   // @PreAuthorize("hasRole('client_user')")
   // @Produces("application/json")
   // public GameDto getGameByTitle(@RequestParam String title) {
   //     return gameService.getGameByTitle(title);
   // }

   // @GetMapping("/by-id/{id}")
   // @PreAuthorize("hasRole('client_user')")
   // @Produces("application/json")
   // public GameDto getGameById(@RequestParam UUID id) {
   //     return gameService.getGameById(id);
   // }

   // @GetMapping("/by-author/{author}")
   // @PreAuthorize("hasRole('client_user')")
   // @Produces("application/json")
   // public GameDto getGameByAuthor(@RequestParam String author) {
   //     return gameService.getGameByAuthor(author);
   // }

   // @GetMapping("/by-genre/{genre}")
   // @PreAuthorize("hasRole('client_user')")
   // @Produces("application/json")
   // public GameDto getGameByGenre(@RequestParam String genre) {
   //     return gameService.getGameByGenre(genre);
   // }
}