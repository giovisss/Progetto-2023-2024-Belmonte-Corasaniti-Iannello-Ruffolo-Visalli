package unical.enterprise.jokibackend.Data.Entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

import java.util.Collection;
import java.util.UUID;

@Data
@Entity(name = "wishlists")
@AllArgsConstructor
@NoArgsConstructor
public class Wishlist {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.UUID)
    @Column
    private UUID id;

    @Column
    private String wishlistName;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToMany
    @JoinTable(
        name = "wishlist_games",
        joinColumns = @JoinColumn(name = "wishlist_id"),
        inverseJoinColumns = @JoinColumn(name = "game_id")
    )
    private Collection<GameDto> games;

    @Column(columnDefinition = "int default 0")
    private Integer visibility;
}