package com.EduLink.service;

import com.EduLink.DTO.CommentDTO;
import com.EduLink.DTO.PublicationDTO;
import com.EduLink.Models.Comment;
import com.EduLink.Models.Publication;
import com.EduLink.Models.User;
import com.EduLink.config.filestorage.FileStorageInterface;
import com.EduLink.repository.PublicationRepository;
import com.EduLink.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PublicationService {

    @Autowired
    private PublicationRepository publicationRepository;

    @Autowired
    private FileStorageInterface fileStorageInterface;

    @Autowired
    private UserRepository userRepository;  // Ajout du service UserRepository pour récupérer l'utilisateur

    public PublicationDTO createPublication(PublicationDTO publicationDTO, MultipartFile image, String idUser) {
        if (publicationDTO == null) {
            throw new IllegalArgumentException("Le DTO de publication ne peut pas être nul");
        }

        // Récupérer l'utilisateur en fonction de l'idUser
        User user = userRepository.findById(idUser).orElseThrow(() ->
                new IllegalArgumentException("Utilisateur non trouvé pour l'ID : " + idUser));

        Publication publication = mapToEntity(publicationDTO);

        // Associer l'utilisateur à la publication
        publication.setUser(user);

        if (image != null && !image.isEmpty()) {
            String imageUrl = fileStorageInterface.saveFile(image);
            publication.setImageUrl(imageUrl);
        }

        publication.setTimestamp(System.currentTimeMillis());
        publication = publicationRepository.save(publication);

        return mapToDTO(publication);
    }

    public PublicationDTO updatePublication(String id, PublicationDTO publicationDTO, MultipartFile image) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("L'identifiant de publication ne peut pas être nul ou vide");
        }

        if (publicationDTO == null) {
            throw new IllegalArgumentException("Le DTO de publication ne peut pas être nul");
        }

        Optional<Publication> existingPublicationOpt = publicationRepository.findById(id);
        if (existingPublicationOpt.isPresent()) {
            Publication existingPublication = existingPublicationOpt.get();

            // Mettre à jour le contenu de la publication
            existingPublication.setTextContent(publicationDTO.getTextContent());
            existingPublication.setTags(publicationDTO.getTags());

            // Vérification et mise à jour de l'image si nécessaire
            if (image != null && !image.isEmpty()) {
                if (existingPublication.getImageUrl() != null) {
                    fileStorageInterface.deleteStorageFile(existingPublication.getImageUrl());
                }
                String imageUrl = fileStorageInterface.saveFile(image);
                existingPublication.setImageUrl(imageUrl);
            }

            // Mettre à jour le timestamp de la publication
            existingPublication.setTimestamp(System.currentTimeMillis());

            // Sauvegarder la publication mise à jour
            existingPublication = publicationRepository.save(existingPublication);

            return mapToDTO(existingPublication);
        }

        throw new IllegalArgumentException("Publication non trouvée pour l'identifiant fourni : " + id);
    }

    public void deletePublication(String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("L'identifiant de publication ne peut pas être nul ou vide");
        }

        Optional<Publication> publicationOpt = publicationRepository.findById(id);
        if (publicationOpt.isPresent()) {
            Publication publication = publicationOpt.get();
            if (publication.getImageUrl() != null) {
                fileStorageInterface.deleteStorageFile(publication.getImageUrl());
            }
            publicationRepository.deleteById(id);
        } else {
            throw new IllegalArgumentException("Publication non trouvée pour l'identifiant fourni : " + id);
        }
    }

    public PublicationDTO getPublicationById(String id) {
        if (id == null || id.isEmpty()) {
            throw new IllegalArgumentException("L'identifiant de publication ne peut pas être nul ou vide");
        }

        return publicationRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new IllegalArgumentException("Publication non trouvée pour l'identifiant fourni : " + id));
    }



    public PublicationDTO mapToDTO(Publication publication) {
        PublicationDTO publicationDTO = new PublicationDTO();
        publicationDTO.setId(publication.getId());
        publicationDTO.setTextContent(publication.getTextContent());
        publicationDTO.setTags(publication.getTags());
        publicationDTO.setImageUrl(publication.getImageUrl());
        publicationDTO.setTimestamp(publication.getTimestamp());
        publicationDTO.setLikes(publication.getLikes());  // Ajoutez les likes ici
        if (publication.getComments() != null) {
            List<CommentDTO> commentDTOs = publication.getComments().stream()
                    .map(comment -> new CommentDTO(
                            comment.getId(),
                            comment.getUserId(),
                            comment.getUserFirstName(),
                            comment.getUserLastName(),
                            comment.getText(),
                            comment.getTimestamp()))
                    .collect(Collectors.toList());
            publicationDTO.setComments(commentDTOs);
        }
        if (publication.getUser() != null) {
            publicationDTO.setUserId(publication.getUser().getId());
            publicationDTO.setFirstName(publication.getUser().getFirstName());
            publicationDTO.setLastName(publication.getUser().getLastName());
        }


        return publicationDTO;
    }


    private Publication mapToEntity(PublicationDTO publicationDTO) {
        Publication publication = new Publication();
        publication.setTextContent(publicationDTO.getTextContent());
        publication.setTags(publicationDTO.getTags());
        publication.setImageUrl(publicationDTO.getImageUrl());
        publication.setTimestamp(publicationDTO.getTimestamp());
        return publication;
    }
    public List<PublicationDTO> getAllPublications() {
        List<Publication> publications = publicationRepository.findAll();
        List<PublicationDTO> publicationDTOs = new ArrayList<>();
        for (Publication publication : publications) {
            if (publication.getUser() != null) { // Exclure les publications sans utilisateur
                publicationDTOs.add(mapToDTO(publication));
            }
        }
        return publicationDTOs;
    }


    public PublicationDTO addLikeToPublication(String id, String userId) {
        Publication publication = publicationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Publication non trouvée pour l'identifiant fourni : " + id));

        if (publication.getLikes() == null) {
            publication.setLikes(new ArrayList<>());
        }

        if (!publication.getLikes().contains(userId)) {
            publication.getLikes().add(userId);
            publication = publicationRepository.save(publication);
        }

        return mapToDTO(publication);
    }

    public PublicationDTO addCommentToPublication(String id, CommentDTO commentDTO) {
        Publication publication = publicationRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Publication non trouvée pour l'identifiant fourni : " + id));

        if (publication.getComments() == null) {
            publication.setComments(new ArrayList<>());
        }

        publication.getComments().add(mapCommentDTOToEntity(commentDTO));
        publication = publicationRepository.save(publication);

        return mapToDTO(publication);
    }
    public List<PublicationDTO> getPublicationsByUserId(String userId) {
        if (userId == null || userId.isEmpty()) {
            throw new IllegalArgumentException("L'identifiant de l'utilisateur ne peut pas être nul ou vide");
        }

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé pour l'ID : " + userId));

        List<Publication> publications = publicationRepository.findByUser(user);
        return publications.stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    private Comment mapCommentDTOToEntity(CommentDTO commentDTO) {
        Comment comment = new Comment();
        comment.setUserId(commentDTO.getUserId());
        comment.setText(commentDTO.getText());
        comment.setTimestamp(System.currentTimeMillis());
        return comment;
    }
    public PublicationDTO addComment(String publicationId, CommentDTO commentDTO) {
        Publication publication = publicationRepository.findById(publicationId)
                .orElseThrow(() -> new IllegalArgumentException("Publication non trouvée"));

        // Récupérer l'utilisateur qui commente
        User user = userRepository.findById(commentDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("Utilisateur non trouvé"));

        Comment comment = new Comment();
        comment.setId(UUID.randomUUID().toString());
        comment.setUserId(user.getId());
        comment.setUserFirstName(user.getFirstName());
        comment.setUserLastName(user.getLastName());
        comment.setText(commentDTO.getText());
        comment.setTimestamp(System.currentTimeMillis());

        if (publication.getComments() == null) {
            publication.setComments(new ArrayList<>());
        }

        publication.getComments().add(comment);
        publication = publicationRepository.save(publication);

        return mapToDTO(publication);
    }

    public PublicationDTO deleteComment(String publicationId, String commentId) {
        Publication publication = publicationRepository.findById(publicationId)
                .orElseThrow(() -> new IllegalArgumentException("Publication non trouvée"));

        if (publication.getComments() != null) {
            publication.setComments(
                    publication.getComments().stream()
                            .filter(c -> !c.getId().equals(commentId))
                            .collect(Collectors.toList())
            );
        }

        publication = publicationRepository.save(publication);
        return mapToDTO(publication);
    }
    public PublicationDTO removeLikeFromPublication(String publicationId, String userId) {
        Publication publication = publicationRepository.findById(publicationId)
                .orElseThrow(() -> new IllegalArgumentException("Publication non trouvée pour l'identifiant fourni : " + publicationId));

        if (publication.getLikes() != null && publication.getLikes().contains(userId)) {
            publication.getLikes().remove(userId);
            publication = publicationRepository.save(publication);
        }

        return mapToDTO(publication);
    }

}
