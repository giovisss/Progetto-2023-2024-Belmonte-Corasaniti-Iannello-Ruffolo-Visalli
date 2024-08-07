package unical.enterprise.jokibackend.Data.Dao;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
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
}
