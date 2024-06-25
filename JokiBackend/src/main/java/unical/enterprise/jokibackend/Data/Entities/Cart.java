package unical.enterprise.jokibackend.Data.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.UUID;

@Data
@Entity(name = "carts")
@AllArgsConstructor
@NoArgsConstructor
public class Cart {
    @Id
    @Column
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.UUID)
    private UUID id;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany
    @JoinTable(
        name = "cart_games",
        joinColumns = @JoinColumn(name = "cart_id"),
        inverseJoinColumns = @JoinColumn(name = "game_id")
    )
    private Collection<Game> games;

    @Column
    private Double price;
}