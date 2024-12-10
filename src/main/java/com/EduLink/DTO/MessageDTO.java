package com.EduLink.DTO;

import lombok.Data;

@Data
public class MessageDTO {
    private String senderId;
    private String recipientId; // Ou null pour les messages de groupe
    private String groupId; // Groupe cible (null pour message privé)
    private String content;
}

