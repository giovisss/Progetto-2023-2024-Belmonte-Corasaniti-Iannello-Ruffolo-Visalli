package unical.enterprise.jokibackend.Controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Controller
public class ChatController {

    @MessageMapping("/ping")
    @SendTo("/topic/ping")
    public String ping(String message) {
        return "Pong: " + message;
    }
}