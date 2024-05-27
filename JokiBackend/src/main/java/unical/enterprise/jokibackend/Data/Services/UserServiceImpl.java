package unical.enterprise.jokibackend.Data.Services;

import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import unical.enterprise.jokibackend.Data.Dao.UserDao;
import unical.enterprise.jokibackend.Data.Entities.User;
import unical.enterprise.jokibackend.Dto.UserDto;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDao userDao;

    private final ModelMapper modelMapper;

    @Override
    public UserDto getUserById(UUID id) {
        User user = userDao.findById(id).orElse(null);
        return modelMapper.map(user, UserDto.class);
    }
    
    @Override
    public UserDto getUserByUsername(String username) {
        User user = userDao.findUserByUsername(username).orElse(null);
        return modelMapper.map(user, UserDto.class);
    }
    
    @Override
    public UserDto getUserByEmail(String email) {
        User user = userDao.findUserByEmail(email).orElse(null);
        return modelMapper.map(user, UserDto.class);
    }
    
    @Override
    public Collection<UserDto> getAllUsers(Specification<User> spec) {
        return userDao.findAll(spec)
        .stream().map(user -> modelMapper
        .map(user, UserDto.class))
        .collect(Collectors.toList());
    }
    
    @Override
    public UserDto save(UserDto userDto) {
        User user = modelMapper.map(userDto, User.class);
        userDao.save(user);
        return modelMapper.map(user, UserDto.class);
    }
    
    @Override
    public void delete(UUID id) {
        userDao.deleteById(id);
    }
}
