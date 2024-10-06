package unical.enterprise.jokibackend.Data.Services;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import unical.enterprise.jokibackend.Data.Dao.GameDao;
import unical.enterprise.jokibackend.Data.Dto.GameDto;
import unical.enterprise.jokibackend.Data.Entities.Game;
import unical.enterprise.jokibackend.Data.Services.Interfaces.AdminService;
import unical.enterprise.jokibackend.Data.Services.Interfaces.GameService;
import unical.enterprise.jokibackend.Utility.CustomContextManager.UserContextHolder;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class GameServiceImpl implements GameService{
    private final AdminService adminService;
    private final GameDao gameDao;
    private final ModelMapper modelMapper;

    private final String folder = "src/main/resources/static/images/";

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
   public Collection<GameDto> findAll() {
       return gameDao.findAll()
               .stream().map(game -> modelMapper
               .map(game, GameDto.class))
               .toList();
   }

   @Override
   @Transactional
   public GameDto save(GameDto gameDto) {
       gameDto.setAdmin(adminService.getByUsername(UserContextHolder.getContext().getPreferredUsername()));
       gameDto.setImagePath(gameDto.getTitle().toLowerCase().replace(" ", "-") + ".jpg");
       Game game = modelMapper.map(gameDto, Game.class);
       gameDao.save(game);
       return modelMapper.map(game, GameDto.class);
   }

    @Override
    @Transactional
    public GameDto update(UUID id, GameDto gameDto) {
        Game game = gameDao.findById(id).orElse(null);
        if (game == null) {
            return null;
        }

        game.setDescription(gameDto.getDescription());
        game.setDeveloper(gameDto.getDeveloper());
        game.setGenre(gameDto.getGenre());
        game.setPrice(gameDto.getPrice());
        game.setPublisher(gameDto.getPublisher());
        game.setReleaseDate(gameDto.getReleaseDate());
        game.setStock(gameDto.getStock());
        game.setTitle(gameDto.getTitle());

        gameDao.save(game);
        return modelMapper.map(game, GameDto.class);
    }

    @Override
    public boolean updatePhoto(GameDto gameDto, MultipartFile photo, int index) {
        try {
            Path path = Paths.get(folder + gameDto.getImagePath().replace(".jpg", "_") + index + ".jpg");

            // Salva il file nel percorso specificato
//            Files.write(path, photo.getBytes());
            photo.transferTo(path);
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    @Override
    public boolean deletePhoto(GameDto gameDto, int index) {
        try {
            Path path = Paths.get(folder + gameDto.getImagePath().replace(".jpg", "_") + index + ".jpg");

            return path.toFile().delete();
        } catch (Exception e) {
            return false;
        }
    }

    @Override
   @Transactional
   public boolean delete(UUID id) {
        Game game = gameDao.findById(id).orElse(null);

        if (game == null) return false;

        GameDto gameDto = modelMapper.map(game, GameDto.class);
        for (int i = 1; i <= 3; i++) {
            deletePhoto(gameDto, i);
        }

        gameDao.deleteById(id);
        return true;
    }
}
