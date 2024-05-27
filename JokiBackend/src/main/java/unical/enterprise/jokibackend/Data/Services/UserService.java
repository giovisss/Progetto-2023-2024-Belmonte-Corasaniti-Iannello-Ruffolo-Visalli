package unical.enterprise.jokibackend.Data.Services;

import java.util.Collection;
import java.util.UUID;

import org.springframework.data.jpa.domain.Specification;

import unical.enterprise.jokibackend.Data.Entities.User;
import unical.enterprise.jokibackend.Dto.UserDto;

public interface UserService {

    public UserDto getUserById(UUID id);

    public UserDto getUserByUsername(String username);

    public UserDto getUserByEmail(String email);

    public Collection<UserDto> getAllUsers(Specification<User> spec);

    public UserDto save(UserDto userDto);

    public void delete(UUID id);
}
