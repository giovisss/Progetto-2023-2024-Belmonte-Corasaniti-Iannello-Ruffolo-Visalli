package unical.enterprise.jokibackend.Data.Entities;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Collection;
import java.util.Date;
import java.util.UUID;

@Getter
@Setter
@Entity(name = "users")
@AllArgsConstructor
@NoArgsConstructor
public class User {
    @Id
    @Column
    private UUID id;

    @Size(min = 3, max = 20, message = "La lunghezza del nome utente deve essere compresa tra 3 e 20 caratteri")
    @NotBlank(message = "Il nome utente non può essere vuoto")
    @Column(unique = true)
    private String username;

    @Max(value = 30, message = "La lunghezza dell'email non può superare i 30 caratteri")
    @NotBlank(message = "L'email non può essere vuota")
    @Email(message = "Inserire un indirizzo email valido")
    @Column(unique = true)
    private String email;

    @NotBlank(message = "Il nome non può essere vuoto")
    @Size(min = 3, max = 20, message = "La lunghezza del nome deve essere compresa tra 3 e 20 caratteri")
    @Column
    private String firstName;

    @NotBlank(message = "Il nome non può essere vuoto")
    @Size(min = 3, max = 20, message = "La lunghezza del nome deve essere compresa tra 3 e 20 caratteri")
    @Column
    private String lastName;

    @Column(columnDefinition = "DATE")
    private Date birthdate;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "libraries",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "game_id"),
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "game_id"})
    )
    @JsonManagedReference
    @JsonBackReference
    @JsonIgnore
    private Collection<Game> games;

    @OneToMany(mappedBy = "user")
    @JsonManagedReference
    @JsonBackReference
    @JsonIgnore
    private Collection<Wishlist> wishlists;

    @ManyToMany
    @JoinTable(
        name = "friends",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "friend_id"),
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "friend_id"})
    )
    @JsonManagedReference
    @JsonBackReference
    @JsonIgnore
    private Collection<User> friends;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "carts",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "game_id"),
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "game_id"})
    )
    @JsonManagedReference
    @JsonBackReference
    @JsonIgnore
    private Collection<Game> cartGames;

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.SHORT_PREFIX_STYLE)
                .append("id", id)
                .append("username", username)
                .append("email", email)
                .append("firstName", firstName)
                .append("lastName", lastName)
                .append("birthdate", birthdate)
                .toString();
    }
}