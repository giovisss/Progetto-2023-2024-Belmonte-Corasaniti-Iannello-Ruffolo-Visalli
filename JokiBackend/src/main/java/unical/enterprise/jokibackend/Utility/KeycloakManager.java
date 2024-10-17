package unical.enterprise.jokibackend.Utility;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;

public class KeycloakManager {

    static Keycloak keycloak = null;

    final static String serverUrl = "http://localhost:8080/";
    public final static String realm = "JokiRealm";
    final static String clientId = "JokiBackend";
    final static String clientSecret = "BAAHnEU37J7LX0Do6sRcsDN9IZNVs20g";

    public static Keycloak getInstance() {
        if (keycloak == null) {
            keycloak = KeycloakBuilder.builder()
                    .serverUrl(serverUrl)
                    .realm(realm)
                    .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                    .clientId(clientId)
                    .clientSecret(clientSecret)
                    .resteasyClient(new ResteasyClientBuilder().connectionPoolSize(10).build())
                    .build();
        }
        return keycloak;
    }

    // Metodo per ottenere il Realm Resource
    public static RealmResource getRealmResource() {
        return getInstance().realm(realm);
    }

    // Metodo per ottenere la risorsa utenti
    public static UsersResource getUsersResource() {
        return getRealmResource().users();
    }

    // Metodo per ottenere una risorsa utente specifica
    public static UserResource getUserResource(String userId) {
        return getUsersResource().get(userId);
    }

    public static String getUserToken(String username, String password){
        var tmp = KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .grantType(OAuth2Constants.PASSWORD)
                .username(username)
                .password(password)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .resteasyClient(
                        new ResteasyClientBuilder().connectionPoolSize(10).build()
                ).build();

        String token = tmp.tokenManager().getAccessTokenString();
        tmp.close();

        return token;
    }

    public static Object getUserInfo(String token) {
        return getInstance().tokenManager().getAccessToken();
    }

    public static Object getAdminInfo(String token) {
        return getInstance().tokenManager().getAccessToken();
    }
}