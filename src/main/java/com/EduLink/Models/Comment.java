package com.EduLink.Models;

import lombok.Data;

@Data
public class Comment {
    private String userId;
    private String text;
    private long timestamp;
}
