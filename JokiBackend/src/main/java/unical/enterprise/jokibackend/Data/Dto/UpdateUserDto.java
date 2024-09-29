package unical.enterprise.jokibackend.Data.Dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateUserDto {
    private String username;
    private String email;
    private String password;
    private String firstName;
    private String lastName;
    private Date birthdate;

    public boolean getUpdateType(String type) {
        return switch (type) {
            case "email" -> this.email != null;
            case "password" -> this.password != null;
            case "firstName" -> this.firstName != null;
            case "lastName" -> this.lastName != null;
            case "birthdate" -> this.birthdate != null;
            default -> false;
        };
    }

    public void setUsername(String username) {
        this.username = username.toLowerCase();
    }
}
