package unical.enterprise.jokibackend.Service;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.representations.idm.RoleRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


//AL MOMENTO COMMENTATO IN USER CONTROLLER
//NON HA ALTRI USI PER ORA

@Service
public class KeycloakAdminService {

    @Value("${keycloak.auth-server-url}")
    private String authServerUrl;

    @Value("${keycloak.realm}")
    private String realm;

    @Value("${keycloak.resource}")
    private String clientId;

    @Value("${keycloak.credentials.secret}")
    private String clientSecret;

    @Value("${keycloak-admin.username}")
    private String adminUsername;

    @Value("${keycloak-admin.password}")
    private String adminPassword;

    private Keycloak getInstance() {
        System.out.println("Auth server URL: " + authServerUrl);
        System.out.println("Realm: " + realm);

        return KeycloakBuilder.builder()
                .serverUrl(authServerUrl)
                .realm(realm)
                .grantType(OAuth2Constants.PASSWORD)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .username(adminUsername)
                .password(adminPassword)
                .build();
    }

    public List<String> getUserRoles(String userId) {
        Keycloak keycloak = getInstance();
        System.out.println("Keycloak: " + keycloak.toString());
        RealmResource realmResource = keycloak.realm(realm);
        System.out.println(realmResource.toString());
        System.out.println(realmResource.roles().list().toString());

        List<RoleRepresentation> roles = realmResource.users().get(userId).roles().getAll().getRealmMappings();
        return roles.stream().map(RoleRepresentation::getName).collect(Collectors.toList());
    }
}
