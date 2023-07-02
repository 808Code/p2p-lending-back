package com.bonsai.accountservice.models;

import com.bonsai.sharedservice.enums.NotificationType;
import lombok.*;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class UserNotification {
    @Id
    @GeneratedValue
    private UUID id;
    private String notificationMessage;
    private LocalDateTime notificationDate;
    private NotificationType notificationType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private UserCredential user;

}
