package unical.enterprise.jokibackend.Data.Dao;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import unical.enterprise.jokibackend.Data.Dto.GameDto;

@Repository
public interface GameDao extends JpaRepository<GameDto, UUID> {
   // Optional<Game> findGameByTitle(String title);
   // Optional<Game> findGameByDeveloper(String developer);
   // Optional<Game> findGameByGenre(String genre);
}
