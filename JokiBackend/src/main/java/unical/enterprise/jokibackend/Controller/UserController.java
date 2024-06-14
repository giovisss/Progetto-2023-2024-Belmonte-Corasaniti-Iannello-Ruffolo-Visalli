package unical.enterprise.jokibackend.Controller;

import com.nimbusds.jose.proc.SecurityContext;
import lombok.Getter;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;
import java.util.Collection;
import java.util.logging.Logger;

@RestController
@CrossOrigin(origins = "*")
public class UserController {
    private final GrantedAuthoritiesMapper userAuthoritiesMapperForKeycloak;
    Logger logger = Logger.getLogger(UserController.class.getName());

    public UserController(GrantedAuthoritiesMapper userAuthoritiesMapperForKeycloak) {
        this.userAuthoritiesMapperForKeycloak = userAuthoritiesMapperForKeycloak;
    }

    //    @PreAuthorize("hasAnyRole('BLOCCA', 'ROLE_BLOCCA') or hasAnyAuthority('BLOCCA', 'ROLE_BLOCCA')")
    @GetMapping("/user")
    public String getAuthenticatedUser(Authentication authentication) {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Collection<? extends GrantedAuthority> mappedAuthorities = userAuthoritiesMapperForKeycloak.mapAuthorities(authorities);

        logger.info(mappedAuthorities.toString());

        return "HA ACCESSO";
    }
}
