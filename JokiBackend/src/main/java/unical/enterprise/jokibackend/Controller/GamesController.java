package unical.enterprise.jokibackend.Controller;

import java.util.Collection;
import java.util.UUID;
import java.util.logging.Logger;

import javax.ws.rs.Produces;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import lombok.RequiredArgsConstructor;
import unical.enterprise.jokibackend.Data.Dto.GameDto;
import unical.enterprise.jokibackend.Data.Services.GameService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/api/games")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class GamesController {
    
    private final GameService gameService;
    
    Logger logger = Logger.getLogger(UserController.class.getName());

    @GetMapping("")
    @Produces("application/json")
    public ResponseEntity<String> getGamesList(@RequestParam String method, @RequestParam String value){
        try {
            switch(method) {
                case "title":
                    GameDto gameByTitle = gameService.getGameByTitle(value);
                    return ResponseEntity.ok(new Gson().toJson(gameByTitle));
                case "author":
                    GameDto gameByAuthor = gameService.getGameByAuthor(value);
                    return ResponseEntity.ok(new Gson().toJson(gameByAuthor));
                case "genre":
                    GameDto gameByGenre = gameService.getGameByGenre(value);
                    return ResponseEntity.ok(new Gson().toJson(gameByGenre));
                default:
                    Collection<GameDto> games = gameService.findAll();
                    return ResponseEntity.ok(new Gson().toJson(games));
            }
        }
        catch (Exception e) {
            logger.warning(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    @Produces("application/json")
    public GameDto getGameById(@RequestParam UUID id) {
        // sistemare json
        return gameService.getGameById(id);
    }

    @PostMapping("")
    @PreAuthorize("hasRole('client_admin')")
    public GameDto addGame(@RequestBody GameDto gameDto) {
        // sistemare json
        return gameService.save(gameDto);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('client_admin')")
    public GameDto updateGame(@RequestParam UUID id, @RequestBody GameDto gameDto) {
        // TODO: Implement
        return null;
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('client_admin')")
    public void deleteGame(@RequestParam UUID id) {
        // sistemare json
        gameService.delete(id);
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