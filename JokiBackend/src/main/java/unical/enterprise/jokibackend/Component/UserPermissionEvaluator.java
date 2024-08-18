package unical.enterprise.jokibackend.Component;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import unical.enterprise.jokibackend.Data.Services.Interfaces.UserService;

@Component
@RequiredArgsConstructor
public class UserPermissionEvaluator {
    private final UserService userService;

    public boolean isFriend(Authentication auth, Object targetUser) {
        // Implementa il controllo dell'amicizia tra utenti
        String user=auth.getName();
        String target=targetUser.toString();

        if(user.equals(target)) return true;

        return (userService.getFriendByUsername(user, target) != null) &&
                (userService.getFriendByUsername(target,user) != null);
    }

    public boolean canVisulize(Object targetUser) {
        // Implementa il controllo della visibilità del profilo
        return true;
    }
}