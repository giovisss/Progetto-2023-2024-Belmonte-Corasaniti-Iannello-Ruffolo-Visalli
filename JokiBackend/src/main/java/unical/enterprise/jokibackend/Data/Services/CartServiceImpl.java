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
    public CartDto update(UUID id, CartDto cartDto) {
        Cart cart = cartDao.findById(id).orElse(null);
        if (cart == null) {
            return null;
        }
        ModelMapper modelMapperUpdater = new ModelMapper();
        modelMapperUpdater.typeMap(CartDto.class, Cart.class).addMappings(mapper -> {
            mapper.skip(Cart::setId);
        });
        modelMapperUpdater.map(cartDto, cart);
        cartDao.save(cart);
        return modelMapperUpdater.map(cart, CartDto.class);
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
}
