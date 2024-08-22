package unical.enterprise.jokibackend.Data.Services;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import unical.enterprise.jokibackend.Data.Dto.UserDto;
import unical.enterprise.jokibackend.Data.Dto.GameDto;
import unical.enterprise.jokibackend.Data.Entities.Game;
import unical.enterprise.jokibackend.Data.Entities.User;
import unical.enterprise.jokibackend.Data.Entities.Wishlist;
import unical.enterprise.jokibackend.Data.Services.Interfaces.UserService;
import unical.enterprise.jokibackend.Data.Services.Interfaces.WishlistService;
import unical.enterprise.jokibackend.Data.Dto.WishlistDto;
import unical.enterprise.jokibackend.Data.Dao.WishlistDao;
import unical.enterprise.jokibackend.Utility.CustomContextManager.UserContextHolder;

import java.util.*;

@Service
@RequiredArgsConstructor

public class WishlistServiceImpl implements WishlistService {

   private final WishlistDao wishlistDao;
   private final UserService userService;
   private final ModelMapper modelMapper;

   @Override
   public void save(Wishlist wishlist) {
       wishlistDao.save(wishlist);
   }

   @Override
   public void delete(Wishlist wishlist) {
       wishlistDao.delete(wishlist);
   }

   @Override
   public void deleteById(UUID id) {
       wishlistDao.deleteById(id);
   }

   @Override
   public Collection<WishlistDto> findAll() {
       return wishlistDao.findAll()
               .stream().map(wishlist -> modelMapper
               .map(wishlist, WishlistDto.class))
               .toList();
   }

    @Override
    public Collection<WishlistDto> getOthersWishlists(String other) {
        int lower = 2,upper = 2;

        // check if friends
        if(userService.checkIfFriend(other)) lower=1;

        // 0 private, 1 friend, 2 public
        // if friend check for wishlist with 1 or 2 as visibility
        // otherwise just 2
        UUID porcodio=userService.getUserByUsername(other).getId();
        Optional<Collection<Wishlist>> found = wishlistDao.findWishlistByUserFriendship(porcodio,lower,upper);
        Collection<WishlistDto> out = new ArrayList<>();

        if (found.isPresent())
            for(Wishlist wishlist : found.get())
                out.add(modelMapper.map(wishlist,WishlistDto.class));

        return out;
    }

    @Override
    public WishlistDto getByWishlistName(String wishlistName) {
        Wishlist wishlist = wishlistDao.findWishlistByWishlistName(wishlistName).orElse(null);
        return modelMapper.map(wishlist, WishlistDto.class);
    }

    @Override
   public WishlistDto getByUserId(UUID id) {
       Wishlist wishlist = wishlistDao.findWishlistByUserId(id).orElse(null);
       return modelMapper.map(wishlist, WishlistDto.class);
   }

//    @Override
//    public Collection<WishlistDto> getByGameId(UUID id) {
//        Wishlist wishlist = wishlistDao.findWishlistByGameId(id).orElse(null);
//        return List.of(modelMapper.map(wishlist, WishlistDto.class));
//    }

   @Override
   public WishlistDto getByUserUsername(String username) {
       Wishlist wishlist = wishlistDao.findWishlistByUserId(UUID.fromString(username)).orElse(null);
       return modelMapper.map(wishlist, WishlistDto.class);
   }

    @Override
    public boolean addGameToWishlist(GameDto game, String wishlistName) {
        Wishlist wishlist = wishlistDao.findWishlistByWishlistName(wishlistName).orElse(null);
        WishlistDto wishlistDto = modelMapper.map(wishlist, WishlistDto.class);
        if (wishlistDto == null) {
            return false;
        }
        else {
            wishlistDto.getGame().add(game);
            wishlistDao.save(modelMapper.map(wishlistDto, Wishlist.class));
            return true;
        }
    }

    @Override
    public void removeGameFromWishlist(GameDto game, String wishlistName) {
        wishlistDao.deleteByUserAndWishlistNameAndGamesContaining(
                modelMapper.map(userService.getUserByUsername(UserContextHolder.getContext().getPreferredUsername()), User.class),
                wishlistName,
                modelMapper.map(game, Game.class)
        );
    }

    @Override
    public void addWishlist(String wishlistName) {
        WishlistDto wishlistDto = new WishlistDto();
        wishlistDto.setWishlistName(wishlistName);
        UserDto user = userService.getUserByUsername(SecurityContextHolder.getContext().getAuthentication().getName());
        wishlistDto.setUser(modelMapper.map(user, User.class));

        wishlistDao.save(modelMapper.map(wishlistDto, Wishlist.class));
    }

//    @Override
//    public Collection<WishlistDto> getByGameName(String name) {
//        Wishlist wishlist = wishlistDao.findWishlistByGameId(UUID.fromString(name)).orElse(null);
//        return List.of(modelMapper.map(wishlist, WishlistDto.class));
//    }
}
