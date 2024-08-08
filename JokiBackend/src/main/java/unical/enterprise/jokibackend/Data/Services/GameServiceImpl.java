package unical.enterprise.jokibackend.Data.Services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import unical.enterprise.jokibackend.Data.Dao.GameDao;
import unical.enterprise.jokibackend.Data.Dto.GameDto;
import unical.enterprise.jokibackend.Data.Entities.Game;
import unical.enterprise.jokibackend.Data.Services.Interfaces.GameService;

import java.util.Collection;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService{

   private final GameDao gameDao;

   private final ModelMapper modelMapper;

   @Override
   public GameDto getGameById(UUID id) {
       Game game = gameDao.findById(id).orElse(null);
       if (game == null) {
           return null;
       }
       return modelMapper.map(game, GameDto.class);
   }

   @Override
   @Transactional
   public Collection<unical.enterprise.jokibackend.Data.Dto.GameDto> findAll() {
       return gameDao.findAll()
               .stream().map(game -> modelMapper
               .map(game, unical.enterprise.jokibackend.Data.Dto.GameDto.class))
               .toList();
   }

   @Override
   @Transactional
   public GameDto save(GameDto gameDto) {
       Game game = modelMapper.map(gameDto, Game.class);
       gameDao.save(game);
       return modelMapper.map(game, unical.enterprise.jokibackend.Data.Dto.GameDto.class);
   }

    @Override
    @Transactional
    public unical.enterprise.jokibackend.Data.Dto.GameDto update(UUID id, unical.enterprise.jokibackend.Data.Dto.GameDto gameDto) {
        Game game = gameDao.findById(id).orElse(null);
        if (game == null) {
            return null;
        }
        ModelMapper modelMapperUpdater = new ModelMapper();
        modelMapperUpdater.typeMap(unical.enterprise.jokibackend.Data.Dto.GameDto.class, GameDto.class).addMappings(mapper -> {
            mapper.skip(GameDto::setId);
            mapper.skip(GameDto::setAdmin);
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
