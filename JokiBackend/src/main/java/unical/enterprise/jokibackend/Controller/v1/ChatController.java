package unical.enterprise.jokibackend.Controller.v1;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.http.ResponseEntity;

import unical.enterprise.jokibackend.Utility.CustomContextManager.UserContextHolder;

@Controller
@RequestMapping("/api/v1")
public class ChatController {

    List<UUID> adminIds = new ArrayList<> ();

    @MessageMapping("/ping")
    @SendTo("/topic/ping")
    public String ping(String message) {
        return "Pong: " + message;
    }

    // Invia un messaggio da un utente ad un admin
    @MessageMapping("/chat/user-to-admin/{userId}/{adminId}") // ricevo da
    @SendTo("/topic/chat/{adminId}_{userId}") // rispondo a
    public Map<String, Object> sendMessageToAdmin(Map<String, Object> message, @DestinationVariable String userId, @DestinationVariable String adminId) {
        message.put("timestamp", LocalDateTime.now().toString()); // Aggiungi timestamp
        return message;
    }

    // Invia un messaggio da un admin ad un utente
    @MessageMapping("/chat/admin-to-user/{userId}/{adminId}") // ricevo da
    @SendTo("/topic/chat/{userId}_{adminId}") // rispondo a
    public Map<String, Object> sendMessageToUser(Map<String, Object> message, @DestinationVariable String userId, @DestinationVariable String adminId) {
        message.put("timestamp", LocalDateTime.now().toString()); // Aggiungi timestamp
        return message;
    }


    // Aggiunge un admin alla lista di quelli disponibili
    @PostMapping("/chat/admin")
    public ResponseEntity<String> addAdmin() {
        UUID adminId = UserContextHolder.getContext().getId(); // Prende l'id dell'admin
        adminIds.add(adminId); // Aggiunge l'admin alla lista
        return ResponseEntity.ok("Admin aggiunto con ID: " + adminId);
    }

    // Rimuove un admin dalla lista di quelli disponibili
    @PostMapping("/chat/admin/remove")
    public ResponseEntity<String> removeAdmin(@RequestBody String id) {
        UUID adminId = UUID.fromString(id); // Converte l'id in UUID
        if (adminIds.remove(adminId)) {
            return ResponseEntity.ok("Admin rimosso con ID: " + adminId);
        } else {
            return ResponseEntity.status(404).body("Admin non trovato con ID: " + adminId);
        }
    }

    // Restituisce l'ID del primo admin disponibile
    @GetMapping("/chat/admin")
    public ResponseEntity<String> getAvailableAdmin() {
        if (adminIds.isEmpty()) {
            return ResponseEntity.status(404).body("Nessun admin disponibile al momento.");
        }
        UUID firstAdminId = adminIds.get(0); // Prende il primo admin dalla lista
        adminIds.remove(0); // Rimuove l'admin dalla lista per evitare che venga selezionato da più utenti
        return ResponseEntity.ok(firstAdminId.toString());
    }
}