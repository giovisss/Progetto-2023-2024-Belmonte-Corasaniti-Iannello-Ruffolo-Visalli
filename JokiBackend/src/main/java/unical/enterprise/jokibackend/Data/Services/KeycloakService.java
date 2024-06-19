package unical.enterprise.jokibackend.Data.Services;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.AllArgsConstructor;
import unical.enterprise.jokibackend.Config.KeycloakConfig;
import unical.enterprise.jokibackend.Data.Dao.UserDao;
import unical.enterprise.jokibackend.Data.Entities.User;
import unical.enterprise.jokibackend.Dto.KeycloakUserDTO;
import unical.enterprise.jokibackend.Dto.UserDto;

@AllArgsConstructor
@Service
public class KeycloakService {
    @Autowired
    UserDao userDao;

    @Autowired
    ModelMapper modelMapper;

    public void addUser(KeycloakUserDTO userDTO){
        CredentialRepresentation credential = createPasswordCredentials(userDTO.getPassword());
        UserRepresentation user = new UserRepresentation();
        user.setUsername(userDTO.getUserName());
        user.setFirstName(userDTO.getFirstname());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmailId());
        user.setCredentials(Collections.singletonList(credential));
        user.setEnabled(true);

        user.setEmailVerified(true);

        UsersResource instance = getInstance();

        var response = instance.create(user);
        
        //se andata a buon fine, aggiungi user al db locale
        try {
        if(response.getStatus() == 201){
            var tmp = getUser(user.getUsername()).get(0);
            createLocalUser(tmp);
        }
        } catch (Exception e) {
            System.out.println("Errore: " + e.getMessage());
        }
    }

    private User createLocalUser(UserRepresentation userRepresentation) {
        UserDto userDto = new UserDto();
        userDto.setId(UUID.fromString(userRepresentation.getId()));
        userDto.setUsername(userRepresentation.getUsername());
        userDto.setEmail(userRepresentation.getEmail());
        userDto.setName(userRepresentation.getFirstName());
        userDto.setSurname(userRepresentation.getLastName());
        return userDao.save(modelMapper.map(userDto, User.class));
    }


    public List<UserRepresentation> getUser(String userName){
        UsersResource usersResource = getInstance();
        return usersResource.search(userName, true);
    }


    public void updateUser(String userId, KeycloakUserDTO userDTO){
        CredentialRepresentation credential = createPasswordCredentials(userDTO.getPassword());
        UserRepresentation user = new UserRepresentation();
        user.setUsername(userDTO.getUserName());
        user.setFirstName(userDTO.getFirstname());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmailId());
        user.setCredentials(Collections.singletonList(credential));

        UsersResource usersResource = getInstance();
        usersResource.get(userId).update(user);
    }


    public void deleteUser(String userId){
        UsersResource usersResource = getInstance();
        usersResource.get(userId)
                .remove();
    }


    public void sendVerificationLink(String userId){
        UsersResource usersResource = getInstance();
        usersResource.get(userId)
                .sendVerifyEmail();
    }


    public void sendResetPassword(String userId){
        UsersResource usersResource = getInstance();

        usersResource.get(userId)
                .executeActionsEmail(Arrays.asList("UPDATE_PASSWORD"));
    }


    public UsersResource getInstance(){
        return KeycloakConfig.getInstance().realm(KeycloakConfig.realm).users();
    }


    private CredentialRepresentation createPasswordCredentials(String password) {
        CredentialRepresentation passwordCredentials = new CredentialRepresentation();
        passwordCredentials.setTemporary(false);
        passwordCredentials.setType(CredentialRepresentation.PASSWORD);
        passwordCredentials.setValue(password);
        return passwordCredentials;
    }
}