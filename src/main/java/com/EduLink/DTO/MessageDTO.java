package com.EduLink.DTO;

import com.EduLink.Models.Message;
import com.EduLink.Models.User;
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

        // Expéditeur
        User sender = message.getSender();
        this.sender = new UserDTO(
                sender.getId(),
                sender.getFirstName(),
                sender.getLastName(),
                sender.getEmail(),
                sender.getBio(),
                sender.getProfileImage(),
                sender.getAddress(),
                sender.getFieldOfStudy(),
                sender.getRole().toString()
        );

        // Destinataire (si présent)
        User recipient = message.getRecipient();
        if (recipient != null) {
            this.recipient = new UserDTO(
                    recipient.getId(),
                    recipient.getFirstName(),
                    recipient.getLastName(),
                    recipient.getEmail(),
                    recipient.getBio(),
                    recipient.getProfileImage(),
                    recipient.getAddress(),
                    recipient.getFieldOfStudy(),
                    sender.getRole().toString()
            );
        } else {
            this.recipient = null;
        }

        // Groupe
        this.groupId = message.getGroup() != null ? message.getGroup().getId() : null;
    }
}
