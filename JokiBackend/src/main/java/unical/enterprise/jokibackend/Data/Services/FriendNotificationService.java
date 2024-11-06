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

    public void sendFriendNotification(String userId, FriendNotification notification){
        log.info("Starting to send notification...");
        log.info("UserID: {}", userId);
        log.info("Notification content: {}", notification);

        try {
            messagingTemplate.convertAndSendToUser(
                    userId,
                    "/notifications",
                    notification
            );
            log.info("Notification sent successfully");
        } catch (Exception e) {
            log.error("Error sending notification: ", e);
        }
    }
}
