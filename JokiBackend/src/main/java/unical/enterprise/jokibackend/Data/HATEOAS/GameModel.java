package unical.enterprise.jokibackend.Data.HATEOAS;

import org.springframework.hateoas.RepresentationModel;
import unical.enterprise.jokibackend.Data.Dto.GameDto;

public class GameModel extends RepresentationModel<GameModel> {
    private final GameDto gameDto;

    public GameModel(GameDto gameDto) {
        this.gameDto = gameDto;
    }

    public GameDto getGameDto() {
        return gameDto;
    }
}