package com.EduLink.DTO;

import com.EduLink.Models.Group;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class GroupDTO {
    private String id;
    private String name;
    private String description;
    private List<UserDTO> members;
    private List<MessageDTO> messages;

    public GroupDTO(Group group) {
        this.id = group.getId();
        this.name = group.getName();
        this.description = group.getDescription();
        this.members = group.getMembers() != null ?
                group.getMembers().stream()
                        .map(user -> new UserDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail()))
                        .collect(Collectors.toList()) : null; // Assure que members n'est jamais null
        this.messages = group.getMessages() != null ?
                group.getMessages().stream().map(MessageDTO::new).collect(Collectors.toList()) : null; // Assure que messages n'est jamais null
    }
}
