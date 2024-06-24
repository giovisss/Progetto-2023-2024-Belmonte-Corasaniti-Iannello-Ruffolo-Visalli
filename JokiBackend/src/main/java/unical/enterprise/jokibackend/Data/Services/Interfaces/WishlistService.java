package unical.enterprise.jokibackend.Data.Services.Interfaces;

import unical.enterprise.jokibackend.Data.Entities.Game;
import unical.enterprise.jokibackend.Data.Entities.Wishlist;
import unical.enterprise.jokibackend.Data.Dto.WishlistDto;
import java.util.UUID;
import java.util.Collection;

public interface WishlistService {
    void save(Wishlist wishlist);
    void delete(Wishlist wishlist);
    void deleteById(UUID id);
    Collection<WishlistDto> findAll();
    WishlistDto getByUserId(UUID id);
    WishlistDto getByUserUsername(String username);
    boolean addGameToWishlist(Game game, String wishlistName);
    void addWishlist(String wishlistName);
    // Collection<WishlistDto> getByGameId(UUID id);
    // Collection<WishlistDto> getByGameName(String name);

}
