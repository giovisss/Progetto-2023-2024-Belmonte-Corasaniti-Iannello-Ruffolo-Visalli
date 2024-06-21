package unical.enterprise.jokibackend.Data.Entities;

import java.util.Collection;
import java.util.Date;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@Entity(name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @Column
    private UUID id;

    @Column(unique = true)
    private String username;
    
    @Column(unique = true)
    private String email;

    @Column
    private String firstName;

    @Column
    private String lastName;

    @Column(columnDefinition = "DATE")
    private Date birthdate;

    @OneToOne
    private Wishlist wishlist;

    @OneToMany
    private Collection<Cart> cart;

    @OneToOne
    private Library library;

    // @OneToMany
    // private Collection<Game> game;
}