package unical.enterprise.jokibackend.Data.Entities;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data // lombok, genera getter e setter
@Entity(name="achievements") // jakarta, fa riferimento alla tabella achievements
public class Achievement {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.AUTO)
    @Column(name = "achievement_id")
    private Long achievement_id;

    @Column(name = "achievement_name")
    private String achievement_name;

    @Column(name = "achievement_description")
    private String achievement_description;

    @Column(name = "achievement_image")
    private String achievement_image;

    @Column(name = "achievement_date")
    private Date achievement_date;

    @ManyToOne(fetch = FetchType.LAZY) // un achievement appartiene ad un solo gioco
    @JoinColumn(name = "game_id")
    private Game game;

    @ManyToMany(mappedBy = "achievements", fetch = FetchType.LAZY) // un achievement può essere sbloccato da più utenti
    private Set<User> users = new HashSet<>();

}
