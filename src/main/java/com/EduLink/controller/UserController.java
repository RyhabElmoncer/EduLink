package com.EduLink.controller;

import com.EduLink.DTO.UserDTO;
import com.EduLink.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/v1/auth/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PutMapping("/{userId}")
    public ResponseEntity<UserDTO> updateUser(
            @PathVariable String userId,
            @RequestBody UserDTO userDTO) {
        UserDTO updatedUser = userService.updateUser(userId, userDTO);
        return ResponseEntity.ok(updatedUser);
    }

    @PostMapping("/{userId}/profile-image")
    public ResponseEntity<String> addProfileImage(
            @PathVariable String userId,
            @RequestParam MultipartFile image) {
        String imageUrl = userService.addProfileImage(userId, image);
        return ResponseEntity.ok(imageUrl);
    }

    @DeleteMapping("/{userId}/profile-image")
    public ResponseEntity<Void> deleteProfileImage(@PathVariable String userId) {
        userService.deleteProfileImage(userId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteUser(@PathVariable String userId) {
        userService.deleteUser(userId);
        return ResponseEntity.noContent().build();
    }
}
