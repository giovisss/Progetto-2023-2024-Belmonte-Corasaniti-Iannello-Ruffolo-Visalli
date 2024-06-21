package unical.enterprise.jokibackend.Data.Services;

import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import unical.enterprise.jokibackend.Data.Dao.UserDao;
import unical.enterprise.jokibackend.Data.Entities.User;
import unical.enterprise.jokibackend.Data.Services.Interfaces.UserService;
import unical.enterprise.jokibackend.Data.Dto.UserDto;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    private final ModelMapper modelMapper;

    @Override
    public void save(User user) {
        userDao.save(user);
    }

    @Override
    public UserDto getUserById(UUID id) {
        User user = userDao.findById(id).orElse(null);
        return checkBeforeReturn(user);
    }
    
    @Override
    public UserDto getUserByUsername(String username) {
        User user = userDao.findUserByUsername(username).orElse(null);
        return checkBeforeReturn(user);
    }
    
    @Override
    public UserDto getUserByEmail(String email) {
        User user = userDao.findUserByEmail(email).orElse(null);
        return checkBeforeReturn(user);
    }
    
    @Override
    public Collection<UserDto> getAllUsers() {
        return userDao.findAll()
            .stream().map(user -> modelMapper
            .map(user, UserDto.class))
            .collect(Collectors.toList());
    }

    @Override
    public UserDto updateUser(String username, UserDto userDto) {
        User user = userDao.findUserByUsername(username).orElse(null);
        if (user == null) {
            return null;
        }
        user = modelMapper.map(userDto, User.class);
        userDao.save(user);
        return modelMapper.map(user, UserDto.class);
    }
    
    @Override
    public void delete(UUID id) {
        userDao.deleteById(id);
    }


    @Override
    public void deleteByUsername(String username) {
        userDao.deleteByUsername(username);
    }

    private UserDto checkBeforeReturn(User user) {
        if(user == null) return null;

        return modelMapper.map(user, UserDto.class);
    }
}
