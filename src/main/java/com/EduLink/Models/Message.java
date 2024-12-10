package com.EduLink.Models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document(collection = "messages")
public class Message {
    @Id
    private String id;

    @DBRef
    private User sender; // Expéditeur du message

    @DBRef
    private User recipient; // Destinataire du message (null si message de groupe)

    private String content;
    private long timestamp;
    private boolean isRead;

    @DBRef
    private Group group; // Groupe associé (null pour un message privé)
}

