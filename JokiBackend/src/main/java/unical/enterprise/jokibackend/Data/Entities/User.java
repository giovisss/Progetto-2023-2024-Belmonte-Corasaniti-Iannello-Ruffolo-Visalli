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