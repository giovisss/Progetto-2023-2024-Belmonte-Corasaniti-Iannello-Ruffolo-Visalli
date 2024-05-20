package unical.enterprise.jokibackend.Data.Entities;

import java.util.Date;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data // lombok, genera getter e setter
@Entity(name = "games") // jakarta, fa riferimento alla tabella games
@AllArgsConstructor
@NoArgsConstructor
public class Game {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.UUID)
    @Column
    private UUID id;

    @Column
    private String name;

    @Column
    private String description;

    @Column
    private Double price;

    @Column
    private String image; // se vogliamo salvare l'immagine in base64

    @Column
    private String category;

    @Column
    private String developer;

    @Column
    private String publisher;

    @Column(columnDefinition = "DATE")
    private Date releaseDate;

    @Column
    private Integer stock;

    @ManyToOne
    @JoinColumn
    private Admin admin;
}