package unical.enterprise.jokibackend.Data.Services.Interfaces;

import unical.enterprise.jokibackend.Data.Dto.GameDto;
import unical.enterprise.jokibackend.Data.Dto.UpdateUserDto;
import unical.enterprise.jokibackend.Data.Dto.UserDto;
import unical.enterprise.jokibackend.Data.Entities.User;

import java.util.Collection;
import java.util.UUID;

public interface UserService {

    void save(User user);

    UserDto getUserById(UUID id);

    UserDto getUserByUsername(String username);

    UserDto getUserByEmail(String email);

    Collection<UserDto> getAllUsers();

    Boolean updateUser(String username, UpdateUserDto userDto);

    void delete(UUID id);

    void deleteByUsername(String username);

    Collection<GameDto> getGamesByUsername(String username);

    User getFriendByUsername(String first, String second);

    boolean addGameToUserLibrary(String username, UUID gameId);

    boolean removeGameFromUserLibrary(String username, UUID gameId);

    Collection<GameDto> getUserCart(String username);

    boolean addGameToUserCart(String username, UUID gameId);

    boolean removeGameFromUserCart(String username, UUID gameId);

    void clearUserCart(String username);

    boolean checkIfFriend(String other);

    void deleteGameFromUsers(UUID id);
}
