package unical.enterprise.jokibackend.Data.Services;

import java.util.UUID;

import unical.enterprise.jokibackend.Dto.GamesDto;

public interface GameService {

    public GamesDto getGameById(UUID id);

    public GamesDto getGameByTitle(String title);

    public GamesDto getGameByAuthor(String author);

    public GamesDto getGameByGenre(String genre);

    public GamesDto save(GamesDto gameDto);

    public void delete(UUID id);
}
