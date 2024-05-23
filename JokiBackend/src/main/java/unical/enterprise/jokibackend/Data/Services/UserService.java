package unical.enterprise.jokibackend.Data.Services;

import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import unical.enterprise.jokibackend.Data.Dao.UserDao;
import unical.enterprise.jokibackend.Data.Entities.User;
import unical.enterprise.jokibackend.Dto.UserDto;

@Service
public class UserService {
    
    @Autowired
    private UserDao userDao;

    private ModelMapper modelMapper;

    public UserDto getUserById(UUID id) {
        User user = userDao.findById(id).orElse(null);
        return modelMapper.map(user, UserDto.class);
    }

    public Collection<UserDto> getAllUsers() {
        return userDao.findAll().stream().map(user -> modelMapper.map(user, UserDto.class)).collect(Collectors.toList());
    }

    public UserDto save(UserDto userDto) {
        User user = modelMapper.map(userDto, User.class);
        userDao.save(user);
        return modelMapper.map(user, UserDto.class);
    }
}
