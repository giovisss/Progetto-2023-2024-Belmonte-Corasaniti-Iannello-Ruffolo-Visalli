package unical.enterprise.jokibackend.Data.Services.Interfaces;

import java.util.Collection;
import java.util.UUID;

import unical.enterprise.jokibackend.Data.Entities.User;
import unical.enterprise.jokibackend.Data.Dto.UserDto;

public interface UserService {

    void save(User user);

    UserDto getUserById(UUID id);

    UserDto getUserByUsername(String username);

    UserDto getUserByEmail(String email);

    Collection<UserDto> getAllUsers();

    UserDto updateUser(String username, UserDto userDto);

    void delete(UUID id);

    void deleteByUsername(String username);
}
