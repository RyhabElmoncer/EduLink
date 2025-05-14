package com.EduLink.Models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
// Comment.java
public class Comment {
    private String id;
    private String userId;
    private String userFirstName;
    private String userLastName;
    private String text;
    private long timestamp;
}

