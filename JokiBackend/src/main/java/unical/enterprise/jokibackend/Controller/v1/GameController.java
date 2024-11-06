package unical.enterprise.jokibackend.Controller.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unical.enterprise.jokibackend.Data.Assemblers.GameModelAssembler;
import unical.enterprise.jokibackend.Data.Assemblers.Model;
import unical.enterprise.jokibackend.Data.Dto.GameDto;
import unical.enterprise.jokibackend.Data.Services.Interfaces.GameService;

import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/games")
@CrossOrigin(origins = "*")
@RequiredArgsConstructor
public class GameController {

    private final GameService gameService;
    private final GameModelAssembler gameModelAssembler;

    @GetMapping(value = "", produces = "application/json")
    public ResponseEntity<CollectionModel<Model<GameDto>>> getGamesList() {
        Collection<GameDto> games = gameService.findAll();
        Collection<Model<GameDto>> gameModels = games.stream()
                .map(gameModelAssembler::toModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(gameModels));
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<EntityModel<Model<GameDto>>> getGameById(@PathVariable UUID id) {
        GameDto game = gameService.getGameById(id);
        if (game != null) {
            Model<GameDto> gameModel = gameModelAssembler.toModel(game);
            return ResponseEntity.ok(EntityModel.of(gameModel));
        } else {
            return ResponseEntity.notFound().build();
        }
    }


}
