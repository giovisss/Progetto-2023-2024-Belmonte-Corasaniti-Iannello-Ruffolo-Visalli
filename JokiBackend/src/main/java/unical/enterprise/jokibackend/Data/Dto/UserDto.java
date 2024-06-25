package unical.enterprise.jokibackend.Data.Dto;

import java.util.Collection;
import java.util.Date;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private WishlistDto wishlist;
    private Collection<CartDto> carts;
}
