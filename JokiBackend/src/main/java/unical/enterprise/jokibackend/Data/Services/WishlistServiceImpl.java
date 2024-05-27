package unical.enterprise.jokibackend.Data.Services;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import unical.enterprise.jokibackend.Data.Entities.Wishlist;
import unical.enterprise.jokibackend.Dto.WishlistDto;
import unical.enterprise.jokibackend.Data.Dao.WishlistDao;
import java.util.Collection;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor

public class WishlistServiceImpl implements WishlistService {

    private final WishlistDao wishlistDao;
    private final ModelMapper modelMapper;

    public WishlistServiceImpl(WishlistDao wishlistDao, ModelMapper modelMapper) {
        this.wishlistDao = wishlistDao;
        this.modelMapper = modelMapper;
    }

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
    public WishlistDto getByUserId(UUID id) {
        Wishlist wishlist = wishlistDao.findWishlistByUser(id).orElse(null);
        return modelMapper.map(wishlist, WishlistDto.class);
    }

    @Override
    public Collection<WishlistDto> getByGameId(UUID id) {
        Wishlist wishlist = wishlistDao.findWishlistByGame(id).orElse(null);
        return List.of(modelMapper.map(wishlist, WishlistDto.class));
    }

    @Override
    public WishlistDto getByUserUsername(String username) {
        Wishlist wishlist = wishlistDao.findWishlistByUser(UUID.fromString(username)).orElse(null);
        return modelMapper.map(wishlist, WishlistDto.class);
    }

    @Override
    public Collection<WishlistDto> getByGameName(String name) {
        Wishlist wishlist = wishlistDao.findWishlistByGame(UUID.fromString(name)).orElse(null);
        return List.of(modelMapper.map(wishlist, WishlistDto.class));
    }
}
