package unical.enterprise.jokibackend.Data.Services.Interfaces;

import java.util.Collection;
import java.util.UUID;

import unical.enterprise.jokibackend.Data.Dto.GameDto;

public interface GameService {
    public GameDto getGameById(UUID id);
    // public GameDto getGameByTitle(String title);
    // public GameDto getGameByDeveloper(String developer);
    // public GameDto getGameByGenre(String genre);
    public Collection<GameDto> findAll();
    public GameDto save(GameDto gameDto);
    public void delete(UUID id);
    public GameDto update(UUID id, GameDto gameDto);
}
