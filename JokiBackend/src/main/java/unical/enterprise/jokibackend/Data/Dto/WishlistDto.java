package unical.enterprise.jokibackend.Data.Dto;

import java.util.Collection;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import unical.enterprise.jokibackend.Data.Dto.GameDto;
import unical.enterprise.jokibackend.Data.Entities.User;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WishlistDto {
    private UUID id;
    private String wishlistName;
    private User user;
    private Collection<GameDto> game;
    private Integer visibility;
}
