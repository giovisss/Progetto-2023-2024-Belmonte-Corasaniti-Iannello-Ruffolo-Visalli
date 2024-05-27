package unical.enterprise.jokibackend.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import unical.enterprise.jokibackend.Data.Entities.Game;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WishlistDto {
    private String user;
    private Game game;
    private Integer visibility;
}
