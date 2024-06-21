package unical.enterprise.jokibackend.Data.Services;

import unical.enterprise.jokibackend.Data.Entities.Cart;
import unical.enterprise.jokibackend.Data.Dto.OrderDto;

import java.util.Collection;
import java.util.UUID;

public interface OrderService {
    void save(Cart cart);

    void delete(Cart cart);

    void deleteById(UUID id);

    void update(Cart cart);

    OrderDto getById(UUID id);
    Collection<OrderDto> findAll();
    Collection<OrderDto> getByUserId(UUID id);
    Collection<OrderDto> getByGameId(UUID id);
    Collection<OrderDto> getOrderByStatus(Boolean status);
    Collection<OrderDto> getOrdersByGameTitle(String title);
}
