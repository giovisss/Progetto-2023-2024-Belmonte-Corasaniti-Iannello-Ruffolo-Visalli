package unical.enterprise.jokibackend.Data.Dao;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import unical.enterprise.jokibackend.Data.Entities.User;

@Repository
public interface UserDao extends JpaRepository<User, UUID>{

    // non so se serve la query, i nomi di tabelle e dei campi non sono precisamente quelli delle convenzioni hibernate
    // magari facciamo qualche prova prima di andare avanti
    @Query("SELECT u FROM users u WHERE u.users_id = :id") 
    Optional<User> findUserById(UUID id);

    @Query("SELECT u FROM users u WHERE u.email = :email")
    Optional<User> findUserByEmail(String email);

    Optional<User> findUserByUsername(String username);
}
