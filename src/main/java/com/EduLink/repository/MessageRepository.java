package com.EduLink.repository;

import com.EduLink.Models.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
@Repository
public interface MessageRepository extends MongoRepository<Message, String> {

    // Récupérer les messages d'une conversation entre deux utilisateurs
    @Query("{ $or: [ { 'sender.id': ?0, 'recipient.id': ?1 }, { 'sender.id': ?1, 'recipient.id': ?0 } ] }")
    List<Message> findConversation(String userId1, String userId2);
    Page<Message> findByGroupId(String groupId, Pageable pageable);

    // Récupérer les messages d'un groupe
    List<Message> findByGroupId(String groupId);
}

