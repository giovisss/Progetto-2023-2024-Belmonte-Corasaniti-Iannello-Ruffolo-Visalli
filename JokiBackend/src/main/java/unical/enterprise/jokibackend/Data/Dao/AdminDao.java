package unical.enterprise.jokibackend.Data.Dao;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import unical.enterprise.jokibackend.Data.Entities.Admin;
import unical.enterprise.jokibackend.Data.Entities.Game;

import java.util.ArrayList;
import java.util.Optional;


@Repository
public interface AdminDao extends JpaRepository<Admin, UUID>{

    Optional<Admin> findAdminByEmail(String email);

    Optional<Admin> findAdminByUsername(String username);

    @Query("SELECT g FROM games g WHERE g.admin_id = :id")
    ArrayList<Game> findGamesByThisAdminId(UUID id);
}
