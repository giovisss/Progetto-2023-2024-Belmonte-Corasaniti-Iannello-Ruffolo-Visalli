package unical.enterprise.jokibackend.Data.Dao;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import unical.enterprise.jokibackend.Data.Entities.Order;

@Repository
public interface OrderDao extends JpaRepository<Order, UUID> {
    Optional<Order> findOrdersByUserId(UUID id);
    Optional<Order> findOrdersByStatus(Boolean status);
    Optional<Order> findOrdersByGameId(UUID id);
    Optional<Order> findOrdersByGameTitle(String title);

}
