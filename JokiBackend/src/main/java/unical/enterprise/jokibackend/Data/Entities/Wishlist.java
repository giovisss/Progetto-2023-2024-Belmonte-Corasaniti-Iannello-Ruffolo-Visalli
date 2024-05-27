package unical.enterprise.jokibackend.Data.Entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@Entity(name = "wishlists")
@AllArgsConstructor
@NoArgsConstructor
public class Wishlist {
    @Id
    @ManyToOne
    @JoinColumn
    private User user;
    
    @Id
    @ManyToOne
    @JoinColumn
    private Game game;

    @Column(columnDefinition = "int default 0")
    private Integer visibility;
}