package unical.enterprise.jokibackend.Data.Services;

import java.util.Collection;
import java.util.UUID;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import unical.enterprise.jokibackend.Data.Dao.GameDao;
import unical.enterprise.jokibackend.Data.Entities.GameDto;
import unical.enterprise.jokibackend.Data.Services.Interfaces.GameService;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService{

   private final GameDao gameDao;

   private final ModelMapper modelMapper;

   @Override
   public unical.enterprise.jokibackend.Data.Dto.GameDto getGameById(UUID id) {
       GameDto game = gameDao.findById(id).orElse(null);
       if (game == null) {
           return null;
       }
       return modelMapper.map(game, unical.enterprise.jokibackend.Data.Dto.GameDto.class);
   }
   

//    @Override
//    public GameDto getGameByTitle(String title) {
//        Game game = gameDao.findGameByTitle(title).orElse(null);
//        return modelMapper.map(game, GameDto.class);
//    }

//    @Override
//    public GameDto getGameByDeveloper(String developer) {
//        Game game = gameDao.findGameByDeveloper(developer).orElse(null);
//        return modelMapper.map(game, GameDto.class);
//    }

//    @Override
//    public GameDto getGameByGenre(String genre) {
//        Game game = gameDao.findGameByGenre(genre).orElse(null);
//        return modelMapper.map(game, GameDto.class);
//    }

   @Override
   public Collection<unical.enterprise.jokibackend.Data.Dto.GameDto> findAll() {
       return gameDao.findAll()
               .stream().map(game -> modelMapper
               .map(game, unical.enterprise.jokibackend.Data.Dto.GameDto.class))
               .toList();
   }

   @Override
   @Transactional
   public unical.enterprise.jokibackend.Data.Dto.GameDto save(unical.enterprise.jokibackend.Data.Dto.GameDto gameDto) {
       GameDto game = modelMapper.map(gameDto, GameDto.class);
       gameDao.save(game);
       return modelMapper.map(game, unical.enterprise.jokibackend.Data.Dto.GameDto.class);
   }

    @Override
    @Transactional
    public unical.enterprise.jokibackend.Data.Dto.GameDto update(UUID id, unical.enterprise.jokibackend.Data.Dto.GameDto gameDto) {
        GameDto game = gameDao.findById(id).orElse(null);
        if (game == null) {
            return null;
        }
        ModelMapper modelMapperUpdater = new ModelMapper();
        modelMapperUpdater.typeMap(unical.enterprise.jokibackend.Data.Dto.GameDto.class, GameDto.class).addMappings(mapper -> {
            mapper.skip(GameDto::setId);
        });
        modelMapperUpdater.map(gameDto, game);
        gameDao.save(game);
        return modelMapperUpdater.map(game, unical.enterprise.jokibackend.Data.Dto.GameDto.class);
    }

   @Override
   @Transactional
   public void delete(UUID id) {
       gameDao.deleteById(id);
   }
}
