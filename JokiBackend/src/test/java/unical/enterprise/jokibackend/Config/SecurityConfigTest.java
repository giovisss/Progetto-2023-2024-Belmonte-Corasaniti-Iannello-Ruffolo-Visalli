package unical.enterprise.jokibackend.Config;

import org.junit.jupiter.api.Test;
import org.keycloak.adapters.springsecurity.client.KeycloakRestTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
@ActiveProfiles("test")

class SecurityConfigTest {

    @LocalServerPort
    private int port;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private KeycloakRestTemplate keycloakRestTemplate;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testAccessWithoutAuthentication() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/secure/test"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }

    @Test
    public void testAccessWithAuthentication() throws Exception {
        // Simula l'autenticazione con un token valido
        ResponseEntity<String> response = restTemplate.withBasicAuth("user", "password")
                .getForEntity("http://localhost:" + port + "/secure/test", String.class);
        assert(response.getStatusCode()).equals(HttpStatus.OK);
    }

    @Test
    public void testAccessWithUnauthorizedRole() throws Exception {
        // Simula l'autenticazione con un ruolo non autorizzato
        mockMvc.perform(MockMvcRequestBuilders.get("/secure/test")
                        .header("Authorization", "Bearer invalid_token"))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
    }

    @Test
    public void testAccessWithAuthorizedRole() throws Exception {
        // Simula l'autenticazione con un ruolo autorizzato
        mockMvc.perform(MockMvcRequestBuilders.get("/secure/test")
                        .header("Authorization", "Bearer valid_token"))
                .andExpect(MockMvcResultMatchers.status().isOk());
    }

}