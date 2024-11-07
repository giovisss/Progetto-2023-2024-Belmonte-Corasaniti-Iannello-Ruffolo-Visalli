package unical.enterprise.jokibackend.Data.Services;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import unical.enterprise.jokibackend.Data.Dao.GameDao;
import unical.enterprise.jokibackend.Data.Dao.UserDao;
import unical.enterprise.jokibackend.Data.Dto.GameDto;
import unical.enterprise.jokibackend.Data.Dto.UpdateUserDto;
import unical.enterprise.jokibackend.Data.Dto.UserDto;
import unical.enterprise.jokibackend.Data.Entities.Game;
import unical.enterprise.jokibackend.Data.Entities.User;
import unical.enterprise.jokibackend.Data.Services.Interfaces.AdminService;
import unical.enterprise.jokibackend.Data.Services.Interfaces.UserService;
import unical.enterprise.jokibackend.Utility.CustomContextManager.UserContext;
import unical.enterprise.jokibackend.Utility.CustomContextManager.UserContextHolder;
import unical.enterprise.jokibackend.Utility.KeycloakManager;
import unical.enterprise.jokibackend.Utility.UserFriendship;

import javax.ws.rs.NotFoundException;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

import static unical.enterprise.jokibackend.Utility.NotificationStatus.ACCEPTED;
import static unical.enterprise.jokibackend.Utility.NotificationStatus.PENDING;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final ModelMapper modelMapper;
    private final GameDao gameDao;
    private final AdminService adminService;

    @Override
    public void save(User user) {
        userDao.save(user);
    }

    @Override
    public boolean firstLogin() {
        try {
            if (adminService.getById(UserContextHolder.getContext().getId()) != null) return false;

            if (userDao.findUserByUsername(UserContextHolder.getContext().getPreferredUsername()).orElse(null) == null) {
                UserContext context = UserContextHolder.getContext();

                userDao.save(userDao.save(new User(
                        UUID.fromString(KeycloakManager.getUsersResource().search(context.getPreferredUsername(), true).get(0).getId()),
                        context.getPreferredUsername(),
                        context.getEmail(),
                        context.getGivenName(),
                        context.getFamilyName(),
                        null,null,null,null,null)));
            } else {
                return false;
            }

            return true;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return false;
        }
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
    public Boolean updateUser(String username, UpdateUserDto userDto) {
        User user = userDao.findUserByUsername(username).orElse(null);

        if (user == null)   return false;

        user.setUsername(userDto.getUsername());
        user.setEmail(userDto.getEmail());
        user.setFirstName(userDto.getFirstName());
        user.setLastName(userDto.getLastName());
        user.setBirthdate(userDto.getBirthdate());

        userDao.save(user);

        return true;
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

    public Collection<GameDto> getGamesByUsername(String username) {
        return userDao.findGamesByUsername(username)
                       .orElseThrow(() -> new UsernameNotFoundException("User not found"))
                       .stream()
                       .map(game -> modelMapper.map(game, GameDto.class))
                       .collect(Collectors.toList());
    }

    @Override
    public User getFriendByUsername(String first, String second) {
        Collection<User> friends = userDao.findFriendsByUsername(first).orElse(null);

        if(friends == null) return null;

        return friends.stream().filter(friend -> friend.getUsername().equals(second)).findFirst().orElse(null);
    }

    @Override
    public UserFriendship isFriend(String other) {
        String user=UserContextHolder.getContext().getPreferredUsername();

        UserFriendship out = UserFriendship.NOT_FRIENDS;

        if (user.equals(other)) {
            out = UserFriendship.FRIENDS;
        } else if (getFriendByUsername(user, other) != null && getFriendByUsername(other, user) != null) {
            out = UserFriendship.FRIENDS;
        } else if (getFriendByUsername(user, other) != null) {
            out = UserFriendship.PENDING;
        } else if (getFriendByUsername(other, user) != null) {
            out = UserFriendship.REQUESTED;
        }

        return out;
    }

    @Override
    @Transactional
    public void editFriendship(String other, boolean add) {
        UUID user=UserContextHolder.getContext().getId();
        UUID oth=userDao.findUserByUsername(other).orElseThrow(() -> new UsernameNotFoundException("User not found")).getId();

        if (add) {
            userDao.addFriendship(user, oth);
        } else {
            userDao.removeFriendship(user, oth);
        }
    }

    @Override
    @Transactional
    public boolean addGameToUserLibrary(String username, UUID gameId) {
        Game game = gameDao.findById(gameId)
                .orElseThrow(() -> new EntityNotFoundException("Game not found"));

        if (userDao.findGamesByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"))
                .stream().anyMatch(existingGame -> existingGame.getId().equals(gameId))) {
            return false;
        }

        if (game.getStock() <= 0) {
            throw new IllegalStateException("No stock available for this game");
        }

        int updatedRows = userDao.addGameToLibrary(username, gameId);
        if (updatedRows > 0) {
            game.setStock(game.getStock() - 1);
            gameDao.save(game);
            return true;
        }
    
        return false;
    }

    @Override
    @Transactional
    public boolean removeGameFromUserLibrary(String username, UUID gameId) {
        if (!gameDao.existsById(gameId)) {
            throw new EntityNotFoundException("Game not found");
        }
        int updatedRows = userDao.removeGameFromLibrary(username, gameId);
        return updatedRows > 0;
    }

    @Override
    public Collection<GameDto> getUserCart(String username) {
        User user = userDao.findUserByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return user.getCartGames()
                .stream()
                .map(game -> modelMapper.map(game, GameDto.class))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public boolean addGameToUserCart(String username, UUID gameId) {
        if (userDao.findGamesByUsername(username).orElseThrow(() -> new UsernameNotFoundException("User not found"))
                .stream().anyMatch(existingGame -> existingGame.getId().equals(gameId))) {
            throw new IllegalStateException("Game already in library");
        }
        if (!gameDao.existsById(gameId)) {
            throw new EntityNotFoundException("Game not found");
        }
        int updatedRows = userDao.addGameToCart(username, gameId);
        return updatedRows > 0;
    }

    @Override
    @Transactional
    public boolean removeGameFromUserCart(String username, UUID gameId) {
        int updatedRows = userDao.removeGameFromCart(username, gameId);
        return updatedRows > 0;
    }

    @Override
    @Transactional
    public void clearUserCart(String username) {
        userDao.clearUserCart(username);
    }

    @Override
    public boolean checkIfFriend(String other) {
        var  tmp=userDao.findUserByUsername(other);
        if (tmp.isEmpty()) throw new NotFoundException();
        UUID oth=tmp.get().getId();

        UUID user=UserContextHolder.getContext().getId();

        int response=userDao.checkFriendship(user, oth);
        return response == 2;
    }

    @Override
    public void deleteGameFromUsers(UUID id) {
        userDao.deleteGameFromUsers(id);
    }

    @Override
    @Transactional
    public void checkout() {
        String username = UserContextHolder.getContext().getPreferredUsername();
        Collection<GameDto> cartGames = getUserCart(username);

        if (cartGames.isEmpty()) {
            throw new IllegalStateException("Cart is empty");
        }

        try {
            for (GameDto gameDto : cartGames) {
                // Verifica disponibilità prima del checkout
                Game game = gameDao.findById(gameDto.getId())
                        .orElseThrow(() -> new EntityNotFoundException("Game not found: " + gameDto.getId()));

                if (game.getStock() <= 0) {
                    throw new IllegalStateException("Game out of stock: " + game.getTitle());
                }
            }

            // Se tutti i controlli passano, procedi con il checkout
            for (GameDto gameDto : cartGames) {
                addGameToUserLibrary(username, gameDto.getId());
            }

            // Svuota il carrello solo dopo che tutti i giochi sono stati aggiunti con successo
            clearUserCart(username);
        } catch (Exception e) {
            // In caso di errore, esegui il rollback della transazione
            throw new RuntimeException("Checkout failed: " + e.getMessage(), e);
        }
    }
}

