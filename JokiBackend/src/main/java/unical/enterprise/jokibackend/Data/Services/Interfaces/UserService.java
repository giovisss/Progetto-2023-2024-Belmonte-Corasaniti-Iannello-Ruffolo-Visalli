package unical.enterprise.jokibackend.Data.Services.Interfaces;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import unical.enterprise.jokibackend.Data.Dto.GameDto;
import unical.enterprise.jokibackend.Data.Dto.UpdateUserDto;
import unical.enterprise.jokibackend.Data.Entities.Game;
import unical.enterprise.jokibackend.Data.Entities.User;
import unical.enterprise.jokibackend.Data.Dto.UserDto;

public interface UserService {

    void save(User user);

    UserDto getUserById(UUID id);

    UserDto getUserByUsername(String username);

    UserDto getUserByEmail(String email);

    Collection<UserDto> getAllUsers();

    Boolean updateUser(String username, UpdateUserDto userDto);

    void delete(UUID id);

    void deleteByUsername(String username);

    Collection<Game> getUsernameGames(String username);
}
