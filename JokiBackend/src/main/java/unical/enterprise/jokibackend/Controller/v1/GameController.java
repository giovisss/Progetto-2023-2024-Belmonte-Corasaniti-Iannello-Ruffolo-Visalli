package unical.enterprise.jokibackend.Controller.v1;

import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import unical.enterprise.jokibackend.Data.Assemblers.GameModelAssembler;
import unical.enterprise.jokibackend.Data.Dto.GameDto;
import unical.enterprise.jokibackend.Data.Models.GameModel;
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
    public ResponseEntity<CollectionModel<GameModel>> getGamesList() {
        Collection<GameDto> games = gameService.findAll();
        Collection<GameModel> gameModels = games.stream()
                .map(gameModelAssembler::toModel)
                .collect(Collectors.toList());
        return ResponseEntity.ok(CollectionModel.of(gameModels));
    }

    @GetMapping(value = "/{id}", produces = "application/json")
    public ResponseEntity<EntityModel<GameModel>> getGameById(@PathVariable UUID id) {
        GameDto game = gameService.getGameById(id);
        if (game != null) {
            GameModel gameModel = gameModelAssembler.toModel(game);
            return ResponseEntity.ok(EntityModel.of(gameModel));
        } else {
            return ResponseEntity.notFound().build();
        }
    }




}


//package unical.enterprise.jokibackend.Controller.v1;
//
//import com.google.gson.Gson;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//import unical.enterprise.jokibackend.Data.Dto.GameDto;
//import unical.enterprise.jokibackend.Data.Services.Interfaces.GameService;
//
//import java.util.Collection;
//import java.util.UUID;
//
//@RestController
//@RequestMapping("/api/v1/games")
//@CrossOrigin(origins = "*")
//@RequiredArgsConstructor
//public class GameController {
//
//    private final GameService gameService;
//
//    @GetMapping(value = "", produces = "application/json")
//    public ResponseEntity<String> getGamesList(){
//        Collection<GameDto> games = gameService.findAll();
//        return ResponseEntity.ok(new Gson().toJson(games));
//    }
//
//   @GetMapping(value = "/{id}", produces = "application/json")
//   public ResponseEntity<String> getGameById(@PathVariable UUID id) {
//       GameDto game = gameService.getGameById(id);
//       if (game != null) {
//           return ResponseEntity.ok(new Gson().toJson(game));
//       } else {
//           return ResponseEntity.notFound().build();
//       }
//   }
//}
//
//
//
//
//
//
//
//
//   // @GetMapping("/by-title/{title}")
//   // @PreAuthorize("hasRole('client_user')")
//   // @Produces("application/json")
//   // public GameDto getGameByTitle(@RequestParam String title) {
//   //     return gameService.getGameByTitle(title);
//   // }
//
//   // @GetMapping("/by-id/{id}")
//   // @PreAuthorize("hasRole('client_user')")
//   // @Produces("application/json")
//   // public GameDto getGameById(@RequestParam UUID id) {
//   //     return gameService.getGameById(id);
//   // }
//
//   // @GetMapping("/by-author/{author}")
//   // @PreAuthorize("hasRole('client_user')")
//   // @Produces("application/json")
//   // public GameDto getGameByAuthor(@RequestParam String author) {
//   //     return gameService.getGameByAuthor(author);
//   // }
//
//   // @GetMapping("/by-genre/{genre}")
//   // @PreAuthorize("hasRole('client_user')")
//   // @Produces("application/json")
//   // public GameDto getGameByGenre(@RequestParam String genre) {
//   //     return gameService.getGameByGenre(genre);
//   // }
////    @GetMapping(value = "/start", produces = "application/json")
////    public ResponseEntity<String> start(){
////        for (int i = 0; i < 10; i++) {
////            GameDto game = new GameDto();
////            game.setTitle("Game " + i);
////            gameService.save(game);
////        }
////
////        for (int i = 0; i < 3; i++) {
////            User user = new User();
////            user.setId(UUID.randomUUID());
////            user.setUsername("User" + i);
////            user.setEmail("user" + i + "@gmail.com");
////            userService.save(user);
////        }
////
////
////            return ResponseEntity.ok("Server setup successfully");
////    }
////
////
////    @GetMapping("friends/{username}")
////    @PreAuthorize("@userPermissionEvaluator.isFriend(authentication, #username)")
////    public ResponseEntity<String> getFriends(@PathVariable String username) {
////        return ResponseEntity.ok("You are friends");
////    }
////
////    @PostMapping("tmp")
////    @PreAuthorize("hasRole('client_user') or hasRole('client_admin')")
////    public ResponseEntity<String> tmp() {
////        // return ResponseEntity.ok(UserContextHolder.getContext().toString());
////        return ResponseEntity.ok(SecurityContextHolder.getContext().getAuthentication().getName());
////    }
