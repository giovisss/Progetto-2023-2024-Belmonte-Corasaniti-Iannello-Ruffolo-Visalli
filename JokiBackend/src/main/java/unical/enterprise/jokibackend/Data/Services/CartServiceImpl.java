package unical.enterprise.jokibackend.Data.Services;

import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import unical.enterprise.jokibackend.Data.Dao.CartDao;
import unical.enterprise.jokibackend.Data.Entities.Cart;
import unical.enterprise.jokibackend.Data.Services.Interfaces.CartService;
import unical.enterprise.jokibackend.Data.Dto.CartDto;

import java.util.Collection;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartDao cartDao;
    private final ModelMapper modelMapper;

    @Override
    public void save(Cart cart) {
            cartDao.save(cart);
    }

    @Override
    public void delete(Cart cart) {
        cartDao.delete(cart);
    }

    @Override
    public void deleteById(UUID id) {
        cartDao.deleteById(id);
    }

    @Override
    public void update(Cart cart) {
        cartDao.save(cart);
    }


    @Override
    public CartDto getById(UUID id) {
        Cart cart = cartDao.findById(id).orElse(null);
        return modelMapper.map(cart, CartDto.class);
    }

    @Override
    public Collection<CartDto> findAll() {
        return cartDao.findAll()
                .stream().map(cart -> modelMapper
                .map(cart, CartDto.class))
                .toList();
    }

    // @Override
    // public Collection<CartDto> getByUserId(UUID id) {
    //     return CartDao.findCartsByUserId(id)
    //             .stream().map(Cart -> modelMapper
    //             .map(Cart, CartDto.class))
    //             .toList();
    // }

    // @Override
    // public Collection<CartDto> getByGameId(UUID id) {
    //     return CartDao.findCartsByGameId(id)
    //             .stream().map(Cart -> modelMapper
    //             .map(Cart, CartDto.class))
    //             .toList();
    // }

    // @Override
    // public Collection<CartDto> getCartsByGameTitle(String title) {
    //     return CartDao.findCartsByGameTitle(title)
    //             .stream().map(Cart -> modelMapper
    //             .map(Cart, CartDto.class))
    //             .toList();
    // }
}
