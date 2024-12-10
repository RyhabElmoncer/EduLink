package com.EduLink.service;

import com.EduLink.DTO.GroupDTO;
import com.EduLink.DTO.MessageDTO;
import com.EduLink.Exceptions.ResourceNotFoundException;
import com.EduLink.Models.Group;
import com.EduLink.Models.Message;
import com.EduLink.Models.User;
import com.EduLink.repository.GroupRepository;
import com.EduLink.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
public class GroupService {

    @Autowired
    private GroupRepository groupRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public GroupDTO createGroup(GroupDTO groupDTO) {
        Group group = Group.builder()
                .name(groupDTO.getName())
                .description(groupDTO.getDescription())
                .members(groupDTO.getMembers().stream()
                        .map(member -> userRepository.findById(member.getId())
                                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + member.getId())))
                        .collect(Collectors.toList()))
                .build();

        group = groupRepository.save(group);
        return new GroupDTO(group);
    }

    public void joinGroup(String groupId, String userId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found: " + groupId));

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + userId));

        group.getMembers().add(user);
        groupRepository.save(group);
    }

    public void leaveGroup(String groupId, String userId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found: " + groupId));

        group.getMembers().removeIf(user -> user.getId().equals(userId));
        groupRepository.save(group);
    }

    public MessageDTO postInGroup(String groupId, MessageDTO messageDTO) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found: " + groupId));

        User sender = userRepository.findById(messageDTO.getSender().getId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + messageDTO.getSender().getId()));

        Message message = Message.builder()
                .content(messageDTO.getContent())
                .timestamp(System.currentTimeMillis())
                .isRead(false)
                .sender(sender)
                .group(group)
                .build();

        group.getMessages().add(message);
        groupRepository.save(group);

        // Notifier tous les membres du groupe
        messagingTemplate.convertAndSend("/topic/groups/" + groupId, new MessageDTO(message));

        return new MessageDTO(message);
    }

    public List<MessageDTO> getGroupMessages(String groupId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new ResourceNotFoundException("Group not found: " + groupId));

        return group.getMessages().stream().map(MessageDTO::new).collect(Collectors.toList());
    }
}
