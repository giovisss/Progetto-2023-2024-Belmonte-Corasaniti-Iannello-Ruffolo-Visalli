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

    // sempre in minuscolo
    public void setUsername(String username) {
        this.username = username != null ? username.toLowerCase() : null;
    }
}
