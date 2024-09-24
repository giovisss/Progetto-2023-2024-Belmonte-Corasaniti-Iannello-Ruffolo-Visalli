package unical.enterprise.jokibackend.Data.Dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import unical.enterprise.jokibackend.Data.Entities.Game;
import unical.enterprise.jokibackend.Data.Entities.User;
import unical.enterprise.jokibackend.Data.Entities.Wishlist;

import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface WishlistDao extends JpaRepository<Wishlist, UUID> {
   Optional<Wishlist> findWishlistByUserId(UUID id);

   @Query(value = "select * from wishlists w where w.user_id = :user_id and w.visibility between :visibility and :visibility2",
           nativeQuery = true)
   Optional<Collection<Wishlist>> findWishlistByUserFriendship(@Param("user_id") UUID user_id, @Param("visibility") Integer visibility, @Param("visibility2") Integer visibility2);
   Optional<Wishlist> findWishlistByWishlistName(String name);
   void deleteByWishlistName(String name);
   void deleteByUserAndWishlistNameAndGamesContaining(User user, String wishlistName, Game games);
   Optional<Wishlist> findWishlistByUserAndWishlistName(User user, String wishlistName);
   // Optional<Wishlist> findWishlistByGameId(UUID id);
}
