package unical.enterprise.jokibackend.Controller.v1;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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
    List<UUID> userIds = new ArrayList<> ();
    List<HashMap<String, String>> conversations = new ArrayList<> (); // lista delle conversazioni

    @MessageMapping("/ping")
    @SendTo("/topic/ping")
    public String ping(String message) {
        return "Pong: " + message;
    }

    // Invia un messaggio da un utente ad un admin
    @MessageMapping("/chat/user-to-admin/{userId}/{adminId}") // ricevo da
    @SendTo("/topic/chat/{adminId}_{userId}") // invio a
    public Map<String, Object> sendMessageToAdmin(Map<String, Object> message, @DestinationVariable String userId, @DestinationVariable String adminId) {
        message.put("timestamp", LocalDateTime.now().toString()); // Aggiungi timestamp
        return message;
    }

    // Invia un messaggio da un admin ad un utente
    @MessageMapping("/chat/admin-to-user/{userId}/{adminId}") // ricevo da
    @SendTo("/topic/chat/{userId}_{adminId}") // invio a
    public Map<String, Object> sendMessageToUser(Map<String, Object> message, @DestinationVariable String userId, @DestinationVariable String adminId) {
        message.put("timestamp", LocalDateTime.now().toString()); // Aggiungi timestamp
        return message;
    }


    // Aggiunge un admin alla lista di quelli disponibili
    @PostMapping("/chat/admin")
    public ResponseEntity<Map<String, Object>> addAdmin() {
        UUID adminId = UserContextHolder.getContext().getId();
        adminIds.add(adminId);
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Admin aggiunto con ID: " + adminId);
        response.put("adminId", adminId.toString());
        
        return ResponseEntity.ok(response);
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
    public ResponseEntity<Map<String, String>> getAvailableAdmin() {
        if (adminIds.isEmpty()) {
            return ResponseEntity.status(404).body(Collections.singletonMap("error", "Nessun admin disponibile al momento."));
        }
        UUID firstAdminId = adminIds.get(0); // Prende il primo admin dalla lista
        // adminIds.remove(0); // Rimuove l'admin dalla lista per evitare che venga selezionato da più utenti
        // Crea una mappa per la risposta JSON
        Map<String, String> response = new HashMap<>();
        response.put("adminId", firstAdminId.toString());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/chat/user")
    public ResponseEntity<Map<String, Object>> addUser() {
        UUID userId = UserContextHolder.getContext().getId(); // Prende l'id dell'utente
        userIds.add(userId); // Aggiunge l'utente alla lista
        
        // Crea una mappa per la risposta JSON
        Map<String, Object> response = new HashMap<>();
        response.put("message", "Utente aggiunto con ID: " + userId);
        response.put("userId", userId.toString());
        
        return ResponseEntity.ok(response);
    }    

    @PostMapping("/chat/user/remove")
    public ResponseEntity<String> removeUser(@RequestBody String id) {
        UUID userId = UUID.fromString(id); // Converte l'id in UUID
        if (userIds.remove(userId)) {
            return ResponseEntity.ok("Utente rimosso con ID: " + userId);
        } else {
            return ResponseEntity.status(404).body("Utente non trovato con ID: " + userId);
        }
    }

    @GetMapping("/chat/user")
    public ResponseEntity<Map<String, String>> getUser() {
        if (userIds.isEmpty()) {
            Map<String, String> response = new HashMap<>();
            response.put("error", "Nessun utente disponibile al momento.");
            return ResponseEntity.status(404).body(response);
        }
        UUID firstUserId = userIds.get(0); // Prende il primo utente disponibile
        // userIds.remove(0); // Rimuove l'utente dalla lista per evitare che venga selezionato di nuovo
        Map<String, String> response = new HashMap<>();
        response.put("userId", firstUserId.toString()); // Restituisce l'ID come risposta
        return ResponseEntity.ok(response);
    }
    
}