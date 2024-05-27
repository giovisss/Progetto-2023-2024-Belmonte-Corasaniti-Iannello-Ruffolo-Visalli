package unical.enterprise.jokibackend.Data.Services;

import java.util.UUID;

import unical.enterprise.jokibackend.Dto.GameDto;

public interface GameService {

    public GameDto getGameById(UUID id);

    public GameDto getGameByTitle(String title);

    public GameDto getGameByAuthor(String author);

    public GameDto getGameByGenre(String genre);

    public GameDto save(GameDto gameDto);

    public void delete(UUID id);
}
