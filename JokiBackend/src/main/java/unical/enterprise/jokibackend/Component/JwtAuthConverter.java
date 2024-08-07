package unical.enterprise.jokibackend.Component;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.lang.NonNull;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.stereotype.Component;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import java.text.ParseException;
import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
public class JwtAuthConverter implements Converter<Jwt, AbstractAuthenticationToken> {

    @Value("${jwt.auth.converter.resource-id}")
    private String resourceId;

    @Value("${jwt.auth.converter.principle-attribute}")
    private String principleAttribute;

    private final JwtGrantedAuthoritiesConverter jwtGrantedAuthoritiesConverter = new JwtGrantedAuthoritiesConverter();

    @Override
    public AbstractAuthenticationToken convert(@NonNull Jwt jwt) {
        Collection<GrantedAuthority> authorities = Stream.concat(
                jwtGrantedAuthoritiesConverter.convert(jwt).stream(),
                extractResourceRoles(jwt).stream()
        ).collect(Collectors.toSet());

        // Estrazione e stampa dei dati del token
        try {
            extractAndPrintTokenDetails(jwt.getTokenValue());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return new JwtAuthenticationToken(jwt, authorities, getPricipleClaimName(jwt));
    }

    private String getPricipleClaimName(Jwt jwt) {
        String claimName = JwtClaimNames.SUB;

        // commentare se si vuole usare l'id invece del nome
        if (principleAttribute != null) {
            claimName = principleAttribute;
        }

        return jwt.getClaim(claimName);
    }

    private Collection<? extends GrantedAuthority> extractResourceRoles(Jwt jwt) {
        Map<String, Object> resourceAccess;
        Map<String, Object> resource;
        Collection<String> resourceRoles;

        if (jwt.getClaim("resource_access") == null) return Set.of();

        resourceAccess = jwt.getClaim("resource_access");

        if (resourceAccess.get(resourceId) == null) return Set.of();

        resource = (Map<String, Object>) resourceAccess.get(resourceId);

        resourceRoles = (Collection<String>) resource.get("roles");

        return resourceRoles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role))
                .collect(Collectors.toSet());
    }

    private void extractAndPrintTokenDetails(String token) throws ParseException {
        SignedJWT signedJWT = SignedJWT.parse(token);
        JWTClaimsSet claims = signedJWT.getJWTClaimsSet();

        String scope = claims.getStringClaim("scope");
        String sid = claims.getStringClaim("sid");
        Boolean emailVerified = claims.getBooleanClaim("email_verified");
        String name = claims.getStringClaim("name");
        String preferredUsername = claims.getStringClaim("preferred_username");
        String givenName = claims.getStringClaim("given_name");
        String familyName = claims.getStringClaim("family_name");
        String email = claims.getStringClaim("email");

        System.out.println("Scope: " + scope);
        System.out.println("SID: " + sid);
        System.out.println("Email Verified: " + emailVerified);
        System.out.println("Name: " + name);
        System.out.println("Preferred Username: " + preferredUsername);
        System.out.println("Given Name: " + givenName);
        System.out.println("Family Name: " + familyName);
        System.out.println("Email: " + email);
    }
}
