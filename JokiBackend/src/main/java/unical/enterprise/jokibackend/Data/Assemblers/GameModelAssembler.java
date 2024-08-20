package unical.enterprise.jokibackend.Data.Assemblers;

import org.springframework.hateoas.Link;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;
import unical.enterprise.jokibackend.Controller.v1.GameController;
import unical.enterprise.jokibackend.Controller.v1.UserController;
import unical.enterprise.jokibackend.Data.Dto.GameDto;
import unical.enterprise.jokibackend.Data.Models.GameModel;

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
        // Aggiungi il link per aggiungere il gioco al carrello
        Link addToCartLink = linkTo(methodOn(UserController.class).addGameToCart(gameDto.getId())).withRel("addToCart");

        gameModel.add(addToCartLink);
        gameModel.add(selfLink);

        return gameModel;
    }
}