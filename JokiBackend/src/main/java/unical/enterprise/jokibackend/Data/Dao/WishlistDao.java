package unical.enterprise.jokibackend.Data.Dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import unical.enterprise.jokibackend.Data.Dto.WishlistDto;
import unical.enterprise.jokibackend.Data.Entities.Game;
import unical.enterprise.jokibackend.Data.Entities.User;
import unical.enterprise.jokibackend.Data.Entities.Wishlist;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WishlistDao extends JpaRepository<Wishlist, UUID> {
   Optional<Wishlist> findWishlistByUserId(UUID id);
   Optional<Wishlist> findWishlistByWishlistName(String name);
   void deleteByWishlistName(String name);
   void deleteByUserAndWishlistNameAndGamesContaining(User user, String wishlistName, Game games);
   // Optional<Wishlist> findWishlistByGameId(UUID id);
}
