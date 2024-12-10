package com.EduLink.DTO;

import com.EduLink.Models.User;
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
    private String role;

    // Constructor accepting a User instance
    public UserDTO(User user) {
        this.id = user.getId();
        this.firstName = user.getFirstName();
        this.lastName = user.getLastName();
        this.email = user.getEmail();
        this.bio = user.getBio();
        this.profileImage = user.getProfileImage();
        this.address = user.getAddress();
        this.fieldOfStudy = user.getFieldOfStudy();
    }

    // Correct constructor for manual instantiation
    public UserDTO(String id, String firstName, String lastName, String email) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
    }
}
