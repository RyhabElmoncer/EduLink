package com.EduLink.DTO;

import lombok.Data;

import java.util.List;

@Data
public class PublicationDTO {
    private String id;
    private String userId;            // ID de l'utilisateur
    private String textContent;       // Contenu textuel
    private List<String> tags;        // Liste des hashtags
    private String imageUrl;          // URL de l'image jointe (facultatif)
    private List<String> likes;       // Liste des IDs des utilisateurs ayant liké
    private long timestamp;
    private String firstName;
    private String lastName;
    private List<CommentDTO> comments; // Ajout de la liste des commentaires


}
