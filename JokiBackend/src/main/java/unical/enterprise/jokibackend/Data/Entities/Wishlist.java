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
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "wishlist_name"})
)
public class Wishlist {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column
    private String wishlistName;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(columnDefinition = "int default 0")
    private Integer visibility;


    @ManyToMany
    @JoinTable(
        name = "wishlist_games",
        joinColumns = @JoinColumn(name = "wishlist_id"),
        inverseJoinColumns = @JoinColumn(name = "game_id"),
        uniqueConstraints = @UniqueConstraint(columnNames = {"wishlist_id", "game_id"})
    )
    private Collection<Game> games;

//    @ManyToOne
//    @JoinColumn(name = "game_id")
//    private Game game;
//


//    @JoinTable(
//        name = "wishlist_games",
//        joinColumns = @JoinColumn(name = "wishlist_id"),
//        inverseJoinColumns = @JoinColumn(name = "game_id")
//    )
//    private Collection<Game> games;

}