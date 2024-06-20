package unical.enterprise.jokibackend.Controller;

import java.util.Collection;
import java.util.UUID;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import unical.enterprise.jokibackend.Data.Dto.GameDto;
import unical.enterprise.jokibackend.Data.Services.GameService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/api/games")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class GamesController {
    private final GameService gameService;

    @GetMapping("/all")
    @PreAuthorize("hasRole('client_user')")
    public Collection<GameDto> getAllGames(@RequestParam String param) {
        return gameService.findAll();
    }

    @GetMapping("/by-title/{title}")
    @PreAuthorize("hasRole('client_user')")
    public GameDto getGameByTitle(@RequestParam String title) {
        return gameService.getGameByTitle(title);
    }

    @GetMapping("/by-id/{id}")
    @PreAuthorize("hasRole('client_user')")
    public GameDto getGameById(@RequestParam UUID id) {
        return gameService.getGameById(id);
    }

    @GetMapping("/by-author/{author}")
    @PreAuthorize("hasRole('client_user')")
    public GameDto getGameByAuthor(@RequestParam String author) {
        return gameService.getGameByAuthor(author);
    }

    @GetMapping("/by-genre/{genre}")
    @PreAuthorize("hasRole('client_user')")
    public GameDto getGameByGenre(@RequestParam String genre) {
        return gameService.getGameByGenre(genre);
    }

    @PostMapping("/add")
    @PreAuthorize("hasRole('admin_user')")
    public GameDto addGame(@RequestBody GameDto gameDto) {
        return gameService.save(gameDto);
    }
    
    @PostMapping("/delete/{id}")
    @PreAuthorize("hasRole('admin_user')")
    public void deleteGame(@RequestParam UUID id) {
        gameService.delete(id);
    }
}