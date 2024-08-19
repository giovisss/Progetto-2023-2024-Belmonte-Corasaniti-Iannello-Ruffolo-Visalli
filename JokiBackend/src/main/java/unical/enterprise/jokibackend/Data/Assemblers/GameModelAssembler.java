package unical.enterprise.jokibackend.Data.Assemblers;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;
import unical.enterprise.jokibackend.Controller.v1.GameController;
import unical.enterprise.jokibackend.Data.Dto.GameDto;
import unical.enterprise.jokibackend.Data.HATEOAS.GameModel;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class GameModelAssembler extends RepresentationModelAssemblerSupport<GameDto, GameModel> {

    public GameModelAssembler() {
        super(GameController.class, GameModel.class);
    }

    @Override
    public GameModel toModel(GameDto gameDto) {
        GameModel gameModel = new GameModel(gameDto);
        Link selfLink = linkTo(methodOn(GameController.class).getGameById(gameDto.getId())).withSelfRel();
        Link allGamesLink = linkTo(methodOn(GameController.class).getGamesList()).withRel("allGames");
//        Link deleteLink = linkTo(methodOn(GameController.class).deleteGame(gameDto.getId())).withRel("delete");
//        Link updateLink = linkTo(methodOn(GameController.class).updateGame(gameDto.getId(), gameDto)).withRel("update");
        gameModel.add(selfLink, allGamesLink);
        return gameModel;
    }
}