package unical.enterprise.jokibackend.Data.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Data;

@Data // lombok, genera getter e setter
@Entity(name = "games") // jakarta, fa riferimento alla tabella games
public class Game {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.AUTO)
    @Column(name = "game_id")
    private Long game_id;

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
    private String game_releaseDate;
}
