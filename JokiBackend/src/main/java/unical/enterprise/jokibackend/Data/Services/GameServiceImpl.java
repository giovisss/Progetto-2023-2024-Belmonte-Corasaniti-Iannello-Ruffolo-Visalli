package unical.enterprise.jokibackend.Data.Services;

import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import unical.enterprise.jokibackend.Data.Dao.GameDao;
import unical.enterprise.jokibackend.Data.Entities.Game;
import unical.enterprise.jokibackend.Dto.GameDto;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService{

    private final GameDao gameDao;

    private final ModelMapper modelMapper;

    public GameServiceImpl(GameDao gameDao, ModelMapper modelMapper) {
        this.gameDao = gameDao;
        this.modelMapper = modelMapper;
    }

    @Override
    public GameDto getGameById(UUID id) {
        Game game = gameDao.findById(id).orElse(null);
        return modelMapper.map(game, GameDto.class);
    }
    
    @Override
    public GameDto getGameByTitle(String title) {
        Game game = gameDao.findGameByTitle(title).orElse(null);
        return modelMapper.map(game, GameDto.class);
    }
    
    @Override
    public GameDto getGameByAuthor(String author) {
        Game game = gameDao.findGameByAuthor(author).orElse(null);
        return modelMapper.map(game, GameDto.class);
    }
    
    @Override
    public GameDto getGameByGenre(String genre) {
        Game game = gameDao.findGameByGenre(genre).orElse(null);
        return modelMapper.map(game, GameDto.class);
    }
    
    @Override
    public GameDto save(GameDto gameDto) {
        Game game = modelMapper.map(gameDto, Game.class);
        gameDao.save(game);
        return modelMapper.map(game, GameDto.class);
    }

    @Override
    public void delete(UUID id) {
        gameDao.deleteById(id);
    }
}
