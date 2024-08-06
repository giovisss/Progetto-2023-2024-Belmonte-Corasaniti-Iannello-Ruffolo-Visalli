package unical.enterprise.jokibackend.Data.Entities.Embeddable;

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class WishlistGamesId implements Serializable {
    private UUID wishlistId;
    private UUID gameId;
}