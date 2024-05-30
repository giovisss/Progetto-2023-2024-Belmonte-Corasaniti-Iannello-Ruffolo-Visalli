package unical.enterprise.jokibackend.Data.Dao;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import unical.enterprise.jokibackend.Data.Entities.Order;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OrderDao extends JpaRepository<Order, UUID>, JpaSpecificationExecutor<Order>{
    Optional<Order> findOrdersByUserId(UUID id);
    Optional<Order> findOrdersByStatus(Boolean status);
    Optional<Order> findOrdersByGameId(UUID id);
    // Optional<Order> findOrdersByGameName(String name);

}
