package unical.enterprise.jokibackend.Config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.security.core.session.SessionRegistryImpl;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.session.RegisterSessionAuthenticationStrategy;
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy;
import org.springframework.security.web.session.HttpSessionEventPublisher;
import org.springframework.web.bind.annotation.CrossOrigin;
import unical.enterprise.jokibackend.Component.JwtAuthConverter;
import javax.ws.rs.HttpMethod;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
class SecurityConfig {
    private final JwtAuthConverter jwtAuthConverter;
    private final String prefix="/api/*";

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorize -> authorize
                        .requestMatchers(prefix+"/admin/**").hasRole("client_admin")
//                        .requestMatchers(prefix+"/user/**").hasRole("client_user")
                        .requestMatchers(HttpMethod.GET, prefix+"/games/**").permitAll() // permetti tutte le richieste GET a /api/games/** */
                        .requestMatchers(HttpMethod.GET, prefix+"/reviews/**").permitAll() // Permetti tutte le GET a /reviews/**
                        .requestMatchers(HttpMethod.GET, "/images/**").permitAll() // Permette tutte le richieste GET a /api/images/{imageName:.+}
                        .anyRequest().hasRole("client_user")); // permetti tutte le richieste se autenticato
//                        .anyRequest().authenticated()); // permetti tutte le richieste se autenticato
//                        .anyRequest().permitAll()); // permetti tutte le richieste se autenticato
        http
                .oauth2ResourceServer(oauth2 ->
                        oauth2.jwt(Customizer.withDefaults()).jwt(
                                jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter)
                        )
                );

        http.oauth2Login(Customizer.withDefaults());

        http
                .sessionManagement(sessionManagement -> sessionManagement
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    // TODO: verificare utilità bean (no logout)

    @Bean
    public SessionRegistry sessionRegistry() {
        return new SessionRegistryImpl();
    }

    @Bean
    protected SessionAuthenticationStrategy sessionAuthenticationStrategy() {
        return new RegisterSessionAuthenticationStrategy(sessionRegistry());
    }

    @Bean
    public HttpSessionEventPublisher httpSessionEventPublisher() {
        return new HttpSessionEventPublisher();
    }


    @Bean
    public ClientRegistrationRepository clientRegistrationRepository() {
        return new InMemoryClientRegistrationRepository(this.keycloakClientRegistration());
    }

    private ClientRegistration keycloakClientRegistration() {
        return ClientRegistration.withRegistrationId("keycloak")
                .clientId("JokiBackend")
                .clientSecret("BAAHnEU37J7LX0Do6sRcsDN9IZNVs20g")
                .scope("openid")
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationUri("http://localhost:8080/auth/realms/JokiRealm/protocol/openid-connect/auth")
                .tokenUri("http://localhost:8080/auth/realms/JokiRealm/protocol/openid-connect/token")
                .userInfoUri("http://localhost:8080/auth/realms/JokiRealm/protocol/openid-connect/userinfo")
                .redirectUri("http://localhost:8081/login/oauth2/code/keycloak")
                .build();
    }
}


//@Configuration
//@EnableWebSecurity
//@EnableMethodSecurity
//@RequiredArgsConstructor
//@CrossOrigin(origins = "*")
//public class SecurityConfig extends AbstractHttpConfigurer<SecurityConfig, HttpSecurity> {
//
//    private final JwtAuthConverter jwtAuthConverter;
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        http
//                .csrf(AbstractHttpConfigurer::disable)
//                .authorizeHttpRequests(authorize -> authorize
//                        .requestMatchers("/tmp/admin/**").hasRole("client_admin")
//                        .requestMatchers("/api/auth/**").permitAll() // permetti tutte le richieste a /api/auth/** */
//                        .requestMatchers(HttpMethod.GET, "/api/games/**").permitAll() // permetti tutte le richieste GET a /api/games/** */
//                        .anyRequest().authenticated()); // permetti tutte le richieste se autenticato
//
//        http
//                .oauth2ResourceServer(oauth2 ->
//                        oauth2.jwt(Customizer.withDefaults()).jwt(
//                                jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter)
//                        )
//                );
//
//        http
//                .sessionManagement(sessionManagement -> sessionManagement
//                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));
//
//        return http.build();
//    }
//}









//    autorizzazione delle richieste per gli utenti in base ai loro ruoli
//    private static final String GROUPS = "groups";
//    private static final String REALM_ACCESS_CLAIM = "realm_access";
//    private static final String ROLES_CLAIM = "roles";
//    private final LogoutHandler keycloakLogoutHandler;

//    @Bean
//    public SecurityFilterChain resourceServerFilterChain(HttpSecurity http) throws Exception {
//        http.authorizeHttpRequests(auth -> auth
//                .requestMatchers("/tmp/admin/**").hasRole("admin")
//                .requestMatchers("/tmp/user/**").hasRole("user")
////                .requestMatchers("/").permitAll()
//                .anyRequest().authenticated());
//        http.oauth2ResourceServer((oauth2) -> oauth2
//                .jwt(Customizer.withDefaults()));
//        http.oauth2Login(Customizer.withDefaults())
//                .logout(logout -> logout.addLogoutHandler(keycloakLogoutHandler).logoutSuccessUrl("/"));
//        return http.build();
//    }
//
//    @Bean
//    public GrantedAuthoritiesMapper userAuthoritiesMapperForKeycloak() {
//        return authorities -> {
//            Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
//            var authority = authorities.iterator().next();
//            boolean isOidc = authority instanceof OidcUserAuthority;
//
//            System.out.println("isOidc: " + isOidc);
//            if (isOidc) {
//                var oidcUserAuthority = (OidcUserAuthority) authority;
//                var userInfo = oidcUserAuthority.getUserInfo();
//
//                // Tokens can be configured to return roles under
//                // Groups or REALM ACCESS hence have to check both
//                if (userInfo.hasClaim(REALM_ACCESS_CLAIM)) {
//                    var realmAccess = userInfo.getClaimAsMap(REALM_ACCESS_CLAIM);
//                    var roles = (Collection<String>) realmAccess.get(ROLES_CLAIM);
//                    mappedAuthorities.addAll(generateAuthoritiesFromClaim(roles));
//                } else if (userInfo.hasClaim(GROUPS)) {
//                    Collection<String> roles = (Collection<String>) userInfo.getClaim(
//                            GROUPS);
//                    mappedAuthorities.addAll(generateAuthoritiesFromClaim(roles));
//                }
//            } else {
//                var oauth2UserAuthority = (OAuth2UserAuthority) authority;
//                Map<String, Object> userAttributes = oauth2UserAuthority.getAttributes();
//
//                if (userAttributes.containsKey(REALM_ACCESS_CLAIM)) {
//                    Map<String, Object> realmAccess = (Map<String, Object>) userAttributes.get(
//                            REALM_ACCESS_CLAIM);
//                    Collection<String> roles = (Collection<String>) realmAccess.get(ROLES_CLAIM);
//                    mappedAuthorities.addAll(generateAuthoritiesFromClaim(roles));
//                }
//            }
//            mappedAuthorities.forEach(System.out::println);
//            return mappedAuthorities;
//        };
//    }
//
//    Collection<GrantedAuthority> generateAuthoritiesFromClaim(Collection<String> roles) {
//        return roles.stream().map(role -> new SimpleGrantedAuthority("ROLE_" + role)).collect(
//                Collectors.toList());
//    }
