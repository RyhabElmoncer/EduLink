package com.EduLink.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class NotificationDTO {
    private String id;
    private String message;
    private boolean isRead;
    private long timestamp;
    private UserDTO recipient;
}


