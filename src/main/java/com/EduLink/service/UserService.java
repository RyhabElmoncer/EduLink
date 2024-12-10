package com.EduLink.service;

import com.EduLink.DTO.UserDTO;
import com.EduLink.Models.User;
import com.EduLink.config.filestorage.FileStorageInterface;
import com.EduLink.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FileStorageInterface fileStorage;

    // Modifier les données de l'utilisateur
    public UserDTO updateUser(String userId, UserDTO userDTO) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setFirstName(userDTO.getFirstName());
            user.setLastName(userDTO.getLastName());
            user.setAddress(userDTO.getAddress());
            userRepository.save(user);
            return userDTO;
        } else {
            throw new RuntimeException("User not found");
        }
    }
    public User findByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé avec l'email : " + email));
    }
    // Ajouter une image de profil
    public String addProfileImage(String userId, MultipartFile image) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String imageUrl = fileStorage.saveFile(image);
            user.setProfileImage(imageUrl);
            userRepository.save(user);
            return imageUrl;
        } else {
            throw new RuntimeException("User not found");
        }
    }

    // Supprimer une image de profil
    public void deleteProfileImage(String userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String profileImage = user.getProfileImage();
            if (profileImage != null) {
                fileStorage.deleteStorageFile(profileImage);
                user.setProfileImage(null);
                userRepository.save(user);
            } else {
                throw new RuntimeException("No profile image to delete");
            }
        } else {
            throw new RuntimeException("User not found");
        }
    }

    // Supprimer un utilisateur
    public void deleteUser(String userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            String profileImage = user.getProfileImage();
            if (profileImage != null) {
                fileStorage.deleteStorageFile(profileImage);
            }
            userRepository.delete(user);
        } else {
            throw new RuntimeException("User not found");
        }
    }
}
