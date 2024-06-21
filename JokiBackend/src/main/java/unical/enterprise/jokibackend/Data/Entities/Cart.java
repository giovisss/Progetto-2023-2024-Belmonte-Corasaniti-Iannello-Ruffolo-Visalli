package unical.enterprise.jokibackend.Data.Entities;

import java.util.Collection;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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

    @Column
    private Double price;

    @OneToMany
    private Collection<Game> game;

    @ManyToOne
    private User user;
}
