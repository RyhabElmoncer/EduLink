package com.EduLink.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
// CommentDTO.java
public class CommentDTO {
    private String id;
    private String userId;
    private String userFirstName;
    private String userLastName;
    private String text;
    private long timestamp;
}
