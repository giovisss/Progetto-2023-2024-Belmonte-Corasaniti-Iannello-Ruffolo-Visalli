package unical.enterprise.jokibackend.Controller.v1.admin;

import com.google.gson.Gson;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unical.enterprise.jokibackend.Data.Dto.GameDto;
import unical.enterprise.jokibackend.Data.Services.Interfaces.AdminService;
import unical.enterprise.jokibackend.Data.Services.Interfaces.GameService;
import unical.enterprise.jokibackend.Utility.CustomContextManager.UserContextHolder;

import java.util.Collection;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/admin/games")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class AdminGameController {
    private final GameService gameService;
    private final AdminService adminService;

    // this endpoint is a duplicate of the used by users
    // it's added here for easier implementation logic
    @GetMapping(value = "", produces = "application/json")
    public ResponseEntity<String> getGamesList(){
        Collection<GameDto> games = gameService.findAll();
        return ResponseEntity.ok(new Gson().toJson(games));
    }

    // this endpoint is a duplicate of the used by users
    // it's added here for easier implementation logic
    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<String> getGameById(@PathVariable UUID id) {
        GameDto game = gameService.getGameById(id);
        if (game != null) {
            return ResponseEntity.ok(new Gson().toJson(game));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @PostMapping("")
    public ResponseEntity<String> addGame(@RequestBody GameDto gameDto) {
        gameDto.setAdmin(adminService.getByUsername(UserContextHolder.getContext().getPreferredUsername()));

        if(gameService.save(gameDto) != null) {
            return ResponseEntity.ok("Successfully added game");
        } else {
            throw new RuntimeException("Game save failed");
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateGame(@PathVariable UUID id, @RequestBody GameDto gameDto) {
        if(gameService.update(id, gameDto) != null) {
            return ResponseEntity.ok("Successfully updated game");
        } else {
            throw new RuntimeException("Game update failed");
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteGame(@PathVariable UUID id) {
        gameService.delete(id);
        return ResponseEntity.ok("Game deleted");
    }

    @GetMapping(value = "/by-admin", produces = "application/json")
    public ResponseEntity<String> getGamesByAdmin() {
        Collection<GameDto> games = adminService.findGamesByAdminUsername(UserContextHolder.getContext().getPreferredUsername()).orElse(null);
        if (games != null) {
            return ResponseEntity.ok(new Gson().toJson(games));
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}

