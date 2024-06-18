package unical.enterprise.jokibackend.Dto;

import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class KeycloakUserDTO {
    private String userName;
    private String emailId;
    private String password;
    private String firstname;
    private String lastName;
}