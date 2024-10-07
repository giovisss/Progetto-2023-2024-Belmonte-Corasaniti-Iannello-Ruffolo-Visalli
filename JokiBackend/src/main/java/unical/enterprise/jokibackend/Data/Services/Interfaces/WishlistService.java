package unical.enterprise.jokibackend.Data.Services.Interfaces;

import unical.enterprise.jokibackend.Data.Dto.GameDto;
import unical.enterprise.jokibackend.Data.Entities.Wishlist;
import unical.enterprise.jokibackend.Data.Dto.WishlistDto;
import java.util.UUID;
import java.util.Collection;

public interface WishlistService {
    void save(Wishlist wishlist);
    void delete(Wishlist wishlist);
    Collection<WishlistDto> findAll();
    Collection<WishlistDto> getOtherWishlists(String other);
    WishlistDto getOtherWishlistByWishlistName(String other, String wishlistName);
    WishlistDto getByWishlistName(String wishlistName);
    Collection<WishlistDto> getByUserId(UUID id);
    boolean addGameToWishlist(GameDto game, String wishlistName);
    void removeGameFromWishlist(GameDto game, String wishlistName);
    void addWishlist(String wishlistName);

    void deleteByGameId(UUID id);
}
