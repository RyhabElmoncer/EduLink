package com.EduLink.controller;

import com.EduLink.DTO.CommentDTO;
import com.EduLink.DTO.PublicationDTO;
import com.EduLink.service.PublicationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth/publications")
public class PublicationController {

    @Autowired
    private PublicationService publicationService;

    // Créer une publication
    @PostMapping("/create")
    public ResponseEntity<PublicationDTO> createPublication(
            @RequestPart(value = "image", required = false) MultipartFile image,
            @RequestParam("textContent") String textContent,
            @RequestParam("tags") List<String> tags, // Modification ici
            @RequestParam("idUser") String idUser) {

        PublicationDTO publicationDTO = new PublicationDTO();
        publicationDTO.setTextContent(textContent);
        publicationDTO.setTags(tags); // Utiliser la liste de tags

        PublicationDTO createdPublication = publicationService.createPublication(publicationDTO, image, idUser);
        return new ResponseEntity<>(createdPublication, HttpStatus.CREATED);
    }
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<PublicationDTO>> getPublicationsByUser(@PathVariable String userId) {
        List<PublicationDTO> publications = publicationService.getPublicationsByUserId(userId);
        return new ResponseEntity<>(publications, HttpStatus.OK);
    }

    // Mettre à jour une publication
    @PutMapping("/update/{id}")
    public ResponseEntity<PublicationDTO> updatePublication(
            @PathVariable String id,
            @RequestParam("image") MultipartFile image,
            @RequestParam("publicationDTO") String publicationDTOJson) {

        try {
            // Convert the publicationDTOJson string to a PublicationDTO object
            PublicationDTO publicationDTO = new ObjectMapper().readValue(publicationDTOJson, PublicationDTO.class);

            // Proceed with updating the publication
            PublicationDTO updatedPublication = publicationService.updatePublication(id, publicationDTO, image);

            return new ResponseEntity<>(updatedPublication, HttpStatus.OK);
        } catch (JsonProcessingException e) {
            // Handle the exception for invalid JSON processing
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);  // Return a 400 error if JSON is invalid
        } catch (Exception e) {
            // Catch other possible exceptions and return a generic error
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);  // 500 for other errors
        }
    }
    @PostMapping("/{publicationId}/comments")
    public PublicationDTO addCommentToPublication(
            @PathVariable String publicationId,
            @RequestBody CommentDTO commentDTO
    ) {
        return publicationService.addComment(publicationId, commentDTO);
    }

    // Supprimer un commentaire d'une publication
    @DeleteMapping("/{publicationId}/comments/{commentId}")
    public PublicationDTO deleteCommentFromPublication(
            @PathVariable String publicationId,
            @PathVariable String commentId
    ) {
        return publicationService.deleteComment(publicationId, commentId);
    }
    // Supprimer une publication
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deletePublication(@PathVariable String id) {
        publicationService.deletePublication(id);
        return new ResponseEntity<>("Publication supprimée avec succès", HttpStatus.NO_CONTENT);
    }
    @DeleteMapping("/{publicationId}/like/{userId}")
    public ResponseEntity<PublicationDTO> removeLike(@PathVariable String publicationId, @PathVariable String userId) {
        PublicationDTO updatedPublication = publicationService.removeLikeFromPublication(publicationId, userId);
        return ResponseEntity.ok(updatedPublication);
    }

    // Obtenir une publication par ID
    @GetMapping("/{id}")
    public ResponseEntity<PublicationDTO> getPublicationById(@PathVariable String id) {
        PublicationDTO publicationDTO = publicationService.getPublicationById(id);
        return new ResponseEntity<>(publicationDTO, HttpStatus.OK);
    }
    // Récupérer toutes les publications
    @GetMapping("/all")
    public ResponseEntity<List<PublicationDTO>> getAllPublications() {
        List<PublicationDTO> publications = publicationService.getAllPublications();
        return new ResponseEntity<>(publications, HttpStatus.OK);
    }

    // Ajouter un like à une publication
    @PostMapping("/{id}/like")
    public ResponseEntity<PublicationDTO> addLikeToPublication(@PathVariable String id, @RequestParam String userId) {
        PublicationDTO updatedPublication = publicationService.addLikeToPublication(id, userId);
        return new ResponseEntity<>(updatedPublication, HttpStatus.OK);
    }






}
