package com.EduLink.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserDTO {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String bio;              // Biographie de l'utilisateur
    private String profileImage;     // URL de l'image de profil
    private String address;          // Adresse de l'utilisateur
    private String fieldOfStudy;     // Domaine d'études
    private String role;             // Rôle de l'utilisateur sous forme de chaîne de caractères
}
