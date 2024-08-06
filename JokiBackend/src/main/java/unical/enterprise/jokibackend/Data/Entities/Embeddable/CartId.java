package unical.enterprise.jokibackend.Data.Entities.Embeddable;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.UUID;

@Data
@Embeddable
@AllArgsConstructor
@NoArgsConstructor
public class CartId implements Serializable {
    private UUID userId;
    private UUID gameId;
}