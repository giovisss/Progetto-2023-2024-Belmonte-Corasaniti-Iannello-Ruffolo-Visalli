package unical.enterprise.jokibackend.DTO;

import java.util.Date;
import java.util.UUID;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private UUID id;
    private String password;
    private String username;
    private String email;
    private String name;
    private String surname;
    private Date birthdate;
    private String address;
}
