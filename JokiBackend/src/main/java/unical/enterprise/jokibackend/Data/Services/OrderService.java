package unical.enterprise.jokibackend.Data.Services;

import unical.enterprise.jokibackend.Data.Entities.Order;
import unical.enterprise.jokibackend.Dto.OrderDto;

import java.util.Collection;
import java.util.UUID;

public interface OrderService {
    void save(Order order);

    void delete(Order order);

    void deleteById(UUID id);

    void update(Order order);

    OrderDto getById(UUID id);
    Collection<OrderDto> findAll();
    Collection<OrderDto> getByUserId(UUID id);
    Collection<OrderDto> getByGameId(UUID id);
    Collection<OrderDto> getOrderByStatus(Boolean status);
    Collection<OrderDto> getOrdersByGameTitle(String title);
}
