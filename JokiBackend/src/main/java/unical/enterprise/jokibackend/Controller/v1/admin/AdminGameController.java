package unical.enterprise.jokibackend.Controller.v1.admin;

import com.google.gson.Gson;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
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

    @PostMapping(value = "", produces = "application/json")
    public ResponseEntity<String> addGame(@Valid @RequestBody GameDto gameDto) {
        GameDto result = gameService.save(gameDto);
        if(result != null) {
            return ResponseEntity.ok(new Gson().toJson(result));
        } else {
            throw new RuntimeException(new Gson().toJson("Game save failed"));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateGame(@PathVariable UUID id,@Valid @RequestBody GameDto gameDto) {
        if(gameService.update(id, gameDto) != null) {
            return ResponseEntity.ok(new Gson().toJson("Successfully updated game"));
        } else {
            throw new RuntimeException("Game update failed");
        }
    }

    @PutMapping("/{id}/{index}")
    public ResponseEntity<String> uploadPhoto(@RequestParam("file") MultipartFile file, @PathVariable UUID id, @PathVariable int index) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        if(index < 0 || index > 3) {
            throw new IllegalArgumentException("Index out of bounds");
        }

        return gameService.updatePhoto(gameService.getGameById(id), file, index)
                ? ResponseEntity.ok(new Gson().toJson("Photo uploaded successfully"))
                : ResponseEntity.badRequest().body(new Gson().toJson("Photo upload failed"));
    }

    @DeleteMapping("/{id}/{index}")
    public ResponseEntity<String> deletePhoto(@PathVariable UUID id, @PathVariable int index) {
        if(index < 0 || index > 3) {
            throw new IllegalArgumentException("Index out of bounds");
        }

        return gameService.deletePhoto(gameService.getGameById(id), index)
                ? ResponseEntity.ok(new Gson().toJson("Photo deleted successfully"))
                : ResponseEntity.badRequest().body(new Gson().toJson("Photo deleted failed"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteGame(@PathVariable UUID id) {
        return gameService.delete(id)
                ? ResponseEntity.ok(new Gson().toJson("Game deleted successfully"))
                : ResponseEntity.badRequest().body(new Gson().toJson("Game delete failed"));
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

