package unical.enterprise.jokibackend.Data.Entities;

import jakarta.persistence.*;
import lombok.*;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Collection;
import java.util.UUID;

@Getter
@Setter
@Entity(name = "wishlists")
@AllArgsConstructor
@NoArgsConstructor
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "wishlist_name"})
)
public class Wishlist {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column
    private String wishlistName;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(columnDefinition = "int default 0")
    private Integer visibility;


    @ManyToMany
    @JoinTable(
        name = "wishlist_games",
        joinColumns = @JoinColumn(name = "wishlist_id"),
        inverseJoinColumns = @JoinColumn(name = "game_id"),
        uniqueConstraints = @UniqueConstraint(columnNames = {"wishlist_id", "game_id"})
    )
    private Collection<Game> games;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this, ToStringStyle.SHORT_PREFIX_STYLE);
    }

}