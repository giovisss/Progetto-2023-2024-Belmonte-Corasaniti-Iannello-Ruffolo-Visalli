package unical.enterprise.jokibackend.Data.Services;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import unical.enterprise.jokibackend.Data.Dao.WishlistDao;
import unical.enterprise.jokibackend.Data.Dto.GameDto;
import unical.enterprise.jokibackend.Data.Dto.UserDto;
import unical.enterprise.jokibackend.Data.Dto.WishlistDto;
import unical.enterprise.jokibackend.Data.Entities.User;
import unical.enterprise.jokibackend.Data.Entities.Wishlist;
import unical.enterprise.jokibackend.Data.Services.Interfaces.UserService;
import unical.enterprise.jokibackend.Data.Services.Interfaces.WishlistService;
import unical.enterprise.jokibackend.Utility.CustomContextManager.UserContextHolder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;
import java.util.UUID;

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
    public Collection<WishlistDto> getOtherWishlists(String other) {
        int lower = 2,upper = 2;

        // check if friends
        if(userService.checkIfFriend(other)) lower=1;

        // 0 private, 1 friend, 2 public
        // if friend check for wishlist with 1 or 2 as visibility
        // otherwise just 2
        Optional<Collection<Wishlist>> found = wishlistDao.findWishlistByUserFriendship(userService.getUserByUsername(other).getId(),lower,upper);
        Collection<WishlistDto> out = new ArrayList<>();

        if (found.isPresent())
            for(Wishlist wishlist : found.get())
                out.add(modelMapper.map(wishlist,WishlistDto.class));

        return out;
    }

    @Override
    public WishlistDto getByWishlistName(String wishlistName) {
        Wishlist wishlist = wishlistDao.findWishlistByUserAndWishlistName(
                modelMapper.map(userService.getUserByUsername(UserContextHolder.getContext().getPreferredUsername()), User.class),
                wishlistName
        ).orElse(null);
        return modelMapper.map(wishlist, WishlistDto.class);
    }

    @Override
    public WishlistDto getOtherWishlistByWishlistName(String other, String wishlistName) {
        WishlistDto out = null;

        WishlistDto wishlist = wishlistDao.findWishlistByUserAndWishlistName(
                modelMapper.map(userService.getUserByUsername(other), User.class),
                wishlistName
        ).map(w -> modelMapper.map(w, WishlistDto.class)).orElse(null);

        if(wishlist == null || wishlist.getVisibility() == 0) return null;

        if(wishlist.getVisibility() == 2) out = wishlist;
        else if(wishlist.getVisibility() == 1 && userService.checkIfFriend(other)) out = wishlist;

        return out;
    }

    @Override
   public Collection<WishlistDto> getByUserId(UUID id) {
       Collection<Wishlist> wishlist = wishlistDao.findWishlistByUserId(id).orElse(null);
       Collection<WishlistDto> out = new ArrayList<>();
         if (wishlist != null) {
              for (Wishlist w : wishlist) {
                out.add(modelMapper.map(w, WishlistDto.class));
              }
         }
       return out;
   }

//    @Override
//    public Collection<WishlistDto> getByGameId(UUID id) {
//        Wishlist wishlist = wishlistDao.findWishlistByGameId(id).orElse(null);
//        return List.of(modelMapper.map(wishlist, WishlistDto.class));
//    }


    //todo implementare
   @Override
   public WishlistDto getByUserUsername(String username) {
//       Wishlist wishlist = wishlistDao.findWishlistByUserId(UUID.fromString(username)).orElse(null);
//       return modelMapper.map(wishlist, WishlistDto.class);
         return null;
   }


    @Override
    public boolean addGameToWishlist(GameDto game, String wishlistName) {
        Wishlist wishlist = wishlistDao.findWishlistByUserAndWishlistName(
                modelMapper.map(userService.getUserByUsername(UserContextHolder.getContext().getPreferredUsername()), User.class),
                wishlistName
        ).orElse(null);
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
        Wishlist wishlist = wishlistDao.findWishlistByUserAndWishlistName(
                modelMapper.map(userService.getUserByUsername(UserContextHolder.getContext().getPreferredUsername()), User.class),
                wishlistName
        ).orElse(null);

        if(wishlist == null) return;

        WishlistDto wishlistDto = modelMapper.map(wishlist, WishlistDto.class);

        wishlistDto.getGame().remove(game);

        wishlistDao.save(modelMapper.map(wishlistDto, Wishlist.class));
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
