package unical.enterprise.jokibackend.Component;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
public class UserPermissionEvaluator {

    // Aggiungi il tuo servizio o repository qui se necessario per verificare l'amicizia tra utenti

    public boolean isFriend(Authentication auth, Object targetUser) {
        System.out.println("Checking if " + auth.getName() + " is friend with " + targetUser.toString());
        return false; // Restituisci true se sono amici, false altrimenti
    }
}