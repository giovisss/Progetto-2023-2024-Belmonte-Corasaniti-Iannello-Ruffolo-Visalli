package unical.enterprise.jokibackend.Data.Services.Interfaces;

import org.springframework.web.multipart.MultipartFile;
import unical.enterprise.jokibackend.Data.Dto.GameDto;

import java.util.Collection;
import java.util.UUID;

public interface GameService {
    GameDto getGameById(UUID id);

    Collection<GameDto> findAll();
    GameDto save(GameDto gameDto);
    boolean delete(UUID id);
    GameDto update(UUID id, GameDto gameDto);
    boolean updatePhoto(GameDto gameDto, MultipartFile photo, int index);
    boolean deletePhoto(GameDto gameDto, int index);
}
