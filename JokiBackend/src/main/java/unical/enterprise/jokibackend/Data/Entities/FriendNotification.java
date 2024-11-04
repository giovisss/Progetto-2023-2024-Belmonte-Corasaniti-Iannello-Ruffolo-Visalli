package unical.enterprise.jokibackend.Data.Entities;

import lombok.*;
import unical.enterprise.jokibackend.Utility.NotificationStatus;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@ToString
public class FriendNotification {

    private NotificationStatus status;
    private String newFriendUsername;
    private String message;

}
