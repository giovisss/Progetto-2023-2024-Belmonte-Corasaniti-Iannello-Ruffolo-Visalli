package unical.enterprise.jokibackend.Data.Models;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.hateoas.RepresentationModel;
import unical.enterprise.jokibackend.Data.Dto.AdminDto;
import unical.enterprise.jokibackend.Data.Dto.GameDto;

import java.util.Date;
import java.util.UUID;

@Getter
@RequiredArgsConstructor
public class GameModel extends RepresentationModel<GameModel> {
    private final GameDto gameDto;
}
