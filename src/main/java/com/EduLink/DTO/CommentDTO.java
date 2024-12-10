package com.EduLink.DTO;

import lombok.Data;

@Data
public class CommentDTO {
    private String userId;            // ID de l'utilisateur
    private String text;              // Texte du commentaire
    private long timestamp;           // Date de création du commentaire
}
