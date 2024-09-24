package unical.enterprise.jokibackend.Data.Services.Interfaces;

import unical.enterprise.jokibackend.Data.Dto.GameDto;

import java.util.Collection;
import java.util.UUID;

public interface GameService {
    GameDto getGameById(UUID id);
    // public GameDto getGameByTitle(String title);
    // public GameDto getGameByDeveloper(String developer);
    // public GameDto getGameByGenre(String genre);
    Collection<GameDto> findAll();
    GameDto save(GameDto gameDto);
    void delete(UUID id);
    GameDto update(UUID id, GameDto gameDto);
}
