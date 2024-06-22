package unical.enterprise.jokibackend.Data.Entities;

import java.util.Collection;
import java.util.UUID;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

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