package unical.enterprise.jokibackend.Data.Services.Interfaces;

import unical.enterprise.jokibackend.Data.Entities.Cart;
import unical.enterprise.jokibackend.Data.Dto.CartDto;

import java.util.Collection;
import java.util.UUID;

public interface CartService {
    void save(Cart cart);

    void delete(Cart cart);

    void deleteById(UUID id);

    void update(Cart cart);

    CartDto getById(UUID id);
    Collection<CartDto> findAll();
    Collection<CartDto> getByUserId(UUID id);
    Collection<CartDto> getByGameId(UUID id);
    Collection<CartDto> getOrdersByGameTitle(String title);
}
