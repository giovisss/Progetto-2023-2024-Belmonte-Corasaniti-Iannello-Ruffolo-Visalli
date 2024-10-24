package unical.enterprise.jokibackend.Data.Services;

import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import unical.enterprise.jokibackend.Data.Dao.UserDao;
import unical.enterprise.jokibackend.Data.Dto.KeycloakUserDTO;
import unical.enterprise.jokibackend.Data.Dto.UpdateUserDto;
import unical.enterprise.jokibackend.Data.Dto.UserDto;
import unical.enterprise.jokibackend.Data.Entities.User;
import unical.enterprise.jokibackend.Data.Services.Interfaces.KeycloakService;
import unical.enterprise.jokibackend.Data.Services.Interfaces.UserService;
import unical.enterprise.jokibackend.Exceptions.NotModifiedException;
import unical.enterprise.jokibackend.Utility.KeycloakManager;

import java.util.Arrays;
import java.util.Collections;
import java.util.UUID;

@AllArgsConstructor
@Service
public class KeycloakServiceImpl implements KeycloakService {
    private final UserDao userDao;
    private final ModelMapper modelMapper;
    private final UserService userService;

    @Override
    @Transactional
    public User addUser(KeycloakUserDTO userDTO){
        CredentialRepresentation credential = createPasswordCredentials(userDTO.getPassword());
        UserRepresentation user = new UserRepresentation();
        user.setUsername(userDTO.getUsername());
        user.setFirstName(userDTO.getFirstname());
        user.setLastName(userDTO.getLastName());
        user.setEmail(userDTO.getEmailId());
        user.setCredentials(Collections.singletonList(credential));
        user.setEnabled(true);

        user.setEmailVerified(true);

        UsersResource instance = getInstance();

        //se andata a buon fine, aggiungi user al db locale
        try {
            var response = instance.create(user);
            if(response.getStatus() == 201){
                var tmp = getUser(user.getUsername());
                return createLocalUser(tmp);
            }
        } catch (Exception e){
            // deleteUser(getUser(user.getUsername()).getId());
            // userDao.deleteByUsername(user.getUsername());
        }

        return null;
    }

    private User createLocalUser(UserRepresentation userRepresentation) {
        UserDto userDto = new UserDto();
        userDto.setId(UUID.fromString(userRepresentation.getId()));
        userDto.setUsername(userRepresentation.getUsername());
        userDto.setEmail(userRepresentation.getEmail());
        userDto.setFirstName(userRepresentation.getFirstName());
        userDto.setLastName(userRepresentation.getLastName());
        return userDao.save(modelMapper.map(userDto, User.class));
    }


    @Override
    @Transactional
    public Boolean updateUser(String username, UpdateUserDto userDTO){
        if (!username.equals(userDTO.getUsername().toLowerCase())) return false;

        UserDto old = userService.getUserByUsername(username);
        UserRepresentation user = new UserRepresentation();

        if(
                old.getEmail().equals(userDTO.getEmail()) ||
                old.getFirstName().equals(userDTO.getFirstName()) ||
                old.getLastName().equals(userDTO.getLastName()) ||
                old.getBirthdate().equals(userDTO.getBirthdate())
        ) throw new NotModifiedException("Field can not be the same as before");

        //TODO: fare in modo che si possa fare l'update su più campi alla volta
//        user.setFirstName(userDTO.getFirstName());
//        user.setLastName(userDTO.getLastName());
//        user.setEmail(userDTO.getEmail());
//        user.setCredentials(Collections.singletonList(createPasswordCredentials(userDTO.getPassword())));
        if (!userDTO.getUpdateType("email")) userDTO.setEmail(old.getEmail());
        user.setEmail(userDTO.getEmail());

        if (!userDTO.getUpdateType("firstName")) userDTO.setFirstName(old.getFirstName());
        user.setFirstName(userDTO.getFirstName());

        if (!userDTO.getUpdateType("lastName")) userDTO.setLastName(old.getLastName());
        user.setLastName(userDTO.getLastName());

        if (!userDTO.getUpdateType("birthdate")) userDTO.setBirthdate(old.getBirthdate());

        if (userDTO.getUpdateType("password")) user.setCredentials(Collections.singletonList(createPasswordCredentials(userDTO.getPassword())));

        UsersResource usersResource = getInstance();
        usersResource.get(userService.getUserByUsername(username).getId().toString()).update(user);

        return userService.updateUser(username, userDTO);
    }

    @Override
    public void deleteUser(String username){
        UsersResource usersResource = getInstance();
        usersResource.get(username).remove();
        userService.deleteByUsername(username);
    }

    @Override
    public void sendVerificationLink(String userId){
        UsersResource usersResource = getInstance();
        usersResource.get(userId)
                .sendVerifyEmail();
    }

    @Override
    public UserRepresentation getUser(String username) {
        UsersResource usersResource = KeycloakManager.getUsersResource();
        return usersResource.search(username, true).get(0);  // Cerca l'utente per username
    }

    @Override
    public void sendResetPassword(String userId) {
        RealmResource realmResource = KeycloakManager.getRealmResource();  // Get the Realm
        UserResource userResource = realmResource.users().get(userId);
        UserRepresentation userRepresentation = userResource.toRepresentation();
        userRepresentation.setRequiredActions(Arrays.asList("UPDATE_PASSWORD"));  // Set the required action for password reset
        userResource.update(userRepresentation);  // Update the user
    }

    @Override
    public UsersResource getInstance(){
        return KeycloakManager.getInstance().realm(KeycloakManager.realm).users();
    }

    //Funzione di Logout
    //TODO: Non serve per il frontend, rimuoverla se non serve nemmeno per Android
    @Override
    public void logoutUser(String username) {
        UsersResource usersResource = getInstance();
        usersResource.get(username).logout();
    }

    private CredentialRepresentation createPasswordCredentials(String password) {
        CredentialRepresentation passwordCredentials = new CredentialRepresentation();
        passwordCredentials.setTemporary(false);
        passwordCredentials.setType(CredentialRepresentation.PASSWORD);
        passwordCredentials.setValue(password);
        return passwordCredentials;
    }
}