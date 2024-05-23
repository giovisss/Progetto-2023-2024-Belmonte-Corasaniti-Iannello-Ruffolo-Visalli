package unical.enterprise.jokibackend.Data.Dao;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import unical.enterprise.jokibackend.Data.Entities.User;

@Repository
public interface UserDao extends JpaRepository<User, UUID>, JpaSpecificationExecutor<User>{

    Optional<User> findUserById(UUID id);

    Optional<User> findUserByEmail(String email);

    Optional<User> findUserByUsername(String username);
}
