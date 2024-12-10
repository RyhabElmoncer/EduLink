package com.EduLink.controller;

import com.EduLink.DTO.GroupDTO;
import com.EduLink.DTO.MessageDTO;
import com.EduLink.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/auth/groups")
public class GroupController {

    @Autowired
    private GroupService groupService;

    @PostMapping
    public ResponseEntity<GroupDTO> createGroup(@RequestBody GroupDTO groupDTO) {
        GroupDTO createdGroup = groupService.createGroup(groupDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdGroup);
    }

    @PostMapping("/{groupId}/join")
    public ResponseEntity<Void> joinGroup(@PathVariable String groupId, @RequestParam String userId) {
        groupService.joinGroup(groupId, userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{groupId}/leave")
    public ResponseEntity<Void> leaveGroup(@PathVariable String groupId, @RequestParam String userId) {
        groupService.leaveGroup(groupId, userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{groupId}/messages")
    public ResponseEntity<MessageDTO> postInGroup(@PathVariable String groupId, @RequestBody MessageDTO messageDTO) {
        MessageDTO post = groupService.postInGroup(groupId, messageDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(post);
    }

    @GetMapping("/{groupId}/messages")
    public ResponseEntity<List<MessageDTO>> getGroupMessages(@PathVariable String groupId) {
        List<MessageDTO> messages = groupService.getGroupMessages(groupId);
        return ResponseEntity.ok(messages);
    }
}

