package unical.enterprise.jokibackend.Data.Dao;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import unical.enterprise.jokibackend.Data.Entities.Game;
import unical.enterprise.jokibackend.Data.Entities.User;

@Repository
public interface UserDao extends JpaRepository<User, UUID> {
   Optional<User> findUserByEmail(String email);
   Optional<User> findUserByUsername(String username);
   void deleteByUsername(String username);

   @Query("SELECT u.games FROM users u WHERE u.username = :username")
   Optional<Collection<Game>> findGamesByUsername(@Param("username") String username);

    @Query("SELECT u.friends FROM users u WHERE u.username = :username")
    Optional<Collection<User>> findFriendsByUsername(@Param("username") String username);
    
    @Modifying
    @Query(value = "INSERT INTO libraries (user_id, game_id) " +
                   "SELECT u.id, :gameId FROM users u WHERE u.username = :username", 
           nativeQuery = true)
    int addGameToLibrary(@Param("username") String username, @Param("gameId") UUID gameId);
    

    @Modifying
    @Query(value = "DELETE FROM libraries " +
                   "WHERE user_id = (SELECT id FROM users WHERE username = :username) " +
                   "AND game_id = :gameId", 
           nativeQuery = true)
    int removeGameFromLibrary(@Param("username") String username, @Param("gameId") UUID gameId);
    ;
}
