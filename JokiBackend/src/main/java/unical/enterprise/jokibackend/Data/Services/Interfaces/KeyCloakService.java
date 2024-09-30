package unical.enterprise.jokibackend.Data.Services.Interfaces;

import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;

import unical.enterprise.jokibackend.Data.Dto.KeycloakUserDTO;
import unical.enterprise.jokibackend.Data.Dto.UpdateUserDto;
import unical.enterprise.jokibackend.Data.Entities.User;

public interface KeycloakService {
    User addUser(KeycloakUserDTO userDTO);
    UserRepresentation getUser(String userName);
    Boolean updateUser(String userId, UpdateUserDto userDTO);
    void deleteUser(String username);
    void sendVerificationLink(String userId);
    void sendResetPassword(String userId);
    UsersResource getInstance();
}
