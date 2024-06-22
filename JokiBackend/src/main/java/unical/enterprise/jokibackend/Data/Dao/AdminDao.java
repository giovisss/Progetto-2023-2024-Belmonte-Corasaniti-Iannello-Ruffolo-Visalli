//package unical.enterprise.jokibackend.Data.Dao;
//
//import java.util.Collection;
//import java.util.Optional;
//import java.util.UUID;
//
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.stereotype.Repository;
//
//import unical.enterprise.jokibackend.Data.Entities.Admin;
//import unical.enterprise.jokibackend.Data.Entities.Game;
//
//
//@Repository
//public interface AdminDao extends JpaRepository<Admin, UUID> {
//
//    Optional<Admin> findAdminByEmail(String email);
//
//    Optional<Admin> findAdminByUsername(String username);
//
//    // Collection<Game> findGamesInsertByAdminId(UUID id);
//}
