package unical.enterprise.jokibackend.Data.Services;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import unical.enterprise.jokibackend.Data.Dao.GameDao;
import unical.enterprise.jokibackend.Data.Entities.Game;
import unical.enterprise.jokibackend.Dto.GamesDto;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService{

    @Autowired
    private final GameDao gameDao;

    private final ModelMapper modelMapper;
    
    @Override
    public GamesDto getGameById(UUID id) {
        Game game = gameDao.findById(id).orElse(null);
        return modelMapper.map(game, GamesDto.class);
    }
    
    @Override
    public GamesDto getGameByTitle(String title) {
        Game game = gameDao.findGameByTitle(title).orElse(null);
        return modelMapper.map(game, GamesDto.class);
    }
    
    @Override
    public GamesDto getGameByAuthor(String author) {
        Game game = gameDao.findGameByAuthor(author).orElse(null);
        return modelMapper.map(game, GamesDto.class);
    }
    
    @Override
    public GamesDto getGameByGenre(String genre) {
        Game game = gameDao.findGameByGenre(genre).orElse(null);
        return modelMapper.map(game, GamesDto.class);
    }
    
    @Override
    public GamesDto save(GamesDto gameDto) {
        Game game = modelMapper.map(gameDto, Game.class);
        gameDao.save(game);
        return modelMapper.map(game, GamesDto.class);
    }

    @Override
    public void delete(UUID id) {
        gameDao.deleteById(id);
    }
}
