package unical.enterprise.jokibackend.Controller;

import java.util.Collection;
import java.util.UUID;
import java.util.logging.Logger;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import lombok.RequiredArgsConstructor;
import unical.enterprise.jokibackend.Data.Dto.GameDto;
import unical.enterprise.jokibackend.Data.Services.GameService;

@RestController
@RequestMapping("/api/games")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class GameController {
    
    private final GameService gameService;
    
    Logger logger = Logger.getLogger(UserController.class.getName());

    @GetMapping(value = "", produces = "application/json")
    public ResponseEntity<String> getGamesList(@RequestParam(required = false) String method, @RequestParam(required = false) String value){
        try {
            if(method == null) method = "";
            else if(value == null || value.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("value cannot be empty");
            }

            return switch (method) {
                case "title" -> {
                    GameDto gameByTitle = gameService.getGameByTitle(value);
                    yield ResponseEntity.ok(new Gson().toJson(gameByTitle));
                }
                case "author" -> {
                    GameDto gameByAuthor = gameService.getGameByAuthor(value);
                    yield ResponseEntity.ok(new Gson().toJson(gameByAuthor));
                }
                case "genre" -> {
                    GameDto gameByGenre = gameService.getGameByGenre(value);
                    yield ResponseEntity.ok(new Gson().toJson(gameByGenre));
                }
                default -> {
                    Collection<GameDto> games = gameService.findAll();
                    yield ResponseEntity.ok(new Gson().toJson(games));
                }
            };
        }
        catch (Exception e) {
            logger.warning(e.getMessage());
            return ResponseEntity.badRequest().body("An error occurred");
        }
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<String> getGameById(@RequestParam UUID id) {
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
    public ResponseEntity<String> updateGame(@RequestParam UUID id, @RequestBody GameDto gameDto) {
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
    public ResponseEntity<String> deleteGame(@RequestParam UUID id) {
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