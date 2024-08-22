package unical.enterprise.jokibackend.Data.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.UUID;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private UUID id;
    private String username;
    private String email;
    private String firstName;
    private String lastName;
    private Date birthdate;
//    private WishlistDto wishlist;
//    private Collection<GameDto> games;
//    private Collection<GameDto> cartGames;
}
    // private Collection<CartDto> carts;
