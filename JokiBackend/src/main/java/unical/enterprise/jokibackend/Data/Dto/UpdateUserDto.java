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

    public String getUpdateType() {
        if (email != null) return "email";
        if (password != null) return "password";
        if (firstName != null) return "firstName";
        if (lastName != null) return "lastName";
        if (birthdate != null) return "birthdate";
        return null;
    }
}
