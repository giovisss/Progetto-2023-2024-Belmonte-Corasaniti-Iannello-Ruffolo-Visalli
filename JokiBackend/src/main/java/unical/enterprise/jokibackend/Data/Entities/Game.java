package unical.enterprise.jokibackend.Data.Entities;

import java.util.Date;
import java.util.Set;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data // lombok, genera getter e setter
@Entity(name = "games") // jakarta, fa riferimento alla tabella games
@AllArgsConstructor
public class Game {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.UUID)
    @Column(name = "game_id")
    private UUID game_id;

    @Column(name = "game_name")
    private String game_name;

    @Column(name = "game_description")
    private String game_description;

    @Column(name = "game_price")
    private double game_price;

    @Column(name = "game_image")
    private String game_image; // se vogliamo salvare l'immagine in base64

    @Column(name = "game_category")
    private String game_category;

    @Column(name = "game_developer")
    private String game_developer;

    @Column(name = "game_publisher")
    private String game_publisher;

    @Column(name = "game_release_date")
    private Date game_releaseDate;

    @Column(name = "game_stock")
    private int game_stock;

    @Column(name = "admin_id")
    private UUID admin_id;

    @ManyToMany(mappedBy = "games")
    private Set<Wishlist> wishlists;
}