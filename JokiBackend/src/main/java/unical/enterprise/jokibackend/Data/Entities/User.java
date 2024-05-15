package unical.enterprise.jokibackend.Data.Entities;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.Data;

@Data // lombok, genera getter e setter
@Entity(name="users") // jakarta, fa riferimento alla tabella users
public class User {
    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.AUTO)
    @Column(name = "user_id")
    private Long user_id;

    @Column(name = "user_password")
    private String user_password;

    @Column(name = "user_role")
    private String user_role;

    @Column(name = "user_username", unique = true)
    private String user_username;

    @ManyToMany(mappedBy = "user_games", fetch = FetchType.LAZY) // un utente può giocare a più giochi
    private List <Game> user_games = new ArrayList<>();

    @ManyToMany(fetch = FetchType.LAZY) // un utente può sbloccare più achievement
    @JoinTable(name = "user_achievements",
               joinColumns = @JoinColumn(name = "user_id"),
               inverseJoinColumns = @JoinColumn(name = "achievement_id"))
    private Set<Achievement> achievements = new HashSet<>();
}
