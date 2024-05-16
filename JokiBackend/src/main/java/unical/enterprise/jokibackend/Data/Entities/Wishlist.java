package unical.enterprise.jokibackend.Data.Entities;

import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@Entity(name = "wishlists")
@AllArgsConstructor
public class Wishlist {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.UUID)
    @Column(name = "wishlist_id")
    private UUID wishlist_id;

    @Column(name = "wishlist_user_id")
    private UUID wishlist_user_id;

    @Column(name = "wishlist_game_id")
    private UUID wishlist_game_id;
}
