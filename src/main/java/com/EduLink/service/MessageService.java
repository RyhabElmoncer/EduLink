package com.EduLink.service;

import com.EduLink.DTO.MessageDTO;
import com.EduLink.Models.Group;
import com.EduLink.Models.Message;
import com.EduLink.Models.User;
import com.EduLink.repository.MessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MessageService {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public MessageDTO sendMessage(MessageDTO messageDTO) {
        // Création d'un nouveau message
        Message message = Message.builder()
                .content(messageDTO.getContent())
                .timestamp(System.currentTimeMillis())
                .isRead(false)
                .build();

        // Associer les utilisateurs et le groupe si nécessaire
        message.setSender(new User(messageDTO.getSender().getId()));
        message.setRecipient(new User(messageDTO.getRecipient().getId()));
        if (messageDTO.getGroupId() != null) {
            message.setGroup(new Group(messageDTO.getGroupId()));
        }

        // Sauvegarder dans la base de données
        message = messageRepository.save(message);

        // Notification en temps réel au destinataire
        messagingTemplate.convertAndSendToUser(
                messageDTO.getRecipient().getId(),
                "/queue/messages",
                new MessageDTO(message)
        );

        return new MessageDTO(message);
    }

    public List<MessageDTO> getMessages(String userId1, String userId2) {
        List<Message> messages = messageRepository.findConversation(userId1, userId2);
        return messages.stream().map(MessageDTO::new).collect(Collectors.toList());
    }

    public void deleteMessage(String messageId) {
        messageRepository.deleteById(messageId);
    }
}
