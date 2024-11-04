package unical.enterprise.jokibackend.Data.Services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import unical.enterprise.jokibackend.Data.Entities.FriendNotification;

@Service
@RequiredArgsConstructor
@Slf4j
public class FriendNotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    public void sendFriendNotification(String username, FriendNotification notification){
        log.info("Sending notification to {} with payload {}", username, notification);
        messagingTemplate.convertAndSendToUser(
                username,
                "/friend-notification",
                notification
        );
    }
}
