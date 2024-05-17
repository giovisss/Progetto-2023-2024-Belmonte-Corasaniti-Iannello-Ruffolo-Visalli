package unical.enterprise.jokibackend.Data.Entities;

import java.util.Set;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
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

    @OneToOne
    @JoinColumn(name = "wishlist_user_id", referencedColumnName = "user_id")
    private User user;

    // @ManyToMany
    // @JoinTable(
    //     name = "wishlist_games",
    //     joinColumns = @JoinColumn(name = "wishlist_id"),
    //     inverseJoinColumns = @JoinColumn(name = "game_id")
    // )
    // private Set<Game> games;
}
