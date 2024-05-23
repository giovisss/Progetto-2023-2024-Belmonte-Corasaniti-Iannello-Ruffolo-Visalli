package unical.enterprise.jokibackend.Data.Services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import unical.enterprise.jokibackend.DTO.UserDTO;
import unical.enterprise.jokibackend.Data.Dao.UserDao;
import unical.enterprise.jokibackend.Data.Entities.User;

@Service
public class UserService {
    
    @Autowired
    private UserDao userDao;

    public Optional<UserDTO> getUserById(UUID id) {
        Optional<User> user = userDao.findById(id);
        return user.map(this::convertToDTO);
    }

    public Optional<UserDTO> getUserByEmail(String email) {
        Optional<User> user = userDao.findUserByEmail(email);
        return user.map(this::convertToDTO);
    }

    public Optional<UserDTO> getUserByUsername(String username) {
        Optional<User> user = userDao.findUserByUsername(username);
        return user.map(this::convertToDTO);
    }

    public Optional<UserDTO> deleteUser(UUID id) {
        Optional<User> user = userDao.findById(id);
        if (user.isPresent()) {
            userDao.deleteById(id);
            return user.map(this::convertToDTO);
        }
        return Optional.empty();
    }

    public List<UserDTO> getAllUsers() {
        List<User> users = userDao.findAll();
        return users.stream().map(this::convertToDTO).toList();
    }

    public UserDTO saveUser(UserDTO userDTO) {
        User user = new User(
            userDTO.getId(),
            userDTO.getPassword(),
            userDTO.getUsername(),
            userDTO.getEmail(),
            userDTO.getName(),
            userDTO.getSurname(),
            userDTO.getBirthdate(),
            userDTO.getAddress());
        return convertToDTO(userDao.save(user));
    }

    public Optional<UserDTO> updateUser(UserDTO userDTO) {
        Optional<User> user = userDao.findById(userDTO.getId());
        if (user.isPresent()) {
            User updatedUser = new User(
                userDTO.getId(),
                userDTO.getPassword(),
                userDTO.getUsername(),
                userDTO.getEmail(),
                userDTO.getName(),
                userDTO.getSurname(),
                userDTO.getBirthdate(),
                userDTO.getAddress());
            return Optional.of(convertToDTO(userDao.save(updatedUser)));
        }
        return Optional.empty();
    }

    private UserDTO convertToDTO(User user) {
        return new UserDTO(
            user.getId(),
            user.getPassword(),
            user.getUsername(),
            user.getEmail(),
            user.getName(),
            user.getSurname(),
            user.getBirthdate(),
            user.getAddress());
    }
}
