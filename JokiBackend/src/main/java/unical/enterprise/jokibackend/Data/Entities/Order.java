package unical.enterprise.jokibackend.Data.Entities;

import java.util.Date;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@Entity(name = "orders")
@AllArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.UUID)
    @Column(name = "order_id")
    private UUID order_id;

    @Column(name = "order_user_id")
    private UUID order_user_id;

    @Column(name = "order_game_id")
    private UUID order_game_id;

    @Column(name = "order_date")
    private Date order_date;

    @Column(name = "order_price")
    private double order_price;

    @Column(name = "order_status")
    private Boolean order_status;
}
