package unical.enterprise.jokibackend.Data.Dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import unical.enterprise.jokibackend.Data.Entities.Game;

import java.util.UUID;

@Repository
public interface GameDao extends JpaRepository<Game, UUID> {
   // Optional<Game> findGameByTitle(String title);
   // Optional<Game> findGameByDeveloper(String developer);
   // Optional<Game> findGameByGenre(String genre);
}
