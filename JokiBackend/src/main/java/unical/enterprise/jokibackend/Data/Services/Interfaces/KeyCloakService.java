//package unical.enterprise.jokibackend.Data.Services.Interfaces;
//
//import org.keycloak.admin.client.resource.UsersResource;
//import org.keycloak.representations.idm.UserRepresentation;
//
//import unical.enterprise.jokibackend.Data.Dto.KeycloakUserDTO;
//import unical.enterprise.jokibackend.Data.Dto.UpdateUserDto;
//import unical.enterprise.jokibackend.Data.Entities.User;
//
//public interface KeyCloakService {
//    public User addUser(KeycloakUserDTO userDTO);
//    public UserRepresentation getUser(String userName);
//    public Boolean updateUser(String userId, UpdateUserDto userDTO);
//    public void deleteUser(String username);
//    public void sendVerificationLink(String userId);
//    public void sendResetPassword(String userId);
//    public UsersResource getInstance();
//}
