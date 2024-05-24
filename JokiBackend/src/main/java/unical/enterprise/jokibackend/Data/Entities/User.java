package unical.enterprise.jokibackend.Data.Entities;

import java.util.Date;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.UUID)
    @Column
    private UUID id;

    @Column
    private String password;

    @Column(unique = true)
    private String username;
    
    @Column(unique = true)
    private String email;

    @Column
    private String name;

    @Column
    private String surname;

    @Column(columnDefinition = "DATE")
    private Date birthdate;

    @Column
    private String address;

    @OneToOne
    @JoinColumn
    private Wishlist wishlist;

    @OneToMany
    @JoinColumn
    private Order order;

    @OneToMany
    @JoinColumn
    private Game game;
}