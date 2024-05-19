package unical.enterprise.jokibackend.Services;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import unical.enterprise.jokibackend.Data.Dao.UserDao;
import unical.enterprise.jokibackend.Data.Entities.User;

@Service
public class UserService {
    @Autowired
    private UserDao userDao;
    
    public Optional<User> getUserById(UUID id) {
        return userDao.findById(id);
    }

    public Optional<User> getUserByEmail(String email) {
        return userDao.findUserByEmail(email);
    }

    public Optional<User> getUserByUsername(String username) {
        return userDao.findUserByUsername(username);
    }

    public User saveUser(User user) {
        return userDao.save(user);
    }

    public void deleteUser(UUID id) {
        userDao.deleteById(id);
    }

    public List<User> getAllUsers() {
        return userDao.findAll();
    }
}
