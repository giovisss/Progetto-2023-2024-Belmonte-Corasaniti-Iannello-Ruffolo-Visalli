package unical.enterprise.jokibackend.Data.Dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import unical.enterprise.jokibackend.Data.Entities.Review;

import java.util.Collection;
import java.util.UUID;

@Repository
public interface ReviewDao extends JpaRepository<Review, UUID> {
    @Query(value = "SELECT AVG(CASE WHEN suggested = true THEN 1 ELSE 0 END) FROM reviews WHERE game_id = :gameId", nativeQuery = true)
    Double getAverageRating(@Param("gameId") UUID gameId);

    @Query(value = "SELECT * FROM reviews WHERE game_id = :gameId", nativeQuery = true)
    Collection <Review> getReviewsByGameId(@Param("gameId") UUID gameId);

    @Query(value = "SELECT * FROM reviews WHERE user_id = :userId", nativeQuery = true)
    Collection <Review> getReviewsByUserId(@Param("userId") UUID userId);

    @Query(value = "SELECT * FROM reviews WHERE user_id = :userId AND game_id = :gameId", nativeQuery = true)
    Review getReviewByUserIdAndGameId(@Param("userId") UUID userId, @Param("gameId") UUID gameId);
}
