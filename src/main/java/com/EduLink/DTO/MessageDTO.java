package com.EduLink.DTO;

import com.EduLink.Models.Message;
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

    public MessageDTO(Message message) {
        this.id = message.getId();
        this.content = message.getContent();
        this.timestamp = message.getTimestamp();
        this.isRead = message.isRead();
        this.sender = new UserDTO(message.getSender().getId(), null, null, null); // Remplir les autres champs si disponibles
        this.recipient = message.getRecipient() != null ?
                new UserDTO(message.getRecipient().getId(), null, null, null) : null;
        this.groupId = message.getGroup() != null ? message.getGroup().getId() : null;
    }
}


