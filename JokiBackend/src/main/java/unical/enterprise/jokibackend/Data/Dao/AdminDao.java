package unical.enterprise.jokibackend.Data.Dao;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import unical.enterprise.jokibackend.Data.Entities.Admin;
import unical.enterprise.jokibackend.Data.Entities.Game;


@Repository
public interface AdminDao extends JpaRepository<Admin, UUID> {
   Optional<Admin> findAdminByEmail(String email);
   Optional<Admin> findAdminByUsername(String username);
   
   @Query("SELECT g FROM games g WHERE g.admin.username = :username")
   Optional<Collection<Game>> findGamesByAdminUsername(@Param("username") String username);
}
