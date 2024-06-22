package unical.enterprise.jokibackend.Data.Dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import unical.enterprise.jokibackend.Data.Entities.Wishlist;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface WishlistDao extends JpaRepository<Wishlist, UUID> {
   Optional<Wishlist> findWishlistByUserId(UUID id);
   // Optional<Wishlist> findWishlistByGameId(UUID id);
}
