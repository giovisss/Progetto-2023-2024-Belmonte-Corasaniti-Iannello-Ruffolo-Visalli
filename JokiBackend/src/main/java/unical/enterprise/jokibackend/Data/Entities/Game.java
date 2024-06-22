package unical.enterprise.jokibackend.Data.Entities;

import java.util.Collection;
import java.util.Date;
import java.util.UUID;

import jakarta.persistence.*;
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
    private String title;

    @Column
    private String description;

    @Column
    private Double price;

    @Column
    private String imagePath;

    @Column
    private String genre;

    @Column
    private String developer;

    @Column
    private String publisher;

    @Column(columnDefinition = "DATE")
    private Date releaseDate;

    @Column
    private Integer stock;

    @ManyToOne
    @JoinColumn(name = "admin_id")
    private Admin admin;

    @ManyToMany(mappedBy = "games")
    private Collection<User> users;
}