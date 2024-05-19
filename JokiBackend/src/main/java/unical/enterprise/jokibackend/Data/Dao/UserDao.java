package unical.enterprise.jokibackend.Data.Dao;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import unical.enterprise.jokibackend.Data.Entities.User;

@Repository
public interface UserDao extends JpaRepository<User, UUID>{

    // servono le query oppure cambiamo i campi in modo che siano quelli della convenzione hibernate
    @Query("SELECT u FROM users u WHERE u.user_id = :id") 
    Optional<User> findUserById(UUID id);

    @Query("SELECT u FROM users u WHERE u.user_email = :email")
    Optional<User> findUserByEmail(String email);

    @Query("SELECT u FROM users u WHERE u.user_username = :username")
    Optional<User> findUserByUsername(String username);
}
