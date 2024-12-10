package com.EduLink.DTO;

import lombok.Data;

import java.util.List;

@Data
public class GroupDTO {
    private String name;
    private String description;
    private List<String> memberIds; // IDs des membres
}

