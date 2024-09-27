package unical.enterprise.jokibackend.Data.Dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import unical.enterprise.jokibackend.Data.Entities.User;

import java.util.Collection;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WishlistDto {
    private UUID id;
    private String wishlistName;
    private Integer visibility;
    private Collection<GameDto> games;
    @JsonIgnore
    private User user;
}
