package unical.enterprise.jokibackend.Data.Dto;

import lombok.*;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
public class KeycloakUserDTO {
    private UUID userId;
    private String username;
    private String emailId;
    private String password;
    private String firstname;
    private String lastName;
}