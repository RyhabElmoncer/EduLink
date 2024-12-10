package com.EduLink.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MessageDTO {
    private String id;
    private String content;
    private long timestamp;
    private boolean isRead;
    private UserDTO sender;
    private UserDTO recipient;
    private String groupId;
}


