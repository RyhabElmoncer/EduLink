package com.EduLink.DTO;

import lombok.Data;

import java.util.List;

@Data
public class PublicationDTO {
    private String id;                // ID de la publication
    private String userId;            // ID de l'utilisateur
    private String textContent;       // Contenu textuel
    private List<String> tags;        // Liste des hashtags
    private String imageUrl;          // URL de l'image jointe (facultatif)
    private List<String> likes;       // Liste des IDs des utilisateurs ayant liké
    private long timestamp;
    // Date de création ou mise à

}
